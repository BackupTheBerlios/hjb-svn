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
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockSessionBuilder;

public class HJBSessionDurableSubscribersTest extends MockObjectTestCase {

    public HJBSessionDurableSubscribersTest() {

    }

    public void testHJBSessionDurableSubscribersThrowsIllegalArgumentExceptionOnNullConnection() {
        try {
            new HJBSessionDurableSubscribers(null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testCreateSubscriberThrowsIfSessionIsNotThere()
            throws Exception {
        mockSession.stubs().method("createDurableSubscriber");
        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testConnection);
        try {
            subscribers.createDurableSubscriber(0, testTopic, "test");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            subscribers.createDurableSubscriber(1, testTopic, "test-selector");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        try {
            subscribers.createDurableSubscriber(1, testTopic, "test-selector");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        assertEquals("Index should be 0",
                     0,
                     subscribers.createDurableSubscriber(0,
                                                         testTopic,
                                                         "test-selector"));
        assertEquals("Index should be 1",
                     1,
                     subscribers.createDurableSubscriber(0,
                                                         testTopic,
                                                         "test-selector"));
        assertEquals("Index should be 2",
                     2,
                     subscribers.createDurableSubscriber(0,
                                                         testTopic,
                                                         "test-selector",
                                                         "test",
                                                         false));
    }

    public void testCreateSubscriberThrowsHJBExceptionOnJMSException()
            throws Exception {
        mockSession = sessionBuilder.createMockSessionThatThrowsJMSOn("createDurableSubscriber");
        Session aSession = (Session) mockSession.proxy();
        updateConnectionMock(aSession);

        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            subscribers.createDurableSubscriber(0, testTopic, "test-selector");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            subscribers.createDurableSubscriber(0, testTopic, "test-selector");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            subscribers.createDurableSubscriber(0,
                                                testTopic,
                                                "test-selector",
                                                "test",
                                                false);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testCreateSubscriberIsInvokedOnStoredSession() throws Exception {
        mockSession.stubs().method("createDurableSubscriber");
        registerToVerify(mockSession);
        testSession = (Session) mockSession.proxy();
        updateConnectionMock(testSession);

        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     subscribers.createDurableSubscriber(0,
                                                         testTopic,
                                                         "test-selector",
                                                         "test",
                                                         true));
        assertEquals("there should be 1 subscribers",
                     1,
                     subscribers.getSubscribers(0).length);
        assertEquals("Index should be 1",
                     1,
                     subscribers.createDurableSubscriber(0,
                                                         testTopic,
                                                         "test-selector"));
        assertEquals("there should be 2 subscribers",
                     2,
                     subscribers.getSubscribers(0).length);
        assertEquals("Index should be 2",
                     2,
                     subscribers.createDurableSubscriber(0,
                                                         testTopic,
                                                         "test-selector",
                                                         "test",
                                                         true));
        assertEquals("there should be 3 subscribers",
                     3,
                     subscribers.getSubscribers(0).length);
    }

    public void testGetSubscribersThrowsIfSessionIsNotThere() throws Exception {
        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testConnection);
        try {
            subscribers.getSubscribers(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetSubscribersReturnsEmptyForNewEmptySessions()
            throws Exception {
        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals(0, subscribers.getSubscribers(0).length);
    }

    public void testGetSubscriberThrowsIfSessionIsNotThere() throws Exception {
        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testConnection);
        try {
            subscribers.getSubscriber(0, 0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetSubscriberThrowsForNewEmptySessions() throws Exception {
        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            subscribers.getSubscriber(0, 0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetSubscriberThrowsForInvalidSubscriber() throws Exception {
        Mock mockTopic = new Mock(TopicSubscriber.class);
        TopicSubscriber testTopicSubscriber = (TopicSubscriber) mockTopic.proxy();
        mockSession.stubs()
            .method("createDurableSubscriber")
            .will(returnValue(testTopicSubscriber));

        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        subscribers.createDurableSubscriber(0, testTopic, "test");
        try {
            subscribers.getSubscriber(0, 1);
            fail("should have thrown an exception - subscriber 1 does not exist yet");
        } catch (HJBException hjbe) {}
    }

    public void testGetSubscriberReturnsCreatedSubscribers() throws Exception {
        Mock mockTopic = new Mock(TopicSubscriber.class);
        TopicSubscriber testTopicSubscriber = (TopicSubscriber) mockTopic.proxy();
        mockSession.stubs()
            .method("createDurableSubscriber")
            .will(returnValue(testTopicSubscriber));

        HJBSessionDurableSubscribers subscribers = new HJBSessionDurableSubscribers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     subscribers.createDurableSubscriber(0,
                                                         testTopic,
                                                         "test",
                                                         "test",
                                                         true));
        assertNotNull("got first subscriber", subscribers.getSubscriber(0, 0));
        assertEquals("Index should be 1",
                     1,
                     subscribers.createDurableSubscriber(0, testTopic, "test"));
        assertNotNull("got second subscriber", subscribers.getSubscriber(0, 1));
    }

    protected void setUp() throws Exception {
        super.setUp();
        initialiseMockBuilders();

        mockSession = new Mock(Session.class);
        testSession = (Session) mockSession.proxy();

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
        mockConnection.stubs().method("setExceptionListener");
        Connection aConnection = (Connection) mockConnection.proxy();
        testConnection = new HJBConnection(aConnection, 0);
    }

    private Mock mockConnection;
    private HJBConnection testConnection;
    private Mock mockSession;
    private Session testSession;
    private Mock mockTopic;
    private Topic testTopic;

    private MockConnectionBuilder connectionBuilder;
    private MockSessionBuilder sessionBuilder;

}
