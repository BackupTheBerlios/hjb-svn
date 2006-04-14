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
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.testsupport.MockContextBuilder;
import hjb.testsupport.SharedMock;

public class HJBProviderTest extends MockObjectTestCase {
    public HJBProviderTest() {
        contextBuilder = new MockContextBuilder();
        sharedMock = SharedMock.getInstance();
    }

    public void testCannotRegisterDestinationThatIsNotInTheContext()
            throws Exception {
        try {
            testProvider.registerDestination("foo");
            fail("Allowed the registration of a destination that does not exist");
        } catch (HJBException hjbe) {}
    }

    public void testCanRegisterDestinationThatIsInTheContext() {
        Mock destinationMock = new Mock(Destination.class, "testdestination");
        Destination destination = (Destination) destinationMock.proxy();
        Mock newMock = contextBuilder.lookupReturnsDestination(testEnvironment,
                                                               destination);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        try {
            testProvider.registerDestination("testdestination");
        } catch (HJBException hjbe) {
            fail("Disallowed the registration of an registered destination");
        }
        assertTrue("testdestination should be registered",
                   testProvider.getDestinations()
                       .containsKey("testdestination"));
    }

    public void testGetDestinationsReturnsACopy() {
        Mock destinationMock = new Mock(Destination.class, "testdestination");
        Destination destination = (Destination) destinationMock.proxy();
        Mock newMock = contextBuilder.lookupReturnsDestination(testEnvironment,
                                                               destination);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        testProvider.registerDestination("testdestination");
        Map destinations = testProvider.getDestinations();
        destinations.clear();
        assertTrue("testdestination should still be registered",
                   testProvider.getDestinations()
                       .containsKey("testdestination"));
    }

    public void testGetDestination() {
        Mock destinationMock = new Mock(Destination.class, "testdestination");
        Destination destination = (Destination) destinationMock.proxy();
        Mock newMock = contextBuilder.lookupReturnsDestination(testEnvironment,
                                                               destination);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        assertNull("should not exist",
                   testProvider.getDestination("testdestination"));
        testProvider.registerDestination("testdestination");
        assertNotNull("should exist",
                      testProvider.getDestination("testdestination"));
    }

    public void testGetConnectionFactoriesReturnsACopy() {
        Mock connectionFactoryMock = new Mock(ConnectionFactory.class,
                                              "testfactory");
        ConnectionFactory connectionFactory = (ConnectionFactory) connectionFactoryMock.proxy();
        Mock newMock = contextBuilder.lookupReturnsConnectionFactory(testEnvironment,
                                                                     connectionFactory);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        testProvider.registerConnectionFactory("testfactory");
        Map factories = testProvider.getConnectionFactories();
        factories.clear();
        assertTrue("testfactory should still be registered",
                   testProvider.getConnectionFactories()
                       .containsKey("testfactory"));
    }

    public void testGetConnectionFactory() {
        Mock connectionFactoryMock = new Mock(ConnectionFactory.class,
                                              "testfactory");
        ConnectionFactory connectionFactory = (ConnectionFactory) connectionFactoryMock.proxy();
        Mock newMock = contextBuilder.lookupReturnsConnectionFactory(testEnvironment,
                                                                     connectionFactory);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        assertNull("should not exist",
                   testProvider.getConnectionFactory("testfactory"));
        testProvider.registerConnectionFactory("testfactory");
        assertNotNull("should exist",
                      testProvider.getConnectionFactory("testfactory"));
    }

    public void testCannotRegisterDestinationWhenLookupThrows()
            throws Exception {
        Mock destinationMock = new Mock(Destination.class, "testdestination");
        Destination destination = (Destination) destinationMock.proxy();
        Mock newMock = contextBuilder.lookupThrowsNamingException(testEnvironment,
                                                                  destination);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        try {
            testProvider.registerDestination("testdestination");
            fail("Allowed the registration of a destination when lookup throws");
        } catch (HJBException hjbe) {}
        assertFalse("testdestination should not be registered",
                    testProvider.getConnectionFactories()
                        .containsKey("testdestination"));
    }

    public void testCanDeleteDestinationThatIsNotInTheContext()
            throws Exception {
        assertFalse("foo should not be registered",
                    testProvider.getDestinations().containsKey("foo"));
        testProvider.deleteDestination("foo");
        assertFalse("foo should not be registered",
                    testProvider.getDestinations().containsKey("foo"));
    }

    public void testDeletingNonExistentDestinationDoesNotThrow()
            throws Exception {
        Mock DestinationMock = new Mock(Destination.class, "testdestination");
        Destination mockFactory = (Destination) DestinationMock.proxy();
        Mock newMock = contextBuilder.lookupReturnsDestination(testEnvironment,
                                                               mockFactory);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        testProvider.registerDestination("testdestination");
        try {
            testProvider.deleteDestination("testdestination");
        } catch (HJBException hjbe) {
            fail("Disallowed the removal of an registered destination");
        }
        assertFalse("testdestination should not be registered",
                    testProvider.getConnectionFactories()
                        .containsKey("testdestination"));
    }

