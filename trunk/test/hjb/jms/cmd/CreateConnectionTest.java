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

import hjb.jms.HJBConnectionFactory;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.MockHJBRuntime;

import java.io.File;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class CreateConnectionTest extends MockObjectTestCase {

    public void testCreateConnectionThrowsOnNullConnectionFactory() {
        try {
            new CreateConnection(null, "hello", "world", null);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteReportsAFaultOnPossibleExceptions() throws Exception {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("thrown as a test"),
                new RuntimeException("fire in the server room"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            HJBRoot root = new HJBRoot(testRootPath);
            Mock mockFactory = mock(ConnectionFactory.class);
            mockHJB.make1Factory(root,
                                 (ConnectionFactory) mockFactory.proxy(),
                                 "testProvider",
                                 "testFactory");
            HJBConnectionFactory testFactory = root.getProvider("testProvider")
                .getConnectionFactory("testFactory");
            mockFactory.expects(once())
                .method("createConnection")
                .will(throwException(possibleExceptions[i]));

            assertEquals(0, testFactory.getActiveConnections().size());
            CreateConnection command = new CreateConnection(testFactory,
                                                            "hello",
                                                            "world",
                                                            null);
            command.execute();
            assertEquals(0, testFactory.getActiveConnections().size());
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

    public void testExecuteCreatesANewConnection() {
        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Factory(root, "testProvider", "testFactory");
        HJBConnectionFactory testFactory = root.getProvider("testProvider")
            .getConnectionFactory("testFactory");

        assertEquals(0, testFactory.getActiveConnections().size());
        CreateConnection command = new CreateConnection(testFactory,
                                                        "hello",
                                                        "world",
                                                        null);
        command.execute();
        assertEquals(1, testFactory.getActiveConnections().size());
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testExecuteDoesNotUseTheSuppliedClientIdIfTheProviderSuppliesOne() {
        HJBRoot root = new HJBRoot(testRootPath);

        Mock connectionMock = mock(Connection.class);
        Connection testConnection = (Connection) connectionMock.proxy();
        connectionMock.stubs().method("setExceptionListener");
        connectionMock.expects(once())
            .method("getClientID")
            .will(returnValue("testProviderClientId"));
        connectionMock.expects(never()).method("setClientID");

        mockHJB.make1Factory(root,
                             testConnection,
                             "testProvider",
                             "testFactory");
        HJBConnectionFactory testFactory = root.getProvider("testProvider")
            .getConnectionFactory("testFactory");

        assertEquals(0, testFactory.getActiveConnections().size());
        CreateConnection command = new CreateConnection(testFactory,
                                                        "hello",
                                                        "world",
                                                        "newClientId");
        command.execute();
        assertEquals(1, testFactory.getActiveConnections().size());
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testExecuteUsesTheSuppliedClientIdIfTheProviderDoesNotSupplyOne() {
        HJBRoot root = new HJBRoot(testRootPath);

        Mock connectionMock = mock(Connection.class);
        Connection testConnection = (Connection) connectionMock.proxy();
        connectionMock.stubs().method("setExceptionListener");
        connectionMock.expects(once())
            .method("getClientID")
            .will(returnValue(null));
        connectionMock.expects(once())
            .method("setClientID")
            .with(eq("newClientId"));

        mockHJB.make1Factory(root,
                             testConnection,
                             "testProvider",
                             "testFactory");
        HJBConnectionFactory testFactory = root.getProvider("testProvider")
            .getConnectionFactory("testFactory");

        assertEquals(0, testFactory.getActiveConnections().size());
        CreateConnection command = new CreateConnection(testFactory,
                                                        "hello",
                                                        "world",
                                                        "newClientId");
        command.execute();
        assertEquals(1, testFactory.getActiveConnections().size());
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    protected File testRootPath;
    protected MockHJBRuntime mockHJB;
}
