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
package hjb.jms.info;

import java.io.StringWriter;
import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;

import org.jmock.Mock;

import hjb.jms.HJBProvider;
import hjb.jms.ProviderBuilder;
import hjb.misc.HJBConstants;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionFactoryBuilder;
import hjb.testsupport.MockContextBuilder;
import hjb.testsupport.SharedMock;

public class ProviderListingTest extends BaseHJBTestCase {

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
        expectedOutput = expectedOutput + CR + "/testProvider/fooBarFactory:"
                + CR + "total 1" + CR + "connection-0" + CR + '\t'
                + "clientId=, " + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong()
                + ", jms-major-version=(int 444)"
                + ", jms-minor-version=(int 777)"
                + ", jms-provider-name=testProviderName"
                + ", jms-version=testVersion"
                + ", jmsx-property-names=[fakeMetaData1, fakeMetaData2]"
                + ", provider-major-version=(int 999)"
                + ", provider-minor-version=(int 999)"
                + ", provider-version=testProviderVersion" + CR + CR
                + "/testProvider/fooBarFactory/connection-0:" + CR + "total 0"
                + CR + CR + "/testProvider/barFooFactory:" + CR + "total 0"
                + CR;

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

        String expectedOutput = "/testProvider:" + CR + "total 4" + CR
                + "destination/bazbarDestination" + CR
                + "destination/barBazDestination" + CR + "fooBarFactory" + CR
                + "barFooFactory" + CR;

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
        testProvider = new ProviderBuilder(testEnvironment, defaultTestClock()).createProvider();
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
