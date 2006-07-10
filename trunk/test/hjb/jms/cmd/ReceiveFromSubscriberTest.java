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
package hjb.jms.cmd;

import java.io.File;
import java.util.HashMap;

import javax.jms.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.http.cmd.HJBMessageWriter;
import hjb.jms.HJBConnection;
import hjb.jms.HJBMessenger;
import hjb.jms.HJBRoot;
import hjb.jms.HJBSessionDurableSubscribers;
import hjb.misc.HJBException;
import hjb.msg.HJBMessage;
import hjb.msg.MessageCopierFactory;
import hjb.testsupport.MessageAttributeInvoker;
import hjb.testsupport.MockHJBRuntime;

public class ReceiveFromSubscriberTest extends MockObjectTestCase {

    public void testCommitSessionThrowsOnNullInputs() {
        try {
            new ReceiveFromSubscriber(null, 1);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    protected HJBMessage createTestTextHJBMessage() {
        HashMap headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE,
                    TextMessage.class.getName());
        return new HJBMessage(headers, "boo!");
    }

    public void testExecuteReceivesAMessage() {
        Mock mockTextMessage = mock(TextMessage.class);
        mockTextMessage.expects(once())
            .method("getText")
            .will(returnValue("boo!"));
        mockTextMessage.expects(once()).method("acknowledge");
        MessageAttributeInvoker attributeInvoker = new MessageAttributeInvoker();
        attributeInvoker.invokesAllAccessors(mockTextMessage);
        attributeInvoker.invokesGetPropertyNamesReturnsNothing(mockTextMessage);

        Message testMessage = (TextMessage) mockTextMessage.proxy();

        Mock mockSubscriber = mock(TopicSubscriber.class);
        mockSubscriber.stubs().method("getTopic").will(returnValue(null));
        mockSubscriber.stubs().method("getNoLocal").will(returnValue(false));
        mockSubscriber.expects(once())
            .method("receive")
            .will(returnValue(testMessage));
        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();

        Mock mockSession = mock(Session.class);
        mockSession.stubs().method("getTransacted").will(returnValue(false));
        mockSession.stubs()
            .method("createDurableSubscriber")
            .will(returnValue(testSubscriber));
        Session testSession = (Session) mockSession.proxy();

        Mock mockTopic = mock(Topic.class);
        Topic testTopic = (Topic) mockTopic.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1SessionAnd1Destination(root,
                                            testSession,
                                            "testProvider",
                                            "testFactory",
                                            "testDestination",
                                            testTopic);
        HJBConnection testConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);

        create1Subscriber(testConnection);
        ReceiveFromSubscriber command = new ReceiveFromSubscriber(new HJBMessenger(testConnection,
                                                                                   0),
                                                                  0);
        command.execute();
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
        assertNotNull(command.getMessageReceived());
        System.err.println(new HJBMessageWriter().asText(command.getMessageReceived()));
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testExecuteReportsAFaultOnPossibleExceptions() throws Exception {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("thrown as a test"),
                new RuntimeException("fire in the server room"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Mock mockSubscriber = mock(TopicSubscriber.class);
            mockSubscriber.expects(once())
                .method("receive")
                .will(throwException(possibleExceptions[i]));
            TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();

            Mock mockSession = mock(Session.class);
            mockSession.stubs().method("getTransacted").will(returnValue(false));
            mockSession.stubs()
                .method("createDurableSubscriber")
                .will(returnValue(testSubscriber));
            Session testSession = (Session) mockSession.proxy();

            Mock mockTopic = mock(Topic.class);
            Topic testTopic = (Topic) mockTopic.proxy();

            HJBRoot root = new HJBRoot(testRootPath);
            mockHJB.make1SessionAnd1Destination(root,
                                                testSession,
                                                "testProvider",
                                                "testFactory",
                                                "testDestination",
                                                testTopic);
            HJBConnection testConnection = root.getProvider("testProvider")
                .getConnectionFactory("testFactory")
                .getConnection(0);

            create1Subscriber(testConnection);
            ReceiveFromSubscriber command = new ReceiveFromSubscriber(new HJBMessenger(testConnection,
                                                                                       0),
                                                                      0);
            command.execute();
            assertFalse(command.isExecutedOK());
            assertTrue(command.isComplete());
            assertNotNull(command.getFault());
            assertEquals(command.getStatusMessage(), command.getFault()
                .getMessage());
        }
    }

    protected void create1Subscriber(HJBConnection testConnection) {
        HJBSessionDurableSubscribers sessionSubscribers = testConnection.getSessionSubscribers();
        Mock mockTopic = mock(Topic.class);
        Topic testTopic = (Topic) mockTopic.proxy();
        CreateSubscriber command = new CreateSubscriber(sessionSubscribers,
                                                        0,
                                                        testTopic,
                                                        "testName");
        command.execute();
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    protected File testRootPath;
    protected MockHJBRuntime mockHJB;
}
