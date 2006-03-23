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

import hjb.jms.HJBConnectionFactory;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.MockHJBRuntime;

public class DeleteConnectionTest extends MockObjectTestCase {

    public void testDeleteConnectionThrowsOnNullConnectionFactory() {
        try {
            new DeleteConnection(null, 1);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteDeletesAConnection() {
        HJBRoot root = new HJBRoot(testRootPath);
        Mock connectionMock = mock(Connection.class);
        Connection testConnection = (Connection) connectionMock.proxy();
        connectionMock.expects(once()).method("stop");
        connectionMock.expects(once()).method("close");
        connectionMock.stubs().method("setExceptionListener");

        mockHJB.make1Connection(root,
                                testConnection,
                                "testProvider",
                                "testFactory");
        HJBConnectionFactory testFactory = root.getProvider("testProvider").getConnectionFactory("testFactory");

        assertEquals(1, testFactory.getActiveConnections().size());
        DeleteConnection command = new DeleteConnection(testFactory, 0);
        command.execute();
        assertTrue(command.isExecutedOK());
        assertEquals(0, testFactory.getActiveConnections().size());
        assertTrue(command.isComplete());
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
