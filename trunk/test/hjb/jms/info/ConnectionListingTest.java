package hjb.jms.info;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.Session;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.HJBConnection;
import hjb.jms.info.ConnectionListing;
import hjb.testsupport.MockConnectionBuilder;

public class ConnectionListingTest extends MockObjectTestCase {

    public void testGetListingIncludesAllConnections() {
        String expectedOutput = createSomeConnections();
        assertEquals(expectedOutput,
                     new ConnectionListing(testConnection).getListing("/testProvider/testFactory/connection-10"));
    }

    public void testWriteListingIncludesAllConnections() {
        StringWriter sw = new StringWriter();
        String expectedOutput = createSomeConnections();
        new ConnectionListing(testConnection).writeListing(sw,
                                                           "/testProvider/testFactory/connection-10",
                                                           false);
        assertEquals(expectedOutput, sw.toString());
    }

    public void testRecurseListingAddsSessionListings() {
        StringWriter sw = new StringWriter();
        String expectedOutput = createSomeConnections();
        expectedOutput = expectedOutput + CR
                + "/testProvider/testFactory/connection-10/session-1" + CR
                + "/testProvider/testFactory/connection-10/session-0";

        new ConnectionListing(testConnection).writeListing(sw,
                                                           "/testProvider/testFactory/connection-10",
                                                           true);
        assertEquals(expectedOutput, sw.toString());
    }

    protected String createSomeConnections() {
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        testConnection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        String expectedOutput = "/testProvider/testFactory/connection-10/session-1" + CR
                + "/testProvider/testFactory/connection-10/session-0";
        return expectedOutput;
    }

    protected void setUp() throws Exception {
        Mock mockSession = mock(Session.class);
        mockSession.stubs().method("getTransacted").will(returnValue(false));
        Mock mockConnection = new MockConnectionBuilder().createMockConnection((Session) mockSession.proxy());
        mockConnection.stubs().method("setExceptionListener");
        mockConnection.stubs()
            .method("getClientID")
            .will(returnValue("testClientID"));
        testConnection = new HJBConnection((Connection) mockConnection.proxy(),
                                           "testClientId",
                                           10);
    }

    protected void tearDown() throws Exception {
    }

    public static final String CR = System.getProperty("line.separator");
    private HJBConnection testConnection;
}
