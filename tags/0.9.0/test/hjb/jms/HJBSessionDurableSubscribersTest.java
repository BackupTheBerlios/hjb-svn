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

import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.jmock.Mock;

import hjb.misc.HJBException;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockSessionBuilder;

public class HJBSessionDurableSubscribersTest extends BaseHJBTestCase {

    public void testConstructionsThrowsIllegalArgumentExceptionOnNullSession() {
        try {
            new HJBSessionDurableSubscribers(null, defaultTestClock());
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructionsThrowsIllegalArgumentExceptionOnNullClock() {
        try {
            HJBSession aSession = new HJBSession((Session) mockSession.proxy(),
                                                 0,
                                                 defaultTestClock());
            new HJBSessionDurableSubscribers(aSession, null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testCreateSubscriberThrowsHJBExceptionOnJMSException()
            throws Exception {
        mockSession = sessionBuilder.createMockSessionThatThrowsJMSOn("createDurableSubscriber");
        HJBSession aSession = new HJBSession((Session) mockSession.proxy(),
                                             0,
                                             defaultTestClock());
        updateConnectionMock(aSession);

        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(aSession,
                                                                                    defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            subscribers.createDurableSubscriber(testTopic, "test-selector");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            subscribers.createDurableSubscriber(testTopic, "test-selector");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            subscribers.createDurableSubscriber(testTopic,
                                                "test-selector",
                                                "test",
                                                false);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testCreateSubscriberIsInvokedOnStoredSession() throws Exception {
        mockSession.stubs().method("createDurableSubscriber");
        registerToVerify(mockSession);
        testSession = new HJBSession((Session) mockSession.proxy(),
                                     0,
                                     defaultTestClock());
        updateConnectionMock(testSession);

        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testSession,
                                                                                    defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     subscribers.createDurableSubscriber(testTopic,
                                                         "test-selector",
                                                         "test",
                                                         true));
        assertEquals("there should be 1 subscribers",
                     1,
                     subscribers.asArray().length);
        assertEquals("Index should be 1",
                     1,
                     subscribers.createDurableSubscriber(testTopic,
                                                         "test-selector"));
        assertEquals("there should be 2 subscribers",
                     2,
                     subscribers.asArray().length);
        assertEquals("Index should be 2",
                     2,
                     subscribers.createDurableSubscriber(testTopic,
                                                         "test-selector",
                                                         "test",
                                                         true));
        assertEquals("there should be 3 subscribers",
                     3,
                     subscribers.asArray().length);
    }

    public void testGetSubscriberThrowsForInvalidSubscriber() throws Exception {
        Mock mockTopic = new Mock(TopicSubscriber.class);
        TopicSubscriber testTopicSubscriber = (TopicSubscriber) mockTopic.proxy();
        mockSession.stubs()
            .method("createDurableSubscriber")
            .will(returnValue(testTopicSubscriber));

        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testSession,
                                                                                    defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        subscribers.createDurableSubscriber(testTopic, "test");
        try {
            subscribers.getSubscriber(1);
            fail("should have thrown an exception - subscriber 1 does not exist yet");
        } catch (HJBException hjbe) {}
    }

    public void testGetSubscriberReturnsCreatedSubscribers() throws Exception {
        Mock mockTopic = new Mock(TopicSubscriber.class);
        TopicSubscriber testTopicSubscriber = (TopicSubscriber) mockTopic.proxy();
        mockSession.stubs()
            .method("createDurableSubscriber")
            .will(returnValue(testTopicSubscriber));

        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testSession,
                                                                                    defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     subscribers.createDurableSubscriber(testTopic,
                                                         "test",
                                                         "test",
                                                         true));
        assertNotNull("got first subscriber", subscribers.getSubscriber(0));
        assertEquals("Index should be 1",
                     1,
                     subscribers.createDurableSubscriber(testTopic, "test"));
        assertNotNull("got second subscriber", subscribers.getSubscriber(1));
    }

    public void testShouldReturnTheCorrectNumberOfItemDescriptions()
            throws Exception {
        Mock mockTopic = new Mock(TopicSubscriber.class);
        TopicSubscriber testTopicSubscriber = (TopicSubscriber) mockTopic.proxy();
        mockSession.stubs()
            .method("createDurableSubscriber")
            .will(returnValue(testTopicSubscriber));

        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testSession,
                                                                                    defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals(0, subscribers.getItemDescriptions().length);
        subscribers.createDurableSubscriber(testTopic, "test", "test", true);
        assertEquals(1, subscribers.getItemDescriptions().length);
        subscribers.createDurableSubscriber(testTopic, "test");
        assertEquals(2, subscribers.getItemDescriptions().length);
    }

    protected void setUp() throws Exception {
        super.setUp();
        initialiseMockBuilders();

        mockSession = new MockSessionBuilder().createMockSession();
        registerToVerify(mockSession);
        testSession = new HJBSession((Session) mockSession.proxy(),
                                     0,
                                     defaultTestClock());
        mockTopic = new Mock(Topic.class);
        testTopic = (Topic) mockTopic.proxy();
        updateConnectionMock(testSession);
    }

    protected void initialiseMockBuilders() {
        connectionBuilder = new MockConnectionBuilder();
        sessionBuilder = new MockSessionBuilder();
    }

    protected void updateConnectionMock(Session aSession) {
        mockConnection = connectionBuilder.createMockConnection(aSession);
        registerToVerify(mockConnection);
        Connection aConnection = (Connection) mockConnection.proxy();
        testConnection = new HJBConnection(aConnection, 0, defaultTestClock());
    }

    private Mock mockConnection;
    private HJBConnection testConnection;
    private Mock mockSession;
    private HJBSession testSession;
    private Mock mockTopic;
    private Topic testTopic;

    private MockConnectionBuilder connectionBuilder;
    private MockSessionBuilder sessionBuilder;
}
