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
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.testsupport.MockContextBuilder;
import hjb.testsupport.SharedMock;

public class HJBRootTest extends MockObjectTestCase {

    public HJBRootTest() {
        contextBuilder = new MockContextBuilder();
        sharedMock = SharedMock.getInstance();
    }

    public void testCommandRunnerIsActiveOnConstruction() {
        HJBRoot br = new HJBRoot(testRootPath);
        assertNotNull("should be initialised", br.getCommandRunner());
        assertFalse("should not be terminated", br.getCommandRunner()
            .isTerminated());
    }

    public void testDeletingNonExistentProviderDoesNotThrow() {
        HJBRoot br = new HJBRoot(testRootPath);
        br.deleteProvider("notPresent");
    }

    public void testDeleteProvider() throws Exception {
        HJBRoot br = new HJBRoot(testRootPath);

        br.addProvider(testEnvironment);
        Map providers = br.getProviders();
        assertEquals("wrong number of providers", 1, providers.size());

        br.deleteProvider("testProvider");
        providers = br.getProviders();
        assertEquals("provider not removed", 0, providers.size());
    }

    public void testRegisterProvider() throws Exception {
        HJBRoot br = new HJBRoot(testRootPath);

        br.addProvider(testEnvironment);
        Map providers = br.getProviders();
        assertEquals("wrong number of providers", 1, providers.size());
        br.addProvider(testEnvironment);
        providers = br.getProviders();
        assertEquals("wrong number of providers", 1, providers.size());

        br.addProvider(anotherEnvironment);
        providers = br.getProviders();
        assertEquals("wrong number of providers", 2, providers.size());

        Hashtable copy = new Hashtable(testEnvironment);
        copy.put("extra extra", "dummy");
        try {
            br.addProvider(copy);
            fail("allowed two Providers with the same name");
        } catch (HJBException hjbe) {}

        copy = new Hashtable(testEnvironment);
        copy.put(HJBProvider.HJB_PROVIDER_NAME, "changedName");
        try {
            br.addProvider(copy);
            fail("allowed two Providers with the different names but same environment");
        } catch (HJBException hjbe) {}
    }

    public void testGetProvider() throws Exception {
        HJBRoot br = new HJBRoot(testRootPath);
        assertNull("should not be found", br.getProvider("testProvider"));
        br.addProvider(testEnvironment);
        assertNotNull("should be found", br.getProvider("testProvider"));
    }

    public void testCheckValidStorageRoot() throws Exception {
        File doesNotExist = new File(testRootPath, "foo");
        if (doesNotExist.exists()) doesNotExist.delete();
        File doesExist = new File(testRootPath, "bar");
        if (doesExist.exists()) doesExist.delete();
        doesExist.mkdirs();

        HJBRoot br = new HJBRoot(doesExist);
        assertNotNull("was not created", br);

        try {
            br = new HJBRoot(doesNotExist);
            assertNotNull("was not created", br);
            fail("created successfully with invalid storage path");
        } catch (HJBException hje) {}
    }

    public void testProviders() throws Exception {
        HJBRoot br = new HJBRoot(testRootPath);
        br.addProvider(testEnvironment);
        Map providers = br.getProviders();
        providers.clear();
        assertEquals("providers() does not return a copy", 1, br.getProviders()
            .size());
    }

    protected void setUp() throws Exception {
        sharedMock.setCurrentMock(contextBuilder.returnsEnvironment(new Hashtable()));
        testRootPath = File.createTempFile("test", null).getParentFile();
        testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, "testProvider");
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        anotherEnvironment = new Hashtable(testEnvironment);
        anotherEnvironment.put(HJBProvider.HJB_PROVIDER_NAME,
                               "anotherTestProvider");
        anotherEnvironment.put("extra", "dummy info");
    }

    protected void tearDown() throws Exception {
        testEnvironment.clear();
        anotherEnvironment.clear();
    }

    private Hashtable testEnvironment;

    private Hashtable anotherEnvironment;

    private File testRootPath;
    private MockContextBuilder contextBuilder;
    private SharedMock sharedMock;

}