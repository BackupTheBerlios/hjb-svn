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

import hjb.jms.HJBConnection;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.MockHJBRuntime;

public class StartConnectionTest extends MockObjectTestCase {

    public void testStartConnectionThrowsOnNullConnection() {
        try {
            new StartConnection(null);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteStartsAConnection() {
        HJBRoot root = new HJBRoot(testRootPath);
        Mock connectionMock = mock(Connection.class);
        Connection testConnection = (Connection) connectionMock.proxy();
        connectionMock.expects(once()).method("start");
        connectionMock.stubs().method("setExceptionListener");

        mockHJB.make1Connection(root,
                                testConnection,
                                "testProvider",
                                "testFactory");
        HJBConnection testHJBConnection = root.getProvider("testProvider").getConnectionFactory("testFactory").getConnection(0);
        StartConnection command = new StartConnection(testHJBConnection);
        command.execute();
        assertTrue(command.isExecutedOK());
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
