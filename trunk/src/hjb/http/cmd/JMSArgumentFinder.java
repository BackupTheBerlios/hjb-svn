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

import javax.jms.*;

import org.apache.log4j.Logger;

import hjb.http.HJBConstants;
import hjb.jms.HJBProvider;
import hjb.jms.HJBRoot;
import hjb.misc.*;
import hjb.msg.HJBMessage;

/**
 * <code>JMSArgumentFinder</code> encapsulates the logic used to extract the
 * various arguments used in the construction of <code>JMSCommand</code>s
 * from a <code>Map</code>
 * 
 * @author Tim Emiola
 */
public class JMSArgumentFinder {

    public HJBMessage findHJBMessage(Map decodedParameters) {
        String messageAsText = findHJBMessageAsText(decodedParameters);
        Map jmsHeaders = new HashMap(decodedParameters);
        jmsHeaders.remove(HJBConstants.MESSAGE_TO_SEND);
        jmsHeaders.remove(HJBConstants.DELIVERY_MODE);
        jmsHeaders.remove(HJBConstants.TIME_TO_LIVE);
        jmsHeaders.remove(HJBConstants.DESTINATION_URL);
        jmsHeaders.remove(HJBConstants.PRIORITY);
        return new HJBMessage(jmsHeaders, messageAsText);
    }

    public Hashtable findEnvironment(Map decodedParameters, String providerName) {
        Hashtable result = new Hashtable();
        result.putAll(decodedParameters);
        result.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        return result;
    }

    public String findMessageSelector(Map decodedParameters) {
        Object rawValue = decodedParameters.get(HJBConstants.MESSAGE_SELECTOR);
        return (null == rawValue) ? null : "" + rawValue;
    }

    public boolean findDisableTimestamps(Map decodedParameters) {
        Boolean rawValue = findBoolean(decodedParameters,
                                       HJBConstants.DISABLE_TIMESTAMPS,
                                       "disable timestamps",
                                       new Boolean(false));
        if (null == rawValue) {
            return false;
        }
        return ((Boolean) rawValue).booleanValue();
    }

    public boolean findDisableMessageIds(Map decodedParameters) {
        Boolean rawValue = findBoolean(decodedParameters,
                                       HJBConstants.DISABLE_MESSAGE_IDS,
                                       "disable message IDs",
                                       new Boolean(false));
        if (null == rawValue) {
            return false;
        }
        return ((Boolean) rawValue).booleanValue();
    }

    public boolean findNoLocal(Map decodedParameters) {
        Boolean rawValue = findBoolean(decodedParameters,
                                       HJBConstants.CONSUMER_NOLOCAL,
                                       "noLocal",
                                       new Boolean(HJBConstants.DEFAULT_NOLOCAL));
        if (null == rawValue) {
            return HJBConstants.DEFAULT_NOLOCAL;
        }
        return ((Boolean) rawValue).booleanValue();

    }

    public Integer findDeliveryMode(Map decodedParameters) {
        Integer rawValue = findInteger(decodedParameters,
                                       HJBConstants.DELIVERY_MODE,
                                       "delivery mode",
                                       null);
        if (null == rawValue) return null;
        switch (rawValue.intValue()) {
            case DeliveryMode.PERSISTENT:
            case DeliveryMode.NON_PERSISTENT:
                return (Integer) rawValue;
            default:
                if (LOG.isDebugEnabled()) {
                    String message = strings().getString(HJBStrings.IGNORE_AND_DEFAULT_WARNING,
                                                         "delivery mode",
                                                         rawValue,
                                                         null);
                    LOG.debug(message);
                }
                return null;
        }
    }

    public Integer findPriority(Map decodedParameters) {
        Integer rawValue = findInteger(decodedParameters,
                                       HJBConstants.PRIORITY,
                                       "priority",
                                       null);
        if (null == rawValue) return null;
        if (rawValue.intValue() < MINIMUM_PRIORITY
                || rawValue.intValue() > MAXIMUM_PRIORITY) {
            if (LOG.isDebugEnabled()) {
                String message = strings().getString(HJBStrings.IGNORE_AND_DEFAULT_WARNING,
                                                     "priority",
                                                     rawValue,
                                                     null);
                LOG.debug(message);
            }
            return null;
        }
        return (Integer) rawValue;
    }

    public Long findTimeout(Map decodedParameters) {
        return findLong(decodedParameters,
                        HJBConstants.TIMEOUT,
                        "timeout",
                        null);
    }

    public Long findTimeToLive(Map decodedParameters) {
        return findLong(decodedParameters,
                        HJBConstants.TIME_TO_LIVE,
                        "time to live",
                        null);
    }

    public MessageProducerArguments findProducerArguments(Map decodedParameters) {
        return new MessageProducerArguments(findDisableTimestamps(decodedParameters),
                                            findDisableMessageIds(decodedParameters),
                                            findTimeToLive(decodedParameters),
                                            findDeliveryMode(decodedParameters),
                                            findPriority(decodedParameters));
    }

