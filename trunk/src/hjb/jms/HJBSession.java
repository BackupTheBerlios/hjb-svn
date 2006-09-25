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

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

import javax.jms.*;

import org.apache.log4j.Logger;

import hjb.jms.cmd.JMSCommandRunner;
import hjb.jms.info.BaseJMSObjectDescription;
import hjb.jms.info.SessionDescription;
import hjb.misc.Clock;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * HJBSession decorates a {@link javax.jms.Session}.
 * <p />
 * It provides
 * <ul>
 * <li>storage for the <code>MessagesConsumers</code> created by the
 * decorated <code>Session</code></li>
 * <li>storage for the <code>MessageProducers</code> created by the the
 * decorated <code>Session</code></li>
 * <li>storage for the <code>MessageConsumers</code> created by the the
 * decorated <code>Session</code></li>
 * <li>storage for the <code>DurableSubscribers</code> created by the the
 * decorated <code>Session</code></li>
 * <li>storage for the <code>QueueBrowsers</code> created by the the
 * decorated <code>Session</code></li>
 * <li>storage for the <code>Threads</code> on which the <code>Session</code>'s
 * methods are executed</li>
 * </ul>
 * <p />
 * 
 * @author Tim Emiola
 */
public class HJBSession implements Session {

    public HJBSession(Session theSession, int sessionIndex, Clock aClock) {
        if (null == aClock) {
            throw new IllegalArgumentException(strings().needsANonNull(Clock.class));
        }
        if (null == theSession) {
            throw new IllegalArgumentException(strings().needsANonNull(Session.class));
        }
        this.theSession = theSession;
        this.sessionIndex = sessionIndex;
        this.clock = aClock;
        this.creationTime = aClock.getCurrentTime();
        this.browsers = new HJBSessionQueueBrowsers(this, aClock);
        this.consumers = new HJBSessionConsumers(this, aClock);
        this.producers = new HJBSessionProducers(this, aClock);
        this.durableSubscribers = new HJBSessionDurableSubscribers(this, aClock);
        this.commandRunner = new JMSCommandRunner();
    }

    public BytesMessage createBytesMessage() throws JMSException {
        return getTheSession().createBytesMessage();
    }

    public MapMessage createMapMessage() throws JMSException {
        return getTheSession().createMapMessage();
    }

    public Message createMessage() throws JMSException {
        return getTheSession().createMessage();
    }

    public ObjectMessage createObjectMessage() throws JMSException {
        return getTheSession().createObjectMessage();
    }

    public ObjectMessage createObjectMessage(Serializable s)
            throws JMSException {
        return getTheSession().createObjectMessage(s);
    }

    public StreamMessage createStreamMessage() throws JMSException {
        return getTheSession().createStreamMessage();
    }

    public TextMessage createTextMessage() throws JMSException {
        return getTheSession().createTextMessage();
    }

    public TextMessage createTextMessage(String s) throws JMSException {
        return getTheSession().createTextMessage(s);
    }

    public boolean getTransacted() throws JMSException {
        return getTheSession().getTransacted();
    }

    public int getAcknowledgeMode() throws JMSException {
        return getTheSession().getAcknowledgeMode();
    }

    public void commit() throws JMSException {
        getTheSession().commit();
    }

    public void rollback() throws JMSException {
        getTheSession().rollback();
    }

    public void close() throws JMSException {
        getTheSession().close();
    }

    public void recover() throws JMSException {
        getTheSession().recover();
    }

    public MessageListener getMessageListener() throws JMSException {
        return getTheSession().getMessageListener();
    }

    public void setMessageListener(MessageListener ml) throws JMSException {
        getTheSession().setMessageListener(ml);
    }

    public void run() {
        getTheSession().run();
    }

    public MessageProducer createProducer(Destination d) throws JMSException {
        return getTheSession().createProducer(d);
    }

    public MessageConsumer createConsumer(Destination d) throws JMSException {
        return getTheSession().createConsumer(d);
    }

    public MessageConsumer createConsumer(Destination d, String selector)
            throws JMSException {
        return getTheSession().createConsumer(d, selector);
    }

