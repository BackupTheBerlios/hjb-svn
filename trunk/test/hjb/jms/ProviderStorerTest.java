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
package hjb.jms;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;

import org.jmock.MockObjectTestCase;

import hjb.testsupport.MockContextBuilder;
import hjb.testsupport.SharedMock;

public class ProviderStorerTest extends MockObjectTestCase {

    public ProviderStorerTest() {
        contextBuilder = new MockContextBuilder();
        sharedMock = SharedMock.getInstance();
    }

    public void testLoadAndStore() throws Exception {
        HJBProvider p = new ProviderBuilder(testEnvironment).createProvider();
        ProviderStorer ps = new ProviderStorer(p, testRootPath);
        ps.store();
        File expectedFile = ps.getStoragePath(p.getName());
        assertTrue("should have created provider file", expectedFile.exists());

        ps.load(p.getName());
        assertEquals("loaded provider was different", p, ps.getTheProvider());
        expectedFile.delete();

        File invisibleFile = new File(expectedFile.getParentFile(), "foo");
        if (invisibleFile.exists()) invisibleFile.delete();
        try {
            ps.load("foo");
            fail("managed to load invisible Provider file");
        } catch (IOException ioe) {}

        File invisiblePath = new File(testRootPath, "bar");
        if (invisiblePath.exists()) invisiblePath.delete();
        try {
            ps = new ProviderStorer(invisiblePath);
            fail("managed to create ProviderStorer when no filesystem was present");
        } catch (IllegalArgumentException iae) {}
    }

    public void testAsProperties() throws Exception {
        HJBProvider p = new ProviderBuilder(testEnvironment).createProvider();
        ProviderStorer ps = new ProviderStorer(p, testRootPath);
        Map asProperties = new HashMap(ps.asProperties());
        Map expectedProperties = new HashMap(testEnvironment);
        expectedProperties.put(HJBProvider.HJB_PROVIDER_NAME, p.getName());
        assertEquals("properties were incorrect",
                     expectedProperties,
                     asProperties);
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, "testProvider");
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        sharedMock.setCurrentMock(contextBuilder.returnsEnvironment(testEnvironment));

    }

    private Hashtable testEnvironment;
    private File testRootPath;
    private MockContextBuilder contextBuilder;
    private SharedMock sharedMock;

}