    public void testCannotRegisterConnectionFactoryThatIsNotInTheContext()
            throws Exception {
        try {
            testProvider.registerConnectionFactory("foo");
            fail("Allowed the registration of a connection factory that does not exist");
        } catch (HJBException hjbe) {}
    }

    public void testCanRegisterConnectionFactoryThatIsInTheContext()
            throws Exception {
        Mock connectionFactoryMock = new Mock(ConnectionFactory.class,
                                              "testfactory");
        ConnectionFactory mockFactory = (ConnectionFactory) connectionFactoryMock.proxy();
        Mock newMock = contextBuilder.lookupReturnsConnectionFactory(testEnvironment,
                                                                     mockFactory);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        try {
            testProvider.registerConnectionFactory("testfactory");
        } catch (HJBException hjbe) {
            fail("Disallowed the registration of an registered connection factory");
        }
        assertTrue("testfactory should be registered",
                   testProvider.getConnectionFactories()
                       .containsKey("testfactory"));
    }

    public void testCannotRegisterConnectionFactoryWhenLookupThrows()
            throws Exception {
        Mock connectionFactoryMock = new Mock(ConnectionFactory.class,
                                              "testfactory");
        ConnectionFactory mockFactory = (ConnectionFactory) connectionFactoryMock.proxy();
        Mock newMock = contextBuilder.lookupThrowsNamingException(testEnvironment,
                                                                  mockFactory);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        try {
            testProvider.registerConnectionFactory("testfactory");
            fail("Allowed the registration of a connection factory when lookup throws");
        } catch (HJBException hjbe) {}
        assertFalse("testfactory should not be registered",
                    testProvider.getConnectionFactories()
                        .containsKey("testfactory"));
    }

    public void testDeletingNonExistConnectionFactoryDoesNotThrow()
            throws Exception {
        assertFalse("foo should not be registered",
                    testProvider.getConnectionFactories().containsKey("foo"));
        testProvider.deleteConnectionFactory("foo");
        assertFalse("foo should not be registered",
                    testProvider.getConnectionFactories().containsKey("foo"));
    }

    public void testCanDeleteConnectionFactoryThatIsInTheContext()
            throws Exception {
        Mock connectionFactoryMock = new Mock(ConnectionFactory.class,
                                              "testfactory");
        ConnectionFactory mockFactory = (ConnectionFactory) connectionFactoryMock.proxy();
        Mock newMock = contextBuilder.lookupReturnsConnectionFactory(testEnvironment,
                                                                     mockFactory);
        sharedMock.setCurrentMock(newMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
        testProvider.registerConnectionFactory("testfactory");
        try {
            testProvider.deleteConnectionFactory("testfactory");
        } catch (HJBException hjbe) {
            fail("Disallowed the removal of an registered connection factory");
        }
        assertFalse("testfactory should not be registered",
                    testProvider.getConnectionFactories()
                        .containsKey("testfactory"));
    }

    /*
     * Test method for 'hjb.jms.Provider.getEnvironment()'
     */
    public void testGetEnvironment() throws Exception {
        Hashtable environment = new Hashtable();
        environment.put("foo", "bar");
        Context aContext = new InitialContext();
        HJBProvider aProvider = new HJBProvider("test1", environment, aContext);
        assertFalse("Providers share their copies of their environments",
                    aProvider.getEnvironment() == environment);
    }

    /*
     * Test method for 'hjb.jms.Provider.equals(Object)'
     */
    public void testEqualsObject() throws Exception {
        Hashtable environment = new Hashtable();
        environment.put("foo", "bar");
        Hashtable similarEnvironment = new Hashtable();
        similarEnvironment.put("foo", "bar");
        Context sameContext = new InitialContext();
        HJBProvider aProvider = new HJBProvider("test1",
                                                environment,
                                                sameContext);
        HJBProvider anotherProvider = new HJBProvider("test2",
                                                      similarEnvironment,
                                                      sameContext);
        assertEquals("Providers with different names but the same environment should be equal",
                     aProvider,
                     anotherProvider);
        environment.put("foo", "baz");
        aProvider = new HJBProvider("test2", environment, sameContext);
        assertFalse("Providers with the same names but different environments should not be equal",
                    aProvider.equals(anotherProvider));
    }

    protected void setUp() throws Exception {
        testEnvironment = new Hashtable();
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        sharedMock.setCurrentMock(contextBuilder.returnsEnvironment(testEnvironment));
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, "testProvider");
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
    }

    protected void tearDown() throws Exception {
        testEnvironment.clear();
    }
    private Hashtable testEnvironment;
    private HJBProvider testProvider;
    private MockContextBuilder contextBuilder;
    private SharedMock sharedMock;
}
