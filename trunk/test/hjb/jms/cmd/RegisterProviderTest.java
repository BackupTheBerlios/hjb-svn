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

import hjb.jms.HJBProvider;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.MockContextBuilder;
import hjb.testsupport.MockHJBRuntime;
import hjb.testsupport.SharedMock;

import java.io.File;
import java.util.Hashtable;

import javax.naming.Context;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class RegisterProviderTest extends MockObjectTestCase {

    public void testRegisterProviderThrowsOnNullInputs() {
        try {
            new RegisterProvider(null, new Hashtable());
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}

        HJBRoot root = new HJBRoot(testRootPath);
        try {
            new RegisterProvider(root, null);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteRegistersAProvider() {
        Hashtable testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, "testProvider");
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        MockContextBuilder contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.returnsEnvironment(testEnvironment);

        SharedMock sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);

        HJBRoot root = new HJBRoot(testRootPath);

        assertEquals(0, root.getProviders().size());
        RegisterProvider command = new RegisterProvider(root, testEnvironment);
        command.execute();
        assertEquals(1, root.getProviders().size());
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
        Hashtable testEnvironment = new Hashtable();
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        MockContextBuilder contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.returnsEnvironment(testEnvironment);

        SharedMock sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);

        HJBRoot root = new HJBRoot(testRootPath);

        assertEquals(0, root.getProviders().size());
        RegisterProvider command = new RegisterProvider(root, testEnvironment);
        command.execute();
        assertEquals(0, root.getProviders().size());
        assertFalse(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNotNull(command.getFault());
        assertEquals(command.getStatusMessage(), command.getFault()
            .getMessage());
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    protected File testRootPath;
    protected MockHJBRuntime mockHJB;
}
