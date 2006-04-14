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

import junit.framework.TestCase;

import hjb.jms.HJBConnectionFactory;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.MockHJBRuntime;

public class CreateConnectionTest extends TestCase {

    public void testCreateConnectionThrowsOnNullConnectionFactory() {
        try {
            new CreateConnection(null, "hello", "world");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteCreatesANewConnection() {
        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Factory(root, "testProvider", "testFactory");
        HJBConnectionFactory testFactory = root.getProvider("testProvider")
            .getConnectionFactory("testFactory");

        assertEquals(0, testFactory.getActiveConnections().size());
        CreateConnection command = new CreateConnection(testFactory,
                                                        "hello",
                                                        "world");
        command.execute();
        assertEquals(1, testFactory.getActiveConnections().size());
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