    public MessageConsumer createConsumer(Destination d,
                                          String selector,
                                          boolean nolocal) throws JMSException {
        return getTheSession().createConsumer(d, selector, nolocal);
    }

    public Queue createQueue(String queueName) throws JMSException {
        return getTheSession().createQueue(queueName);
    }

    public Topic createTopic(String topicName) throws JMSException {
        return getTheSession().createTopic(topicName);
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String clientId)
            throws JMSException {
        return getTheSession().createDurableSubscriber(topic, clientId);
    }

    public TopicSubscriber createDurableSubscriber(Topic topic,
                                                   String clientId,
                                                   String selector,
                                                   boolean noLocal)
            throws JMSException {
        return getTheSession().createDurableSubscriber(topic,
                                                       clientId,
                                                       selector,
                                                       noLocal);
    }

    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        return getTheSession().createBrowser(queue);
    }

    public QueueBrowser createBrowser(Queue queue, String selector)
            throws JMSException {
        return getTheSession().createBrowser(queue, selector);
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException {
        return getTheSession().createTemporaryQueue();
    }

    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return getTheSession().createTemporaryTopic();
    }

    public void unsubscribe(String clientId) throws JMSException {
        getTheSession().unsubscribe(clientId);
    }

    public void startCommandRunner(String connectionName) {
        String threadName = strings().getString(HJBStrings.RUNNER_FOR,
                                                connectionName,
                                                new SessionDescription(this,
                                                                       sessionIndex).toString());
        Thread runnerThread = new Thread(commandRunner, threadName);
        runnerThread.setDaemon(true);
        runnerThread.start();
    }

    public void stopCommandRunner() {
        commandRunner.terminate();
    }

    public HJBSessionProducers getProducers() {
        return producers;
    }

    public MessageProducer getProducer(int producerIndex) {
        try {
            return getProducers().getProducer(producerIndex);
        } catch (IndexOutOfBoundsException e) {
            handleFailure("" + producerIndex, HJBStrings.PRODUCER_NOT_FOUND, e);
            return null;
        }
    }

    public HJBSessionConsumers getConsumers() {
        return consumers;
    }

    public HJBSessionDurableSubscribers getSubscribers() {
        return durableSubscribers;
    }

    public HJBSessionQueueBrowsers getBrowsers() {
        return browsers;
    }

    public boolean equals(Object o) {
        return getTheSession().equals(o);
    }

    public int hashCode() {
        return getTheSession().hashCode();
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public int getSessionIndex() {
        return sessionIndex;
    }

    public JMSCommandRunner getCommandRunner() {
        return commandRunner;
    }

    protected Session getTheSession() {
        return theSession;
    }

    protected Clock getClock() {
        return clock;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected void handleFailure(int index, MessageFormat formatter, Exception e) {
        logAndThrowFailure(formatter.format(new Object[] {
            new Integer(index)
        }), e);
    }

    protected void handleFailure(int index, String messageKey, Exception e) {
        logAndThrowFailure(strings().getString(messageKey, new Integer(index)),
                           e);
    }

    protected void handleFailure(String itemDescription,
                                 String messageKey,
                                 Exception e) {
        logAndThrowFailure(strings().getString(messageKey,
                                               getDescription(),
                                               itemDescription), e);
    }

    public BaseJMSObjectDescription getDescription() {
        return new SessionDescription(getTheSession(), getSessionIndex());
    }

    protected void logAndThrowFailure(String message, Exception e) {
        LOG.error(message, e);
        throw new HJBException(message, e);
    }

    private final JMSCommandRunner commandRunner;
    private final HJBSessionProducers producers;
    private final HJBSessionConsumers consumers;
    private final HJBSessionDurableSubscribers durableSubscribers;
    private final HJBSessionQueueBrowsers browsers;
    private final Session theSession;
    private final Clock clock;
    private final int sessionIndex;
    private final Date creationTime;
    private static final Logger LOG = Logger.getLogger(HJBSession.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
