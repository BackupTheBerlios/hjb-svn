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

import hjb.jms.HJBConnection;
import hjb.jms.HJBMessenger;
import hjb.jms.HJBRoot;
import hjb.jms.HJBSessionProducers;
import hjb.misc.HJBException;
import hjb.msg.HJBMessage;
import hjb.msg.MessageCopierFactory;
import hjb.testsupport.MessageAttributeInvoker;
import hjb.testsupport.MockHJBRuntime;
import hjb.testsupport.MockSessionBuilder;

public class SendHJBMessageTest extends MockObjectTestCase {

    public void testSendHJBMessageThrowsOnNullInputs() {
        try {
            new SendHJBMessage(null,
                               new HJBMessage(new HashMap(), ""),
                               1,
                               null,
                               null);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    protected HJBMessage createTestTextHJBMessage() {
        HashMap headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE,
                    TextMessage.class.getName());
        return new HJBMessage(headers, "boo!");
    }

    public void testExecuteSendsAMessage() {
        Mock mockJMSMessage = mock(TextMessage.class);
        mockJMSMessage.expects(once()).method("setText").with(eq("boo!"));
        mockJMSMessage.expects(once())
            .method("setStringProperty")
            .with(eq("hjb_message_version"), eq("1.0"));
        MessageAttributeInvoker attributeInvoker = new MessageAttributeInvoker();
        attributeInvoker.invokesAllAccessors(mockJMSMessage);
        attributeInvoker.invokesGetPropertyNamesReturnsNothing(mockJMSMessage);

        Message testMessage = (TextMessage) mockJMSMessage.proxy();

        Mock mockProducer = mock(MessageProducer.class);
        mockProducer.stubs()
            .method("getTimeToLive")
            .will(returnValue(Message.DEFAULT_TIME_TO_LIVE));
        mockProducer.stubs()
            .method("getPriority")
            .will(returnValue(Message.DEFAULT_PRIORITY));
        mockProducer.stubs()
            .method("getDeliveryMode")
            .will(returnValue(Message.DEFAULT_DELIVERY_MODE));
        mockProducer.stubs().method("getDestination").will(returnValue(null));

        mockProducer.expects(once()).method("send");
        mockProducer.expects(atLeastOnce())
            .method("setDisableMessageTimestamp");
        mockProducer.expects(atLeastOnce()).method("setDisableMessageID");
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        Mock mockSession = new MockSessionBuilder().createMockSession();
        registerToVerify(mockSession);
        mockSession.stubs()
            .method("createProducer")
            .will(returnValue(testProducer));
        mockSession.expects(once())
            .method("createTextMessage")
            .will(returnValue(testMessage));
        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Session(root,
                             (Session) mockSession.proxy(),
                             "testProvider",
                             "testFactory");
        HJBConnection testConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);

        create1Producer(testConnection);
        SendHJBMessage command = new SendHJBMessage(new HJBMessenger(testConnection,
                                                                     0),
                                                    createTestTextHJBMessage(),
                                                    0,
                                                    new MockSessionBuilder().defaultProducerArguments(),
                                                    null);
        command.execute();
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    protected void create1Producer(HJBConnection testConnection) {
        HJBSessionProducers sessionProducers = testConnection.getSessionProducers();
        Mock mockDestination = mock(Destination.class);
        Destination testDestination = (Destination) mockDestination.proxy();
        CreateProducer command = new CreateProducer(sessionProducers,
                                                    0,
                                                    testDestination,
                                                    new MockSessionBuilder().defaultProducerArguments());
        command.execute();
    }

    public void testExecuteReportsAFaultOnPossibleExceptions() throws Exception {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("thrown as a test"),
                new RuntimeException("fire in the server room"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Mock mockJMSMessage = mock(TextMessage.class);
            mockJMSMessage.expects(once()).method("setText").with(eq("boo!"));
            mockJMSMessage.expects(once())
                .method("setStringProperty")
                .with(eq("hjb_message_version"), eq("1.0"));
            Message testMessage = (TextMessage) mockJMSMessage.proxy();

            Mock mockProducer = mock(MessageProducer.class);
            mockProducer.expects(once())
                .method("send")
                .will(throwException(possibleExceptions[i]));
            mockProducer.expects(atLeastOnce())
                .method("setDisableMessageTimestamp");
            mockProducer.expects(atLeastOnce()).method("setDisableMessageID");
            mockProducer.stubs()
                .method("getTimeToLive")
                .will(returnValue(Message.DEFAULT_TIME_TO_LIVE));
            mockProducer.stubs()
                .method("getPriority")
                .will(returnValue(Message.DEFAULT_PRIORITY));
            mockProducer.stubs()
                .method("getDeliveryMode")
                .will(returnValue(Message.DEFAULT_DELIVERY_MODE));
            MessageProducer testProducer = (MessageProducer) mockProducer.proxy();

            Mock mockSession = new MockSessionBuilder().createMockSession();
            registerToVerify(mockSession);
            mockSession.stubs()
                .method("createProducer")
                .will(returnValue(testProducer));
            mockSession.expects(once())
                .method("createTextMessage")
                .will(returnValue(testMessage));
            HJBRoot root = new HJBRoot(testRootPath);
            mockHJB.make1Session(root,
                                 (Session) mockSession.proxy(),
                                 "testProvider",
                                 "testFactory");
            HJBConnection testConnection = root.getProvider("testProvider")
                .getConnectionFactory("testFactory")
                .getConnection(0);

            create1Producer(testConnection);
            SendHJBMessage command = new SendHJBMessage(new HJBMessenger(testConnection,
                                                                         0),
                                                        createTestTextHJBMessage(),
                                                        0,
                                                        new MockSessionBuilder().defaultProducerArguments(),
                                                        null);
            command.execute();
            assertFalse(command.isExecutedOK());
            assertTrue(command.isComplete());
            assertNotNull(command.getFault());
            assertEquals(command.getStatusMessage(), command.getFault()
                .getMessage());
        }
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    protected File testRootPath;
    protected MockHJBRuntime mockHJB;
}
