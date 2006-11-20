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

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;

import org.jmock.Mock;

import hjb.jms.HJBConnection;
import hjb.misc.HJBConstants;
import hjb.misc.PathNaming;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;

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
        Mock mockMetaData = new MockConnectionBuilder().createConnectionMetaDataMock();
        ConnectionMetaData testMetaData = (ConnectionMetaData) mockMetaData.proxy();
        Mock mockConnection = new MockConnectionBuilder().createMockConnection();
        mockConnection.stubs()
            .method("getClientID")
            .will(returnValue("foobar"));
        mockConnection.stubs()
            .method("getMetaData")
            .will(returnValue(testMetaData));

        ConnectionDescription testDescription = new ConnectionDescription(new HJBConnection((Connection) mockConnection.proxy(),
                                                                                            null,
                                                                                            1,
                                                                                            defaultTestClock()));

        String expectedOutput = testDescription.toString() + CR
                + "clientId=foobar" + CR + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong() + CR
                + "jms-major-version=(int 444)" + CR
                + "jms-minor-version=(int 777)" + CR
                + "jms-provider-name=testProviderName" + CR
                + "jms-version=testVersion" + CR
                + "jmsx-property-names=[fakeMetaData1, fakeMetaData2]" + CR
                + "provider-major-version=(int 999)" + CR
                + "provider-minor-version=(int 999)" + CR
                + "provider-version=testProviderVersion";

        assertContains(testDescription.toString(), "1");
        assertContains(testDescription.toString(), PathNaming.CONNECTION);
        assertContains(testDescription.toString(), "foobar");
        assertEquals(expectedOutput, testDescription.longDescription());
    }

}