    public int findAcknowledgementMode(Map decodedParameters) {
        Integer rawValue = findInteger(decodedParameters,
                                       HJBConstants.SESSION_ACKNOWLEDGEMENT_MODE,
                                       "acknowledgement mode",
                                       new Integer(HJBConstants.DEFAULT_ACKNOWLEDGEMENT_MODE));
        if (null == rawValue) {
            return HJBConstants.DEFAULT_ACKNOWLEDGEMENT_MODE;
        }
        switch (rawValue.intValue()) {
            case Session.CLIENT_ACKNOWLEDGE:
            case Session.DUPS_OK_ACKNOWLEDGE:
            case Session.SESSION_TRANSACTED:
                return rawValue.intValue();
            default:
                if (LOG.isDebugEnabled()) {
                    String message = strings().getString(HJBStrings.IGNORE_AND_DEFAULT_WARNING,
                                                         "acknowledgement mode",
                                                         rawValue,
                                                         new Integer(HJBConstants.DEFAULT_ACKNOWLEDGEMENT_MODE));
                    LOG.debug(message);
                }
                return HJBConstants.DEFAULT_ACKNOWLEDGEMENT_MODE;
        }
    }

    public boolean findTransacted(Map decodedParameters) {
        Boolean rawValue = findBoolean(decodedParameters,
                                       HJBConstants.SESSION_TRANSACTED,
                                       "transacted",
                                       new Boolean(HJBConstants.DEFAULT_TRANSACTED));
        if (null == rawValue) {
            return HJBConstants.DEFAULT_TRANSACTED;
        }
        return ((Boolean) rawValue).booleanValue();
    }

    public String findSubscriberName(Map decodedParameters) {
        Object rawSubscriberName = decodedParameters.get(HJBConstants.SUBSCRIBER_NAME);
        if (!(rawSubscriberName instanceof String)) {
            String message = strings().getString(HJBStrings.INVALID_SUBSCRIBER_NAME,
                                                 rawSubscriberName);
            throw new HJBClientException(message);
        }
        return (String) rawSubscriberName;
    }
    
    
    public String findClientId(Map decodedParameters) {
        Object rawValue = decodedParameters.get(HJBConstants.CLIENT_ID);
        return (null == rawValue) ? null : "" + rawValue;
    }

    public Queue findQueue(Map decodedParameters,
                           HJBRoot root,
                           String sessionProviderName) {
        Destination rawValue = findRequiredDestination(decodedParameters,
                                                       root,
                                                       sessionProviderName);
        if (!(rawValue instanceof Queue)) {
            String message = strings().getString(HJBStrings.INVALID_DESTINATION_TYPE,
                                                 rawValue.getClass().getName(),
                                                 Queue.class.getName());
            throw new HJBClientException(message);
        }
        return (Queue) rawValue;
    }

    public Topic findTopic(Map decodedParameters,
                           HJBRoot root,
                           String sessionProviderName) {
        Destination rawValue = findRequiredDestination(decodedParameters,
                                                       root,
                                                       sessionProviderName);
        if (!(rawValue instanceof Topic)) {
            String message = strings().getString(HJBStrings.INVALID_DESTINATION_TYPE,
                                                 rawValue.getClass().getName(),
                                                 Topic.class.getName());
            throw new HJBClientException(message);
        }
        return (Topic) rawValue;
    }

    public Destination findRequiredDestination(Map decodedParameters,
                                               HJBRoot root,
                                               String sessionProviderName) {
        return findRequiredDestination(decodedParameters,
                                       root,
                                       sessionProviderName,
                                       true);
    }

    public Destination findOptionalDestination(Map decodedParameters,
                                               HJBRoot root,
                                               String sessionProviderName) {
        try {
            return findRequiredDestination(decodedParameters,
                                           root,
                                           sessionProviderName,
                                           false);
        } catch (HJBNotFoundException e) {
            return null;
        }
    }

    protected Destination findRequiredDestination(Map decodedParameters,
                                                  HJBRoot root,
                                                  String sessionProviderName,
                                                  boolean throwOnFailure) {
        Object rawValue = decodedParameters.get(HJBConstants.DESTINATION_URL);
        if (!assertIsValidDestinationURL(rawValue, throwOnFailure)) {
            return null;
        }
        String destinationURL = (String) rawValue;
        Matcher m = getDestinationPathMatcher().matcher(destinationURL);
        m.matches();

        String destinationProviderName = m.group(1);
        String destinationName = applyURLDecoding(m.group(2));
        assertIsSameProvider(sessionProviderName,
                             destinationProviderName,
                             throwOnFailure);
        HJBTreeWalker walker = new HJBTreeWalker(root,
                                                 destinationURL,
                                                 throwOnFailure);
        return walker.findDestination(destinationProviderName, destinationName);
    }

