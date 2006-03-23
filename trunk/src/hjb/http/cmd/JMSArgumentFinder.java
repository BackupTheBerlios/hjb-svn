/*
HJB (HTTP JMS Bridge) links the HTTP protocol to the JMS API.
Copyright (C) 2006 Timothy Emiola

HJB is free software; you can redistribute it and/or modify it under
the terms of the GNU Lesser General Public License as published by the
Free Software Foundation; either version 2.1 of the License, or (at
your option) any later version.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301
USA

*/
package hjb.http.cmd;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.log4j.Logger;

import hjb.http.HJBServletConstants;
import hjb.jms.HJBProvider;
import hjb.jms.HJBRoot;
import hjb.misc.HJBClientException;
import hjb.misc.HJBException;
import hjb.misc.HJBNotFoundException;
import hjb.misc.HJBStrings;
import hjb.msg.HJBMessage;

/**
 * <code>JMSArgumentFinder</code> encapsulates the logic used to extract the
 * various arguments used in the construction of <code>JMSCommand</code>s
 * from a <code>Map</code>
 * 
 * TODO write unit tests for this class
 * 
 * @author Tim Emiola
 */
public class JMSArgumentFinder {

    public HJBMessage findHJBMessage(Map decodedParameters) {
        String messageAsText = findHJBMessageAsText(decodedParameters);
        Map jmsHeaders = new HashMap(decodedParameters);
        jmsHeaders.remove(HJBServletConstants.MESSAGE_TO_SEND);
        return new HJBMessage(jmsHeaders, messageAsText);
    }

    public Hashtable findEnvironment(Map decodedParameters, String providerName) {
        Hashtable result = new Hashtable();
        result.putAll(decodedParameters);
        result.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        return result;
    }

    public String findMessageSelector(Map decodedParameters) {
        Object rawMessageSelector = decodedParameters.get(HJBServletConstants.MESSAGE_SELECTOR);
        return (null == rawMessageSelector) ? null : "" + rawMessageSelector;
    }

    public Integer findTimeout(Map decodedParameters) {
        Object rawTimeout = decodedParameters.get(HJBServletConstants.TIMEOUT);
        if (!(rawTimeout instanceof Integer)) {
            return null;
        }
        return (Integer) rawTimeout;
    }

    public int findAcknowledgementMode(Map decodedParameters) {
        Object rawAcknowledgementMode = decodedParameters.get(HJBServletConstants.SESSION_ACKNOWLEDGEMENT_MODE);
        if (!(rawAcknowledgementMode instanceof Integer)) {
            String message = strings().getString("acknowledgement mode",
                                                 rawAcknowledgementMode,
                                                 new Integer(HJBServletConstants.DEFAULT_ACKNOWLEDGEMENT_MODE));
            LOG.warn(message);
            return HJBServletConstants.DEFAULT_ACKNOWLEDGEMENT_MODE;
        }
        int acknowledgementValue = ((Integer) rawAcknowledgementMode).intValue();
        switch (acknowledgementValue) {
        case Session.CLIENT_ACKNOWLEDGE:
        case Session.DUPS_OK_ACKNOWLEDGE:
        case Session.SESSION_TRANSACTED:
            return acknowledgementValue;
        default:
            String message = strings().getString("acknowledgement mode",
                                                 rawAcknowledgementMode,
                                                 new Integer(HJBServletConstants.DEFAULT_ACKNOWLEDGEMENT_MODE));
            LOG.warn(message);
            return HJBServletConstants.DEFAULT_ACKNOWLEDGEMENT_MODE;
        }
    }

    public boolean findTransacted(Map decodedParameters) {
        Object rawTransacted = decodedParameters.get(HJBServletConstants.SESSION_TRANSACTED);
        if (!(rawTransacted instanceof Boolean)) {
            String message = strings().getString("transacted",
                                                 rawTransacted,
                                                 new Boolean(HJBServletConstants.DEFAULT_TRANSACTED));
            LOG.warn(message);
            return HJBServletConstants.DEFAULT_TRANSACTED;
        }
        return ((Boolean) rawTransacted).booleanValue();
    }

    public Queue findQueue(Map decodedParameters,
                           HJBRoot root,
                           String sessionProviderName) {
        Destination rawDestination = findDestination(decodedParameters,
                                                     root,
                                                     sessionProviderName);
        if (!(rawDestination instanceof Queue)) {
            String message = strings().getString(HJBStrings.INVALID_DESTINATION_TYPE,
                                                 rawDestination.getClass().getName(),
                                                 Queue.class.getName());
            throw new HJBClientException(message);
        }
        return (Queue) rawDestination;
    }

