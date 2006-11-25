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

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.jmock.Mock;

import hjb.jms.HJBConnectionFactory;
import hjb.misc.HJBConstants;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionFactoryBuilder;

public class ConnectionFactoryListingTest extends BaseHJBTestCase {

    public void testGetListingIncludesAllConnections() {
        String expectedOutput = createSomeConnections();
        assertEquals(expectedOutput,
                     new ConnectionFactoryListing(testFactory).getListing("/testProvider/testFactory"));
    }

    public void testWriteListingIncludesAllConnections() {
        StringWriter sw = new StringWriter();
        String expectedOutput = createSomeConnections();
        new ConnectionFactoryListing(testFactory).writeListing(sw,
                                                               "/testProvider/testFactory",
                                                               false);
        assertEquals(expectedOutput, sw.toString());
    }

    public void testRecurseListingAddsSessionListings() {
        StringWriter sw = new StringWriter();
        String expectedOutput = createSomeConnections();
        expectedOutput = expectedOutput + CR
                + "/testProvider/testFactory/connection-1:" + CR + "total 0"
                + CR + CR + "/testProvider/testFactory/connection-0:" + CR
                + "total 2" + CR + "session-1" + CR
                + "\tacknowledgement-mode=(int 1), "
                + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong()
                + ", transacted=(boolean false)" + CR + "session-0" + CR
                + "\tacknowledgement-mode=(int 1), "
                + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong()
                + ", transacted=(boolean false)" + CR + CR
                + "/testProvider/testFactory/connection-0/session-1:" + CR
                + "total 0" + CR + CR
                + "/testProvider/testFactory/connection-0/session-0:" + CR
                + "total 0" + CR;

        new ConnectionFactoryListing(testFactory).writeListing(sw,
                                                               "/testProvider/testFactory",
                                                               true);
        System.err.println(sw.toString());
        assertEquals(expectedOutput, sw.toString());
    }

    protected String createSomeConnections() {
        testFactory.createConnection();
        testFactory.createConnection();
        testFactory.getConnection(0).createSession(true,
                                                   Session.AUTO_ACKNOWLEDGE);
        testFactory.getConnection(0).createSession(true,
                                                   Session.AUTO_ACKNOWLEDGE);
        String expectedOutput = "/testProvider/testFactory:" + CR + "total 2"
                + CR + "connection-1" + CR + '\t' + "clientId=, "
                + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong()
                + ", jms-major-version=(int 444)"
                + ", jms-minor-version=(int 777)"
                + ", jms-provider-name=testProviderName"
                + ", jms-version=testVersion"
                + ", jmsx-property-names=[fakeMetaData1, fakeMetaData2]"
                + ", provider-major-version=(int 999)"
                + ", provider-minor-version=(int 999)"
                + ", provider-version=testProviderVersion" + CR
                + "connection-0" + CR + '\t' + "clientId=, "
                + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong()
                + ", jms-major-version=(int 444)"
                + ", jms-minor-version=(int 777)"
                + ", jms-provider-name=testProviderName"
                + ", jms-version=testVersion" + ", jmsx-property-names=[]"
                + ", provider-major-version=(int 999)"
                + ", provider-minor-version=(int 999)"
                + ", provider-version=testProviderVersion" + CR;
        return expectedOutput;
    }

    protected void setUp() throws Exception {
        Mock mockFactory = new MockConnectionFactoryBuilder().createMockConnectionFactory();
        testFactory = new HJBConnectionFactory((ConnectionFactory) mockFactory.proxy(),
                                               defaultTestClock());
    }

    protected void tearDown() throws Exception {
    }

    public static final String CR = System.getProperty("line.separator");
    private HJBConnectionFactory testFactory;
}
