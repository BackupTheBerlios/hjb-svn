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

import javax.jms.*;

import org.jmock.Mock;

import hjb.jms.*;
import hjb.misc.MessageProducerArguments;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;

public class SessionListingTest extends BaseHJBTestCase {

    public void testGetListingIncludesAllConnections() {
        String expectedOutput = createSomeSessionObjects();
        assertEquals(expectedOutput,
                     new SessionListing(testConnection, 0).getListing("/testProvider/testFactory/connection-10"));
    }

    public void testWriteListingIncludesAllConnections() {
        StringWriter sw = new StringWriter();
        String expectedOutput = createSomeSessionObjects();
        new SessionListing(testConnection, 0).writeListing(sw,
                                                           "/testProvider/testFactory/connection-10",
                                                           false);
        assertEquals(expectedOutput, sw.toString());
    }

    public void testRecurseListingAddsSessionListings() {
        StringWriter sw = new StringWriter();
        createSomeSessionObjects();
        String expectedOutput =
        "/testProvider/testFactory/connection-10/session-0/consumer-0"
                + CR
                + "message-selector=testSelector"
                + CR
                + "/testProvider/testFactory/connection-10/session-0/subscriber-0[(source mockTopic) (nolocal? false)]"
                + CR
                + "message-selector=testSelector"
                + CR
                + "no-local=(boolean false)"
                + CR
                + "subscriber-name=testTopic"
                + CR
                + "/testProvider/testFactory/connection-10/session-0/producer-0[(target supplied-by-sender) (priority -4)]"
                + CR
                + "delivery-mode=(int 1)"
                + CR
                + "disable-message-ids=(boolean false)"
                + CR
                + "disable-timestamps=(boolean false)"
                + CR
                + "priority=(int -4)"
                + CR
                + "time-to-live=(long 1)"
                + CR
                + "/testProvider/testFactory/connection-10/session-0/browser-0[(source mockQueue)]"
                + CR + "message-selector=testSelector";
        new SessionListing(testConnection, 0).writeListing(sw,
                                                           "/testProvider/testFactory/connection-10",
                                                           true);
        assertEquals(expectedOutput, sw.toString());
    }

    protected String createSomeSessionObjects() {
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        addDefaultTestConsumer();
        addDefaultTestProducer();
        addDefaultTestSubscriber();
        addDefaultTestBrowser();
        String expectedOutput = "/testProvider/testFactory/connection-10/session-0/consumer-0"
                + CR
                + "/testProvider/testFactory/connection-10/session-0/subscriber-0[(source mockTopic) (nolocal? false)]"
                + CR
                + "/testProvider/testFactory/connection-10/session-0/producer-0[(target supplied-by-sender) (priority -4)]"
                + CR
                + "/testProvider/testFactory/connection-10/session-0/browser-0[(source mockQueue)]";
        return expectedOutput;
    }

    protected void addDefaultTestProducer() {
        HJBSessionProducers sessionProducers = testConnection.getSessionProducers();
        sessionProducers.createProducer(0,
                                        (Destination) mock(Destination.class).proxy(),
                                        new MessageProducerArguments());
    }

    protected void addDefaultTestConsumer() {
        HJBSessionConsumers sessionConsumers = testConnection.getSessionConsumers();
        sessionConsumers.createConsumer(0,
                                        (Destination) mock(Destination.class).proxy());
    }

    protected void addDefaultTestBrowser() {
        HJBSessionQueueBrowsers browsers = testConnection.getSessionBrowsers();
        Mock mockQueue = mock(Queue.class);
        browsers.createBrowser(0, (Queue) mockQueue.proxy());
    }

    protected void addDefaultTestSubscriber() {
        HJBSessionDurableSubscribers sessionSubscribers = testConnection.getSessionSubscribers();
        Mock mockTopic = mock(Topic.class);
        sessionSubscribers.createDurableSubscriber(0,
                                                   (Topic) mockTopic.proxy(),
                                                   "testSubscriber");
    }

    protected void setUp() throws Exception {
        Mock mockConnection = new MockConnectionBuilder().createMockConnection();
        mockConnection.stubs().method("setClientID");
        testConnection = new HJBConnection((Connection) mockConnection.proxy(),
                                           "testClientId",
                                           10, defaultTestClock());
    }

    protected void tearDown() throws Exception {
    }

    public static final String CR = System.getProperty("line.separator");
    private HJBConnection testConnection;
}
