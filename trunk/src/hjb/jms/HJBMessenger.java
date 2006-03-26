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

import java.util.*;

import javax.jms.*;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.HJBMessage;
import hjb.msg.MessageCopier;
import hjb.msg.MessageCopierFactory;

/**
 * <code>HJBMessenger</code> uses a <code>HJBConnection</code> to send and
 * receive {@link hjb.msg.HJBMessage}s as JMS messages.
 * 
 * TODO sort how to configure the default timeout and who does it
 * 
 * TODO sort how to configure the minimum timeout
 * 
 * TODO write junit tests for this class
 * 
 * @author Tim Emiola
 */
public class HJBMessenger {

    public HJBMessenger(HJBConnection theConnection, int sessionIndex) {
        if (null == theConnection)
            throw new IllegalArgumentException(strings().needsANonNull(HJBConnection.class.getName()));
        this.theConnection = theConnection;
        this.sessionIndex = sessionIndex;
        verifySessionIndex();
        setTimeout(DEFAULT_TIMEOUT);
        minimumTimeout = MINIMUM_TIMEOUT;
    }

    public HJBMessage[] viewQueue(int index) throws HJBException {
        try {
            List inQueue = Collections.list(getBrowserFor(index).getEnumeration());
            List result = new ArrayList();
            for (Iterator i = inQueue.iterator(); i.hasNext();) {
                Message asJMS = (Message) i.next();
                HJBMessage asHJB = createHJBMessageFor(asJMS);
                findMessageCopierFor(asJMS).copyToHJBMessage(asJMS, asHJB);
                result.add(asHJB);
            }
            return (HJBMessage[]) result.toArray(new HJBMessage[result.size()]);
        } catch (JMSException e) {
            String message = strings().getString(HJBStrings.COULD_NOT_BROWSE_QUEUE,
                                                 new Integer(getSessionIndex()),
                                                 new Integer(index));
            LOG.error(message, e);
            throw new HJBException(message, e);
        }
    }

    public void send(HJBMessage asHJB, int index) throws HJBException {
        try {
            if (null == asHJB) {
                String message = strings().getString(HJBStrings.IGNORED_NULL_HJB_MESSAGE,
                                                     new Integer(getSessionIndex()),
                                                     new Integer(index));
                LOG.warn(message);
                return;
            }
            LOG.info("About to send HJB message: " + asHJB);
            Message asJMS = createJMSMessageFor(asHJB);
            findMessageCopierFor(asHJB).copyToJMSMessage(asHJB, asJMS);
            getProducerFor(index).send(asJMS);
        } catch (JMSException e) {
            String message = strings().getString(HJBStrings.SEND_OF_MESSAGE_FAILED,
                                                 new Integer(getSessionIndex()));
            LOG.error(message);
            throw new HJBException(message);
        }
    }

    public HJBMessage receiveFromConsumer(int index) throws HJBException {
        return receiveFromConsumer(index, getTimeout());
    }

    public HJBMessage receiveFromConsumer(int index, long timeout)
            throws HJBException {
        return receiveFromConsumer(getConsumerFor(index), timeout);
    }

    public HJBMessage receiveFromConsumerNoWait(int index) throws HJBException {
        return receiveFromConsumerNoWait(getConsumerFor(index));
    }

    public HJBMessage receiveFromSubscriber(int index) throws HJBException {
        return receiveFromSubscriber(index, getTimeout());
    }

    public HJBMessage receiveFromSubscriberNoWait(int index)
            throws HJBException {
        return receiveFromConsumerNoWait(getSubscriberFor(index));
    }

    public HJBMessage receiveFromSubscriber(int index, long timeout)
            throws HJBException {
        return receiveFromConsumer(getSubscriberFor(index), timeout);
    }

    public int getSessionIndex() {
        return sessionIndex;
    }

    protected HJBMessage receiveFromConsumer(MessageConsumer aConsumer,
                                             long timeout) throws HJBException {
        try {
            return processReceivedMessage(aConsumer.receive(Math.max(timeout,
                                                                     getMinimumTimeout())));
        } catch (JMSException e) {
            return handleReceiptFailure();
        }
    }

