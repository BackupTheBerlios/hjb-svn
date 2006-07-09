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

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.HJBConnection;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.MockHJBRuntime;

public class UnsubscribeClientIdTest extends MockObjectTestCase {

    public void testUnsubscribeThrowsOnNullInputs() {
        try {
            new UnsubscribeClientId(null, 1, "foo");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
        try {
            Mock mockConnection = mock(Connection.class);
            mockConnection.stubs().method("setExceptionListener");
            new UnsubscribeClientId(new HJBConnection((Connection) mockConnection.proxy(),
                                                      0),
                                    1,
                                    null);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteUnsubscribesAClientId() {
        HJBRoot root = new HJBRoot(testRootPath);
        Mock mockSession = mock(Session.class);
        mockSession.expects(once())
            .method("unsubscribe")
            .with(eq("testClientId"));
        Session testSession = (Session) mockSession.proxy();
        mockHJB.make1Session(root, testSession, "testProvider", "testFactory");
        HJBConnection testConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);

        UnsubscribeClientId command = new UnsubscribeClientId(testConnection,
                                                              0,
                                                              "testClientId");
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

    public void testExecuteReportsAFaultOnPossibleExceptions() throws Exception {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("thrown as a test"),
                new RuntimeException("fire in the server room"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            HJBRoot root = new HJBRoot(testRootPath);
            Mock mockSession = mock(Session.class);
            mockSession.expects(once())
                .method("unsubscribe")
                .will(throwException(possibleExceptions[i]));
            Session testSession = (Session) mockSession.proxy();
            mockHJB.make1Session(root,
                                 testSession,
                                 "testProvider",
                                 "testFactory");
            HJBConnection testConnection = root.getProvider("testProvider")
                .getConnectionFactory("testFactory")
                .getConnection(0);

            UnsubscribeClientId command = new UnsubscribeClientId(testConnection,
                                                                  0,
                                                                  "testClientId");
            command.execute();
            assertFalse(command.isExecutedOK());
            assertTrue(command.isComplete());
            assertNotNull(command.getFault());
            assertEquals(command.getStatusMessage(), command.getFault()
                .getMessage());
            try {
                command.execute();
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    protected File testRootPath;
    protected MockHJBRuntime mockHJB;
}
