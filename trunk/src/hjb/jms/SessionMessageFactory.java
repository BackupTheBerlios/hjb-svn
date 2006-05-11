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
package hjb.jms;

import javax.jms.*;

import org.apache.log4j.Logger;

import hjb.misc.HJBClientException;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.HJBMessage;
import hjb.msg.MessageCopierFactory;

/**
 * <code>SessionMessageFactory</code> allows <code>Session</code>s to be
 * used a 'keyed' factory for <code>Message</code>s, with the key being the
 * class name of the <code>Message</code>.
 * 
 * @author Tim Emiola
 */
public class SessionMessageFactory {

    /**
     * Creates a <code>SessionMessageFactory</code> from <code>Session</code>.
     * 
     * @param theSession
     *            the <code>Session</code> which will be used to create the
     *            <code>Messages</code>
     */
    public SessionMessageFactory(Session theSession) {
        if (null == theSession) {
            throw new IllegalArgumentException(strings().needsANonNull(Session.class.getName()));
        }
        this.theSession = theSession;
    }

    /**
     * Creates <code>Message</code> based on the contents of
     * <code>hjbMessage</code>
     * 
     * @param hjbMessage
     *            an <code>HJBMessage</code>
     * 
     * Uses the value of the header named
     * {@link MessageCopierFactory#HJB_JMS_MESSAGE_INTERFACE} to determine what
     * message type to create.
     * 
     * @return a JMS <code>Message</code>
     * @throws HJBException
     *             if a problem occurs while creating the <code>Message<code>
     */
    public Message createMessage(HJBMessage hjbMessage) throws HJBException {
        return createMessage(hjbMessage.getHeader(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE));
    }

    /**
     * Establishes a link between a <code>JMS</code> message class name, and
     * the <code>Session</code> API method used to create instances of that
     * class.
     * 
     * @param messageClazz
     *            an JMS message class name
     * @return an instance of messageClazz, created using this instances
     *         <code>Session</code>
     * @throws HJBException
     *             if messageClazz is invalid, or if any other problem occurs
     *             during <code>Message</code> instantiation
     */
    public Message createMessage(String messageClazz) throws HJBException {
        try {
            if (TextMessage.class.getName().equals(messageClazz)) {
                return getTheSession().createTextMessage();
            }
            if (ObjectMessage.class.getName().equals(messageClazz)) {
                return getTheSession().createObjectMessage();
            }
            if (StreamMessage.class.getName().equals(messageClazz)) {
                return getTheSession().createStreamMessage();
            }
            if (BytesMessage.class.getName().equals(messageClazz)) {
                return getTheSession().createBytesMessage();
            }
            if (MapMessage.class.getName().equals(messageClazz)) {
                return getTheSession().createMapMessage();
            }
            String message = strings().getString(HJBStrings.COULD_NOT_CREATE_JMS_MESSAGE,
                                                 messageClazz);
            LOG.error(message);
            throw new HJBClientException(message);
        } catch (JMSException e) {
            String message = strings().getString(HJBStrings.COULD_NOT_CREATE_JMS_MESSAGE,
                                                 messageClazz);
            LOG.error(message, e);
            throw new HJBException(message, e);
        }
    }

    protected Session getTheSession() {
        return theSession;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private Session theSession;

    private static final Logger LOG = Logger.getLogger(SessionMessageFactory.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}