    protected HJBMessage receiveFromConsumerNoWait(MessageConsumer aConsumer)
            throws HJBException {
        try {
            return processReceivedMessage(aConsumer.receiveNoWait());
        } catch (JMSException e) {
            return handleReceiptFailure();
        }
    }

    protected HJBMessage processReceivedMessage(Message asJMS)
            throws HJBException {
        try {
            if (null == asJMS) {
                String message = strings().getString(HJBStrings.NO_MESSAGE_AVAILABLE,
                                                     new Integer(getSessionIndex()));
                LOG.error(message);
                throw new HJBException(message);
            }
            asJMS.acknowledge();
            HJBMessage result = createHJBMessageFor(asJMS);
            findMessageCopierFor(asJMS).copyToHJBMessage(asJMS, result);
            LOG.info("Created HJB message: " + result);
            return result;
        } catch (JMSException e) {
            return handleReceiptFailure();
        }
    }

    protected HJBMessage handleReceiptFailure() throws HJBException {
        String message = strings().getString(HJBStrings.RECEIVE_OF_MESSAGE_FAILED,
                                             new Integer(getSessionIndex()));
        LOG.error(message);
        throw new HJBException(message);
    }

    protected MessageCopier findMessageCopierFor(HJBMessage hjbMessage) {
        return getCopierFactory().getCopierFor(hjbMessage);
    }

    protected MessageCopier findMessageCopierFor(Message jmsMessage) {
        return getCopierFactory().getCopierFor(jmsMessage);
    }

    protected Message createJMSMessageFor(HJBMessage source) {
        SessionMessageFactory f = new SessionMessageFactory(getSession());
        return f.createMessage(source);
    }

    protected HJBMessage createHJBMessageFor(Message source) {
        HJBMessage result = new HJBMessage(new HashMap(), "");
        result.addHeader(MessageCopierFactory.HJB_JMS_CLASS,
                         source.getClass().getName());
        return result;
    }

    protected MessageConsumer getConsumerFor(int index) {
        HJBSessionConsumers consumers = getTheConnection().getSessionConsumers();
        return consumers.getConsumer(getSessionIndex(), index);
    }

    protected MessageProducer getProducerFor(int index) {
        HJBSessionProducers producers = getTheConnection().getSessionProducers();
        return producers.getProducer(getSessionIndex(), index);
    }

    protected QueueBrowser getBrowserFor(int index) {
        HJBSessionQueueBrowsers browsers = getTheConnection().getSessionBrowsers();
        return browsers.getBrowser(getSessionIndex(), index);
    }

    protected TopicSubscriber getSubscriberFor(int index) {
        HJBSessionDurableSubscribers subscribers = getTheConnection().getSessionSubscribers();
        return subscribers.getSubscriber(getSessionIndex(), index);
    }

    protected Session getSession() {
        try {
            return getTheConnection().getSession(getSessionIndex());
        } catch (IndexOutOfBoundsException e) {
            String message = strings().getString(HJBStrings.SESSION_NOT_FOUND,
                                                 new Integer(sessionIndex));
            LOG.error(message);
            throw new HJBException(message);
        }
    }

    protected void verifySessionIndex() {
        getSession();
    }

    protected HJBConnection getTheConnection() {
        return theConnection;
    }

    protected MessageCopierFactory getCopierFactory() {
        return COPIER_FACTORY;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    protected long getTimeout() {
        return timeout;
    }

    protected long getMinimumTimeout() {
        return minimumTimeout;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private HJBConnection theConnection;
    private int sessionIndex;
    private long timeout;
    private long minimumTimeout;

    public static long DEFAULT_TIMEOUT = 1000 * 15 * 60;
    public static long MINIMUM_TIMEOUT = 1000 * 2 * 60;

    private static final MessageCopierFactory COPIER_FACTORY = new MessageCopierFactory();
    private static final Logger LOG = Logger.getLogger(HJBMessenger.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
