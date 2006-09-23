package hjb.jms.info;

import java.io.StringWriter;
import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.HJBProvider;
import hjb.jms.ProviderBuilder;
import hjb.testsupport.MockConnectionFactoryBuilder;
import hjb.testsupport.MockContextBuilder;
import hjb.testsupport.SharedMock;

public class ProviderListingTest extends MockObjectTestCase {

    public void testGetListingIncludesAllDestinationsAndConnectionFactories() {
        String expectedOutput = registerSomeObjects();
        assertEquals(expectedOutput,
                     new ProviderListing(testProvider).getListing("/testProvider"));
    }

    public void testWriteListingIncludesAllDestinationsAndConnectionFactories() {
        StringWriter sw = new StringWriter();
        String expectedOutput = registerSomeObjects();
        new ProviderListing(testProvider).writeListing(sw,
                                                       "/testProvider",
                                                       false);
        assertEquals(expectedOutput, sw.toString());
    }

    public void testRecurseListingAddsConnectionFactoryListings() {
        StringWriter sw = new StringWriter();
        String expectedOutput = registerSomeObjects();
        testProvider.getConnectionFactory("fooBarFactory")
            .createHJBConnection(null);
        expectedOutput = expectedOutput + CR + "/testProvider/fooBarFactory" +CR
                + "/testProvider/fooBarFactory/connection-0" + CR + CR
                + "/testProvider/fooBarFactory/connection-0" + CR + CR
                + "/testProvider/barFooFactory" + CR;

        new ProviderListing(testProvider).writeListing(sw,
                                                       "/testProvider",
                                                       true);
        assertEquals(expectedOutput, sw.toString());
    }

    protected String registerSomeObjects() {
        testProvider.registerConnectionFactory("fooBarFactory");
        testProvider.registerConnectionFactory("barFooFactory");
        testProvider.registerDestination("barBazDestination");
        testProvider.registerDestination("bazbarDestination");

        String expectedOutput = "/testProvider/bazbarDestination" + CR
                + "/testProvider/barBazDestination" + CR
                + "/testProvider/fooBarFactory" + CR
                + "/testProvider/barFooFactory" + CR;

        return expectedOutput;
    }

    protected void setUp() throws Exception {
        Mock mockFactory = new MockConnectionFactoryBuilder().createMockConnectionFactory();
        Mock mockDestination = mock(Destination.class);

        testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, "testProvider");
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.lookupReturnsMatchingObject(testEnvironment,
                                                                         (ConnectionFactory) mockFactory.proxy(),
                                                                         (Destination) mockDestination.proxy());
        sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);
        testProvider = new ProviderBuilder(testEnvironment).createProvider();
    }

    protected void tearDown() throws Exception {
        testEnvironment.clear();
    }

    public static final String CR = System.getProperty("line.separator");
    private Hashtable testEnvironment;
    private HJBProvider testProvider;
    private MockContextBuilder contextBuilder;
    private SharedMock sharedMock;
}
