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

import org.jmock.Mock;

import hjb.jms.HJBConnection;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockHJBRuntime;
import hjb.testsupport.MockSessionBuilder;

public class DeleteSessionTest extends BaseHJBTestCase {

    public void testDeleteSessionThrowsOnNullInputs() {
        try {
            new DeleteSession(null, 1);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteDeletesASession() {
        HJBRoot root = new HJBRoot(testRootPath, defaultTestClock());
        Mock mockSession = new MockSessionBuilder().createMockSession();
        registerToVerify(mockSession);
        mockSession.expects(once()).method("close");
        Session testSession = (Session) mockSession.proxy();
        mockHJB.make1Session(root, testSession, "testProvider", "testFactory");
        HJBConnection testConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);

        assertEquals(1, testConnection.getActiveSessions().size());
        DeleteSession command = new DeleteSession(testConnection, 0);
        command.execute();
        assertEquals(0, testConnection.getActiveSessions().size());
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
            HJBRoot root = new HJBRoot(testRootPath, defaultTestClock());
            Mock mockSession = new MockSessionBuilder().createMockSession();
            mockSession.expects(once())
                .method("close")
                .will(throwException(possibleExceptions[i]));
            mockHJB.make1Session(root,
                                 (Session) mockSession.proxy(),
                                 "testProvider",
                                 "testFactory");
            HJBConnection testConnection = root.getProvider("testProvider")
                .getConnectionFactory("testFactory")
                .getConnection(0);

            assertEquals(1, testConnection.getActiveSessions().size());
            DeleteSession command = new DeleteSession(testConnection, 0);
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
