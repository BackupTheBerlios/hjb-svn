package hjb.jms.info;

import java.io.StringWriter;

import javax.jms.ConnectionFactory;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.HJBConnectionFactory;
import hjb.jms.info.ConnectionFactoryListing;
import hjb.testsupport.MockConnectionFactoryBuilder;

public class ConnectionFactoryListingTest extends MockObjectTestCase {

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
                + "/testProvider/testFactory/connection-1" + CR
                + "/testProvider/testFactory/connection-0";

        new ConnectionFactoryListing(testFactory).writeListing(sw,
                                                               "/testProvider/testFactory",
                                                               true);
        assertEquals(expectedOutput, sw.toString());
    }

    protected String createSomeConnections() {
        testFactory.createConnection();
        testFactory.createConnection();
        String expectedOutput = "/testProvider/testFactory/connection-1" + CR
                + "/testProvider/testFactory/connection-0";
        return expectedOutput;
    }

    protected void setUp() throws Exception {
        Mock mockFactory = new MockConnectionFactoryBuilder().createMockConnectionFactory();
        testFactory = new HJBConnectionFactory((ConnectionFactory) mockFactory.proxy());
    }

    protected void tearDown() throws Exception {
    }

    public static final String CR = System.getProperty("line.separator");
    private HJBConnectionFactory testFactory;
}
