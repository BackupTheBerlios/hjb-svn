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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.jms.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.http.cmd.HJBMessageWriter;
import hjb.jms.HJBConnection;
import hjb.jms.HJBMessenger;
import hjb.jms.HJBRoot;
import hjb.jms.HJBSessionQueueBrowsers;
import hjb.misc.HJBException;
import hjb.msg.HJBMessage;
import hjb.msg.MessageCopierFactory;
import hjb.testsupport.MessageAttributeInvoker;
import hjb.testsupport.MockHJBRuntime;
import hjb.testsupport.MockSessionBuilder;

public class ViewQueueTest extends MockObjectTestCase {

    public void testViewQueueThrowsOnNullInputs() {
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
        MessageAttributeInvoker attributeInvoker = new MessageAttributeInvoker();
        attributeInvoker.invokesAllAccessors(mockTextMessage);
        attributeInvoker.invokesGetPropertyNamesReturnsNothing(mockTextMessage);

        Message testMessage = (TextMessage) mockTextMessage.proxy();

        List testMessages = new ArrayList();
        testMessages.add(testMessage);
        Mock mockBrowser = mock(QueueBrowser.class);
        mockBrowser.stubs().method("getQueue").will(returnValue(null));
        mockBrowser.expects(once())
            .method("getEnumeration")
            .will(returnValue(Collections.enumeration(testMessages)));
        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();

        Mock mockSession = new MockSessionBuilder().createMockSession();
        registerToVerify(mockSession);
        mockSession.stubs()
            .method("createBrowser")
            .will(returnValue(testBrowser));
        Mock mockQueue = mock(Queue.class);
        Queue testQueue = (Queue) mockQueue.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1SessionAnd1Destination(root,
                                            (Session) mockSession.proxy(),
                                            "testProvider",
                                            "testFactory",
                                            "testDestination",
                                            testQueue);
        HJBConnection testConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);

        create1Browser(testConnection);
        ViewQueue command = new ViewQueue(new HJBMessenger(testConnection, 0),
                                          0);
        command.execute();
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
        assertNotNull(command.getMessagesOnQueue());
        System.err.println(new HJBMessageWriter().asText(command.getMessagesOnQueue()));
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
            Mock mockBrowser = mock(QueueBrowser.class);
            mockBrowser.stubs().method("getQueue").will(returnValue(null));
            mockBrowser.expects(once())
                .method("getEnumeration")
                .will(throwException(possibleExceptions[i]));
            QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();

            Mock mockSession = new MockSessionBuilder().createMockSession();
            registerToVerify(mockSession);
            mockSession.stubs()
                .method("createBrowser")
                .will(returnValue(testBrowser));
            Mock mockQueue = mock(Queue.class);
            Queue testQueue = (Queue) mockQueue.proxy();

            HJBRoot root = new HJBRoot(testRootPath);
            mockHJB.make1SessionAnd1Destination(root,
                                                (Session) mockSession.proxy(),
                                                "testProvider",
                                                "testFactory",
                                                "testDestination",
                                                testQueue);
            HJBConnection testConnection = root.getProvider("testProvider")
                .getConnectionFactory("testFactory")
                .getConnection(0);

            create1Browser(testConnection);
            ViewQueue command = new ViewQueue(new HJBMessenger(testConnection,
                                                               0), 0);
            command.execute();
            assertFalse(command.isExecutedOK());
            assertTrue(command.isComplete());
            assertNotNull(command.getFault());
            assertEquals(command.getStatusMessage(), command.getFault()
                .getMessage());
        }
    }

    protected void create1Browser(HJBConnection testConnection) {
        HJBSessionQueueBrowsers sessionBrowsers = testConnection.getSessionBrowsers();
        Mock mockQueue = mock(Queue.class);
        Queue testQueue = (Queue) mockQueue.proxy();
        CreateBrowser command = new CreateBrowser(sessionBrowsers,
                                                  0,
                                                  testQueue,
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