    public boolean findNoLocal(PatternMatchingCommandGenerator generator,
                               Map decodedParameters) {
        Object rawNoLocal = decodedParameters.get(HJBServletConstants.CONSUMER_NOLOCAL);
        if (!(rawNoLocal instanceof Boolean)) {
            String message = generator.strings().getString(HJBStrings.IGNORE_AND_DEFAULT_WARNING,
                                                           "noLocal",
                                                           rawNoLocal,
                                                           new Boolean(HJBServletConstants.DEFAULT_NOLOCAL));
            LOG.warn(message);
            return HJBServletConstants.DEFAULT_NOLOCAL;
        }
        return ((Boolean) rawNoLocal).booleanValue();
    }

    public String findSubscriberName(Map decodedParameters) {
        Object rawSubscriberName = decodedParameters.get(HJBServletConstants.SUBSCRIBER_NAME);
        if (!(rawSubscriberName instanceof String)) {
            String message = strings().getString(HJBStrings.INVALID_SUBSCRIBER_NAME,
                                                 rawSubscriberName);
            throw new HJBClientException(message);
        }
        return (String) rawSubscriberName;
    }

    public Topic findTopic(Map decodedParameters,
                           HJBRoot root,
                           String sessionProviderName) {
        Destination rawDestination = findDestination(decodedParameters,
                                                     root,
                                                     sessionProviderName);
        if (!(rawDestination instanceof Topic)) {
            String message = strings().getString(HJBStrings.INVALID_DESTINATION_TYPE,
                                                 rawDestination.getClass().getName(),
                                                 Topic.class.getName());
            throw new HJBClientException(message);
        }
        return (Topic) rawDestination;
    }

    public Destination findDestination(Map decodedParameters,
                                       HJBRoot root,
                                       String sessionProviderName) {
        Object rawURL = decodedParameters.get(HJBServletConstants.DESTINATION_URL);
        assertIsValidDestinationURL(rawURL);
        String destinationURL = (String) rawURL;
        Matcher m = getDestinationPathMatcher().matcher(destinationURL);
        m.matches();

        String destinationProviderName = m.group(1);
        String destinationName = applyURLDecoding(m.group(2));
        assertIsSameProvider(sessionProviderName, destinationProviderName);
        HJBProvider provider = root.getProvider(sessionProviderName);
        if (null == provider)
            handleMissingComponent(destinationURL, destinationProviderName);
        Destination destination = provider.getDestination(destinationName);
        if (null == destination)
            handleMissingComponent(destinationURL, destinationName);
        return destination;
    }

    protected String findHJBMessageAsText(Map decodedParameters) {
        Object rawMessageText = decodedParameters.get(HJBServletConstants.MESSAGE_TO_SEND);
        if (!(rawMessageText instanceof String)) {
            String message = strings().getString(HJBStrings.INVALID_MESSAGE,
                                                 rawMessageText);
            throw new HJBClientException(message);
        }
        return (String) rawMessageText;
    }

    protected void handleMissingComponent(String pathInfo, String component)
            throws HJBException {
        String message = strings().getString(HJBStrings.ALLOWED_PATH_NOT_FOUND,
                                             pathInfo,
                                             component);
        throw new HJBNotFoundException(message);
    }

    protected void assertIsSameProvider(String sessionProviderName,
                                        String destinationProviderName)
            throws HJBClientException {
        if (!destinationProviderName.equals(sessionProviderName)) {
            String message = strings().getString(HJBStrings.CAN_NOT_USE_DESTINATION,
                                                 destinationProviderName,
                                                 sessionProviderName);
            throw new HJBClientException(message);
        }
    }

    protected void assertIsValidDestinationURL(Object rawDestinationURL)
            throws HJBClientException {
        if (!(rawDestinationURL instanceof String)) {
            String message = strings().getString(HJBStrings.INVALID_DESTINATION_URL,
                                                 rawDestinationURL);
            throw new HJBNotFoundException(message);
        }
    }

    protected String applyURLDecoding(String s) {
        try {
            return URLDecoder.decode(s, HJBServletConstants.JAVA_CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            String message = strings().getString(HJBStrings.ENCODING_NOT_SUPPORTED,
                                                 HJBServletConstants.JAVA_CHARSET_UTF8);
            throw new HJBException(message);
        }
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected Pattern getDestinationPathMatcher() {
        return DESTINATION_PATH_MATCHER;
    }

    private static final Logger LOG = Logger.getLogger(JMSArgumentFinder.class);
    private static Pattern DESTINATION_PATH_MATCHER = Pattern.compile("^/(\\w+)/([^/]+)/?$");
    private static HJBStrings STRINGS = new HJBStrings();
}
