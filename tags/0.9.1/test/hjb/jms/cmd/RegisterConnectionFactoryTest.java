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

import javax.jms.Destination;

import org.jmock.Mock;

import hjb.jms.HJBProvider;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockHJBRuntime;

public class RegisterConnectionFactoryTest extends BaseHJBTestCase {

    public void testRegisterConnectionFactoryThrowsOnNullInputs() {
        try {
            new RegisterConnectionFactory(null, "testName");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}

        Mock mockDestination = mock(Destination.class);
        Destination testDestination = (Destination) mockDestination.proxy();
        HJBRoot root = new HJBRoot(testRootPath, defaultTestClock());
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 testDestination);

        try {
            new RegisterConnectionFactory(root.getProvider("testProvider"),
                                          null);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteDeletesADestination() {
        HJBRoot root = new HJBRoot(testRootPath, defaultTestClock());
        mockHJB.make1Provider(root, "testProvider");

        HJBProvider testProvider = root.getProvider("testProvider");
        assertEquals(0, testProvider.getConnectionFactories().size());
        RegisterConnectionFactory command = new RegisterConnectionFactory(testProvider,
                                                                          "testFactory");
        command.execute();
        assertEquals(1, root.getProvider("testProvider")
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
            HJBRoot root = new HJBRoot(testRootPath, defaultTestClock());
            mockHJB.make1ProviderWithContextThatThrows(root,
                                                       "testProvider",
                                                       possibleExceptions[i]);

            HJBProvider testProvider = root.getProvider("testProvider");
            assertEquals(0, testProvider.getConnectionFactories().size());
            RegisterConnectionFactory command = new RegisterConnectionFactory(testProvider,
                                                                              "testFactory");
            command.execute();
            assertEquals(0, root.getProvider("testProvider")
                .getConnectionFactories()
                .size());
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
