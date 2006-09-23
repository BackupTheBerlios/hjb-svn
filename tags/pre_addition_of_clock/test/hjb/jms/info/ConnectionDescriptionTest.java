package hjb.jms.info;

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;

import org.jmock.Mock;

import hjb.jms.HJBConnection;
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
                                                                                            0));
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
                                                                                            0));
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
                                                                                            1));

        String expectedOutput = testDescription.toString() + CR
                + "clientId=foobar" + CR + "jms-major-version=(int 444)" + CR
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
