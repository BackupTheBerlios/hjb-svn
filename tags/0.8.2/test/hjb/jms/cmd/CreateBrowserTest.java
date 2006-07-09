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
import javax.jms.Queue;
import javax.jms.Session;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.HJBConnection;
import hjb.jms.HJBRoot;
import hjb.jms.HJBSessionQueueBrowsers;
import hjb.misc.HJBException;
import hjb.testsupport.MockHJBRuntime;

public class CreateBrowserTest extends MockObjectTestCase {

    public void testCreateBrowserThrowsOnNullBrowsersOrQueue() {
        Mock mockQueue = mock(Queue.class);
        Queue testQueue = (Queue) mockQueue.proxy();
        try {
            new CreateBrowser(null, 1, testQueue, "testSelector");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Connection(root, "testProvider", "testFactory");
        HJBConnection testConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);
        HJBSessionQueueBrowsers sessionBrowsers = new HJBSessionQueueBrowsers(testConnection);
        try {
            new CreateBrowser(sessionBrowsers, 1, null, "testSelector");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteCreatesANewBrowser() {
        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Session(root, "testProvider", "testFactory");
        HJBConnection testConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);
        HJBSessionQueueBrowsers sessionBrowsers = testConnection.getSessionBrowsers();
        Mock mockQueue = mock(Queue.class);
        Queue testQueue = (Queue) mockQueue.proxy();

        assertEquals(0, sessionBrowsers.getBrowsers(0).length);
        CreateBrowser command = new CreateBrowser(sessionBrowsers,
                                                  0,
                                                  testQueue,
                                                  "*");
        command.execute();
        assertEquals(1, sessionBrowsers.getBrowsers(0).length);
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
            Mock mockSession = mock(Session.class);
            mockHJB.make1Session(root,
                                 (Session) mockSession.proxy(),
                                 "testProvider",
                                 "testFactory");
            HJBConnection testConnection = root.getProvider("testProvider")
                .getConnectionFactory("testFactory")
                .getConnection(0);
            HJBSessionQueueBrowsers sessionBrowsers = testConnection.getSessionBrowsers();
            Mock mockQueue = mock(Queue.class);
            mockSession.expects(once())
                .method("createBrowser")
                .will(throwException(possibleExceptions[i]));
            assertEquals(0, sessionBrowsers.getBrowsers(0).length);
            CreateBrowser command = new CreateBrowser(sessionBrowsers,
                                                      0,
                                                      ((Queue) mockQueue.proxy()),
                                                      "*");
            command.execute();
            assertEquals(0, sessionBrowsers.getBrowsers(0).length);
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
