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

import hjb.jms.HJBConnection;
import hjb.misc.HJBConstants;
import hjb.misc.PathNaming;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;

import javax.jms.Connection;

import org.jmock.Mock;

public class ConnectionDescriptionTest extends BaseHJBTestCase {

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new ConnectionDescription(null);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testToStringHasNoXtraInfoForNonClientIDConnections() {
        Mock mockConnection = new MockConnectionBuilder().createMockConnection();
        mockConnection.stubs()
            .method("getClientID")
            .will(returnValue("barfoo"));

        Connection testConnection = (Connection) mockConnection.proxy();
        ConnectionDescription testDescription = new ConnectionDescription(new HJBConnection(testConnection,
                                                                                            null,
                                                                                            0,
                                                                                            defaultTestClock()));
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.CONNECTION);
        assertContains(testDescription.toString(), "barfoo");
    }

    public void testToStringHasXtraInfoForClientIDConnections() {
        Mock mockConnection = new MockConnectionBuilder().createMockConnection();
        mockConnection.stubs()
            .method("getClientID")
            .will(returnValue("foobar"));

        Connection testConnection = (Connection) mockConnection.proxy();
        ConnectionDescription testDescription = new ConnectionDescription(new HJBConnection(testConnection,
                                                                                            null,
                                                                                            0,
                                                                                            defaultTestClock()));
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.CONNECTION);
        assertContains(testDescription.toString(), "foobar");
    }

    public void testLongDescriptionIncludeConnectionAttributes() {
        Mock mockConnection = new MockConnectionBuilder().createMockConnection();
        mockConnection.stubs()
            .method("getClientID")
            .will(returnValue("foobar"));
        ConnectionDescription testDescription = new ConnectionDescription(new HJBConnection((Connection) mockConnection.proxy(),
                                                                                            null,
                                                                                            1,
                                                                                            defaultTestClock()));

        String expectedOutput = testDescription.toString() + CR + '\t'
                + "clientId=foobar, " + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong()
                + ", jms-major-version=(int 444)"
                + ", jms-minor-version=(int 777)"
                + ", jms-provider-name=testProviderName"
                + ", jms-version=testVersion"
                + ", jmsx-property-names=[fakeMetaData1, fakeMetaData2]"
                + ", provider-major-version=(int 999)"
                + ", provider-minor-version=(int 999)"
                + ", provider-version=testProviderVersion";

        assertContains(testDescription.toString(), "1");
        assertContains(testDescription.toString(), PathNaming.CONNECTION);
        assertContains(testDescription.toString(), "foobar");
        assertEquals(expectedOutput, testDescription.longDescription());
    }

}
