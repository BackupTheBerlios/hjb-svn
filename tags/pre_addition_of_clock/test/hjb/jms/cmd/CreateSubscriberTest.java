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

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.HJBConnection;
import hjb.jms.HJBRoot;
import hjb.jms.HJBSessionDurableSubscribers;
import hjb.misc.HJBException;
import hjb.testsupport.MockHJBRuntime;
import hjb.testsupport.MockSessionBuilder;

public class CreateSubscriberTest extends MockObjectTestCase {

    public void testCreateSubscriberThrowsOnNullInputs() {
        Mock mockTopic = mock(Topic.class);
        Topic testTopic = (Topic) mockTopic.proxy();
        try {
            new CreateSubscriber(null, 1, testTopic, "testSelector");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Connection(root, "testProvider", "testFactory");
        HJBConnection testConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);
        HJBSessionDurableSubscribers sessionSubscribers = new HJBSessionDurableSubscribers(testConnection);
        try {
            new CreateSubscriber(sessionSubscribers, 1, null, "testSelector");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteCreatesANewSubscriber() {
        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Session(root, "testProvider", "testFactory");
        HJBConnection testConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);
        HJBSessionDurableSubscribers sessionSubscribers = testConnection.getSessionSubscribers();
        Mock mockTopic = mock(Topic.class);
        Topic testTopic = (Topic) mockTopic.proxy();

        assertEquals(0, sessionSubscribers.getSubscribers(0).length);
        CreateSubscriber command = new CreateSubscriber(sessionSubscribers,
                                                        0,
                                                        testTopic,
                                                        "*");
        command.execute();
        assertEquals(1, sessionSubscribers.getSubscribers(0).length);
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
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
            HJBRoot root = new HJBRoot(testRootPath);
            Mock mockSession = new MockSessionBuilder().createMockSession();
            registerToVerify(mockSession);
            mockHJB.make1Session(root,
                                 (Session) mockSession.proxy(),
                                 "testProvider",
                                 "testFactory");
            HJBConnection testConnection = root.getProvider("testProvider")
                .getConnectionFactory("testFactory")
                .getConnection(0);
            HJBSessionDurableSubscribers sessionSubscribers = testConnection.getSessionSubscribers();
            Mock mockTopic = mock(Topic.class);
            Topic testTopic = (Topic) mockTopic.proxy();
            mockSession.expects(once())
                .method("createDurableSubscriber")
                .will(throwException(possibleExceptions[i]));

            assertEquals(0, sessionSubscribers.getSubscribers(0).length);
            CreateSubscriber command = new CreateSubscriber(sessionSubscribers,
                                                            0,
                                                            testTopic,
                                                            "*");
            command.execute();
            assertEquals(0, sessionSubscribers.getSubscribers(0).length);
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
