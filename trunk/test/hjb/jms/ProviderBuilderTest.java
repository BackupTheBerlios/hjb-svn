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

import java.util.Hashtable;

import javax.naming.Context;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.testsupport.MockContextBuilder;
import hjb.testsupport.SharedMock;

public class ProviderBuilderTest extends MockObjectTestCase {

    public ProviderBuilderTest() {
        contextBuilder = new MockContextBuilder();
        sharedMock = SharedMock.getInstance();
    }

    public void testProviderBuilder() {
        Hashtable environment = new Hashtable();
        ProviderBuilder pc = null;
        try {
            pc = new ProviderBuilder(environment);
            fail("construction succeeded with a Hashtable without a "
                    + HJBProvider.HJB_PROVIDER_NAME);
        } catch (HJBException hjbe) {}
        environment.put(HJBProvider.HJB_PROVIDER_NAME, "testProvider");
        pc = new ProviderBuilder(environment);
        assertNotNull("ProviderBuilder was succesfully created", pc);
    }

    public void testProviderBuilderThrowsOnNoName() {
        testEnvironment.remove(HJBProvider.HJB_PROVIDER_NAME);
        try {
            new ProviderBuilder(testEnvironment);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testProviderBuilderThrowsOnInvalidName() {
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, "1willfail");
        try {
            new ProviderBuilder(testEnvironment);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testObtainJNDIEnvironment() throws Exception {
        ProviderBuilder pc = new ProviderBuilder(testEnvironment);
        Hashtable jndi = pc.obtainJNDIEnvironment();
        assertNotNull("the jndi environment was not found", jndi);
        assertTrue("the jndi initial context factory value was not present",
                   jndi.containsKey(Context.INITIAL_CONTEXT_FACTORY));
        assertFalse("the provider value had not been removed",
                    jndi.containsKey(HJBProvider.HJB_PROVIDER_NAME));

    }

    public void testCreateProvider() {
        ProviderBuilder pc = new ProviderBuilder(testEnvironment);
        HJBProvider p = pc.createProvider();
        assertNotNull("the hjb provider was not created", p);
    }

    public void testObtainHJBProviderName() {
        ProviderBuilder pc = new ProviderBuilder(testEnvironment);
        String testName = pc.obtainHJBProviderName();
        assertNotNull("the hjb provider name was not found", testName);
        assertEquals("the hjb provider name had the wrong value",
                     "testProvider",
                     testName);
    }

    public void testObtainInitialContext() throws Exception {
        ProviderBuilder pc = new ProviderBuilder(testEnvironment);
        Context testContext = pc.obtainInitialContext();
        assertNotNull("the initial context was not found", testContext);
        assertEquals("the configured initial context factory was not used",
                     TEST_TOKEN,
                     testContext.getEnvironment().get(TEST_TOKEN));
    }

    protected void setUp() throws Exception {
        testEnvironment = new Hashtable();
        testEnvironment.put(TEST_TOKEN, TEST_TOKEN);
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        sharedMock.setCurrentMock(contextBuilder.returnsEnvironment(new Hashtable(testEnvironment)));
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, "testProvider");
    }

    protected void tearDown() throws Exception {
        testEnvironment.clear();
    }

    public static final String TEST_TOKEN = "testToken";

    private Hashtable testEnvironment;
    private MockContextBuilder contextBuilder;
    private SharedMock sharedMock;
}