    protected Integer findInteger(Map decodedParameters,
                                  String key,
                                  String name,
                                  Object defaultValue) {
        Object rawValue = decodedParameters.get(key);
        if (!(rawValue instanceof Integer)) {
            if (LOG.isDebugEnabled()) {
                String message = strings().getString(HJBStrings.IGNORE_AND_DEFAULT_WARNING,
                                                     name,
                                                     rawValue,
                                                     defaultValue);
                LOG.debug(message);
            }
            rawValue = defaultValue;
        }
        return (Integer) rawValue;
    }

    protected Boolean findBoolean(Map decodedParameters,
                                  String key,
                                  String name,
                                  Object defaultValue) {
        Object rawValue = decodedParameters.get(key);
        if (!(rawValue instanceof Boolean)) {
            if (LOG.isDebugEnabled()) {
                String message = strings().getString(HJBStrings.IGNORE_AND_DEFAULT_WARNING,
                                                     name,
                                                     rawValue,
                                                     defaultValue);
                LOG.debug(message);
            }
            rawValue = defaultValue;
        }
        return (Boolean) rawValue;
    }

    protected Long findLong(Map decodedParameters,
                            String key,
                            String name,
                            Object defaultValue) {
        Object rawValue = decodedParameters.get(key);
        if (!(rawValue instanceof Long)) {
            if (LOG.isDebugEnabled()) {
                String message = strings().getString(HJBStrings.IGNORE_AND_DEFAULT_WARNING,
                                                     name,
                                                     rawValue,
                                                     defaultValue);
                LOG.debug(message);
            }
            rawValue = defaultValue;
        }
        return (Long) rawValue;
    }

    protected String findHJBMessageAsText(Map decodedParameters) {
        Object rawValue = decodedParameters.get(HJBConstants.MESSAGE_TO_SEND);
        if (!(rawValue instanceof String)) {
            String message = strings().getString(HJBStrings.INVALID_MESSAGE,
                                                 rawValue);
            LOG.error(message);
            throw new HJBClientException(message);
        }
        return (String) rawValue;
    }

    protected void assertIsSameProvider(String sessionProviderName,
                                        String destinationProviderName,
                                        boolean throwOnFailure)
            throws HJBClientException {
        if (!destinationProviderName.equals(sessionProviderName)) {
            if (throwOnFailure) {
                String message = strings().getString(HJBStrings.CAN_NOT_USE_DESTINATION,
                                                     destinationProviderName,
                                                     sessionProviderName);
                LOG.error(message);
                throw new HJBClientException(message);
            } else if (LOG.isDebugEnabled()) {
                String message = strings().getString(HJBStrings.CAN_NOT_USE_DESTINATION,
                                                     destinationProviderName,
                                                     sessionProviderName);
                LOG.debug(message);
            }
        }
    }

    protected boolean assertIsValidDestinationURL(Object rawDestinationURL,
                                                  boolean throwOnFailure)
            throws HJBClientException {
        if (rawDestinationURL instanceof String) {
            Matcher m = getDestinationPathMatcher().matcher((String) rawDestinationURL);
            if (!m.matches()) {
                String message = strings().getString(HJBStrings.INVALID_DESTINATION_URL,
                                                     rawDestinationURL);
                LOG.error(message);
                throw new HJBNotFoundException(message);
            }
            return true;
        }
        if (throwOnFailure) {
            String message = strings().getString(HJBStrings.INVALID_DESTINATION_URL,
                                                 rawDestinationURL);
            LOG.error(message);
            throw new HJBNotFoundException(message);
        } else if (LOG.isDebugEnabled()) {
            String message = strings().getString(HJBStrings.INVALID_DESTINATION_URL,
                                                 rawDestinationURL);
            LOG.debug(message);
        }
        return false;
    }

    protected String applyURLDecoding(String s) {
        try {
            return URLDecoder.decode(s, HJBConstants.JAVA_CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            String message = strings().getString(HJBStrings.ENCODING_NOT_SUPPORTED,
                                                 HJBConstants.JAVA_CHARSET_UTF8);
            LOG.error(message, e);
            throw new HJBException(message);
        }
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected Pattern getDestinationPathMatcher() {
        return DESTINATION_PATH_MATCHER;
    }

    public static final int MAXIMUM_PRIORITY = 9;
    public static final int MINIMUM_PRIORITY = 0;
    private static final Logger LOG = Logger.getLogger(JMSArgumentFinder.class);
    private static final Pattern DESTINATION_PATH_MATCHER = Pattern.compile("^/[^/]*/[^/]*/(\\w+)/"
            + PathNaming.DESTINATION + "/(.+)/?$");
    private static final HJBStrings STRINGS = new HJBStrings();
    
}
