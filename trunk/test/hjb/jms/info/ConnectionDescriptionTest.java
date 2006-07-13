package hjb.jms.info;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;

import org.jmock.Mock;

import hjb.http.cmd.PathNaming;

public class ConnectionDescriptionTest extends BaseDescriptionTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockConnection = mock(Connection.class);
        Connection testConnection = (Connection) mockConnection.proxy();
        try {
            new ConnectionDescription(testConnection, -1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new ConnectionDescription(null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testToStringHasNoXtraInfoForNonClientIDConnections() {
        Mock mockConnection = mock(Connection.class);
        mockConnection.stubs()
            .method("getClientID")
            .will(returnValue("barfoo"));

        Connection testConnection = (Connection) mockConnection.proxy();
        ConnectionDescription testDescription = new ConnectionDescription(testConnection,
                                                                          0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.CONNECTION);
        assertContains(testDescription.toString(), "barfoo");
    }

    public void testToStringHasXtraInfoForClientIDConnections() {
        Mock mockConnection = mock(Connection.class);
        mockConnection.stubs()
            .method("getClientID")
            .will(returnValue("foobar"));

        Connection testConnection = (Connection) mockConnection.proxy();
        ConnectionDescription testDescription = new ConnectionDescription(testConnection,
                                                                          0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.CONNECTION);
        assertContains(testDescription.toString(), "foobar");
    }

    public void testLongDescriptionIncludeConnectionAttributes() {
        Mock mockMetaData = createConnectionMetaDataMock();
        ConnectionMetaData testMetaData = (ConnectionMetaData) mockMetaData.proxy();
        Mock mockConnection = mock(Connection.class);
        mockConnection.stubs()
            .method("getClientID")
            .will(returnValue("foobar"));
        mockConnection.stubs()
            .method("getMetaData")
            .will(returnValue(testMetaData));

        ConnectionDescription testDescription = new ConnectionDescription((Connection) mockConnection.proxy(),
                                                                          0);

        String expectedOutput = testDescription.toString() + CR
                + "clientId=foobar" + CR + "jms-major-version=(int 444)" + CR
                + "jms-minor-version=(int 777)" + CR
                + "jms-provider-name=testProviderName" + CR
                + "jms-version=testVersion" + CR
                + "jmsx-property-names=[fakeMetaData1, fakeMetaData2]" + CR
                + "provider-major-version=(int 999)" + CR
                + "provider-minor-version=(int 999)" + CR
                + "provider-version=testProviderVersion";

        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.CONNECTION);
        assertContains(testDescription.toString(), "foobar");
        assertEquals(expectedOutput, testDescription.longDescription());
    }

    protected Mock createConnectionMetaDataMock() {
        Mock mockMetaData = mock(ConnectionMetaData.class);
        for (int i = 0; i < METADATA_TEST_VALUES.length; i++) {
            mockMetaData.stubs()
                .method("" + METADATA_TEST_VALUES[i][0])
                .will(returnValue(METADATA_TEST_VALUES[i][1]));
        }
        String[] fakeCustomMetaDataNames = {
                "fakeMetaData1", "fakeMetaData2"
        };
        Enumeration fakeNames = Collections.enumeration(Arrays.asList(fakeCustomMetaDataNames));
        mockMetaData.stubs()
            .method("getJMSXPropertyNames")
            .will(returnValue(fakeNames));
        return mockMetaData;
    }

    private Object[][] METADATA_TEST_VALUES = {
            {
                    "getJMSVersion", "testVersion"
            }, {
                    "getJMSProviderName", "testProviderName"
            }, {
                    "getJMSMajorVersion", new Integer(444)
            }, {
                    "getJMSMinorVersion", new Integer(777)
            }, {
                    "getProviderMajorVersion", new Integer(999)
            }, {
                    "getProviderMinorVersion", new Integer(999)
            }, {
                    "getProviderVersion", "testProviderVersion"
            },
    };

}
