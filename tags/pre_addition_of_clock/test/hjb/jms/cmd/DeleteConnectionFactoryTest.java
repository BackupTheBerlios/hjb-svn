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

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.HJBProvider;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockHJBRuntime;

public class DeleteConnectionFactoryTest extends MockObjectTestCase {

    public void testDeleteConnectionFactoryThrowsOnNullInputs() {
        try {
            new DeleteConnectionFactory(null, "testName");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Factory(root, "testProvider", "testFactory");

        try {
            new DeleteConnectionFactory(root.getProvider("testProvider"), null);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteDeletesAConnectionFactoryAndChildConnection() {
        HJBRoot root = new HJBRoot(testRootPath);
        Mock mockConnection = new MockConnectionBuilder().createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("stop");
        mockConnection.expects(once()).method("close");

        mockHJB.make1Connection(root,
                                (Connection) mockConnection.proxy(),
                                "testProvider",
                                "testFactory");

        HJBProvider testProvider = root.getProvider("testProvider");
        assertEquals(1, testProvider.getConnectionFactories().size());
        DeleteConnectionFactory command = new DeleteConnectionFactory(testProvider,
                                                                      "testFactory");
        command.execute();
        assertTrue(command.isExecutedOK());
        assertEquals(0, root.getProvider("testProvider")
            .getConnectionFactories()
            .size());
        assertTrue(command.isComplete());
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}

    }

    public void testExecuteDeletesAConnectionFactory() {
        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Factory(root, "testProvider", "testFactory");

        HJBProvider testProvider = root.getProvider("testProvider");
        assertEquals(1, testProvider.getConnectionFactories().size());
        DeleteConnectionFactory command = new DeleteConnectionFactory(testProvider,
                                                                      "testFactory");
        command.execute();
        assertTrue(command.isExecutedOK());
        assertEquals(0, root.getProvider("testProvider")
            .getConnectionFactories()
            .size());
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
            new RuntimeException("fire in the server room"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            HJBRoot root = new HJBRoot(testRootPath);
            Mock mockConnection = new MockConnectionBuilder().createMockConnection();
            registerToVerify(mockConnection);
            mockConnection.expects(once())
                .method("stop")
                .will(throwException(possibleExceptions[i]));
            mockHJB.make1Connection(root,
                                    (Connection) mockConnection.proxy(),
                                    "testProvider",
                                    "testFactory");

            HJBProvider testProvider = root.getProvider("testProvider");
            assertEquals(1, testProvider.getConnectionFactories().size());
            DeleteConnectionFactory command = new DeleteConnectionFactory(testProvider,
                                                                          "testFactory");
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
