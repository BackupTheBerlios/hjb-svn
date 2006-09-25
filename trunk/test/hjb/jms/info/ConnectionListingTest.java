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

import javax.jms.Connection;
import javax.jms.Queue;
import javax.jms.Session;

import org.jmock.Mock;

import hjb.jms.HJBConnection;
import hjb.jms.HJBSessionQueueBrowsers;
import hjb.misc.HJBConstants;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;

public class ConnectionListingTest extends BaseHJBTestCase {

    public void testGetListingIncludesAllConnections() {
        String expectedOutput = createSomeSessions();
        assertEquals(expectedOutput,
                     new ConnectionListing(testConnection).getListing("/testProvider/testFactory/connection-10"));
    }

    public void testWriteListingIncludesAllConnections() {
        StringWriter sw = new StringWriter();
        String expectedOutput = createSomeSessions();
        new ConnectionListing(testConnection).writeListing(sw,
                                                           "/testProvider/testFactory/connection-10",
                                                           false);
        assertEquals(expectedOutput, sw.toString());
    }

    public void testRecurseListingAddsSessionListings() {
        StringWriter sw = new StringWriter();
        String expectedOutput = createSomeSessions();
        addDefaultTestBrowser();
        expectedOutput = expectedOutput
                + CR
                + "/testProvider/testFactory/connection-10/session-1"
                + CR
                + "acknowledgement-mode=(int 1)"
                + CR
                + HJBConstants.CREATION_TIME
                + "="
                + defaultClockTimeAsHJBEncodedLong()
                + CR
                + "transacted=(boolean false)"
                + CR
                + CR
                + "/testProvider/testFactory/connection-10/session-0"
                + CR
                + "acknowledgement-mode=(int 1)"
                + CR
                + HJBConstants.CREATION_TIME
                + "="
                + defaultClockTimeAsHJBEncodedLong()
                + CR
                + "transacted=(boolean false)"
                + CR
                + "/testProvider/testFactory/connection-10/session-0/browser-0[(source mockQueue)]"
                + CR + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong() + CR
                + "message-selector=testSelector";
        new ConnectionListing(testConnection).writeListing(sw,
                                                           "/testProvider/testFactory/connection-10",
                                                           true);
        assertEquals(expectedOutput, sw.toString());
    }

    protected String createSomeSessions() {
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        testConnection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        String expectedOutput = "/testProvider/testFactory/connection-10/session-1"
                + CR + "/testProvider/testFactory/connection-10/session-0" + CR;
        return expectedOutput;
    }

    protected void setUp() throws Exception {
        Mock mockConnection = new MockConnectionBuilder().createMockConnection();
        mockConnection.stubs().method("setClientID");
        mockConnection.stubs()
            .method("getClientID")
            .will(returnValue("testClientId"));
        testConnection = new HJBConnection((Connection) mockConnection.proxy(),
                                           "testClientId",
                                           10,
                                           defaultTestClock());
    }

    protected void addDefaultTestBrowser() {
        HJBSessionQueueBrowsers browsers = testConnection.getSession(0)
            .getBrowsers();
        Mock mockQueue = mock(Queue.class);
        browsers.createBrowser((Queue) mockQueue.proxy());
    }

    public static final String CR = System.getProperty("line.separator");
    private HJBConnection testConnection;
}
