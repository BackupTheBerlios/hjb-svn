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
import hjb.misc.HJBConstants;
import hjb.misc.MessageProducerArguments;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;

public class SessionListingTest extends BaseHJBTestCase {

    public void testGetListingIncludesAllConnections() {
        String expectedOutput = createSomeSessionObjects();
        assertEquals(expectedOutput,
                     new SessionListing(testSession).getListing("/testProvider/testFactory/connection-10"));
    }

    public void testWriteListingIncludesAllConnections() {
        StringWriter sw = new StringWriter();
        String expectedOutput = createSomeSessionObjects();
        new SessionListing(testSession).writeListing(sw,
                                                     "/testProvider/testFactory/connection-10",
                                                     false);
        assertEquals(expectedOutput, sw.toString());
    }

    public void testRecurseListingAddsSessionObjectListings() {
        StringWriter sw = new StringWriter();
        createSomeSessionObjects();
        String expectedOutput = "/testProvider/testFactory/connection-10/session-0/consumer-0"
                + CR
                + HJBConstants.CREATION_TIME
                + "="
                + defaultClockTimeAsHJBEncodedLong()
                + CR
                + "message-selector=testSelector"
                + CR
                + "/testProvider/testFactory/connection-10/session-0/subscriber-0[(source mockTopic) (nolocal? false)]"
                + CR
                + HJBConstants.CREATION_TIME
                + "="
                + defaultClockTimeAsHJBEncodedLong()
                + CR
                + "message-selector=testSelector"
                + CR
                + "no-local=(boolean false)"
                + CR
                + "subscriber-name=testTopic"
                + CR
                + "/testProvider/testFactory/connection-10/session-0/producer-0[(target supplied-by-sender) (priority -4)]"
                + CR
                + HJBConstants.CREATION_TIME
                + "="
                + defaultClockTimeAsHJBEncodedLong()
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
                + CR
                + HJBConstants.CREATION_TIME
                + "="
                + defaultClockTimeAsHJBEncodedLong()
                + CR
                + "message-selector=testSelector";
        new SessionListing(testSession).writeListing(sw,
                                                     "/testProvider/testFactory/connection-10",
                                                     true);
        assertEquals(expectedOutput, sw.toString());
    }

    protected String createSomeSessionObjects() {
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
        HJBSessionProducers producers = testSession.getProducers();
        producers.createProducer((Destination) mock(Destination.class).proxy(),
                                 new MessageProducerArguments());
    }

    protected void addDefaultTestConsumer() {
        HJBSessionConsumers consumers = testSession.getConsumers();
        consumers.createConsumer((Destination) mock(Destination.class).proxy());
    }

    protected void addDefaultTestBrowser() {
        HJBSessionQueueBrowsers browsers = testSession.getBrowsers();
        Mock mockQueue = mock(Queue.class);
        browsers.createBrowser((Queue) mockQueue.proxy());
    }

    protected void addDefaultTestSubscriber() {
        HJBSessionDurableSubscribers subscribers = testSession.getSubscribers();
        Mock mockTopic = mock(Topic.class);
        subscribers.createDurableSubscriber((Topic) mockTopic.proxy(),
                                            "testSubscriber");
    }

    protected void setUp() throws Exception {
        Mock mockConnection = new MockConnectionBuilder().createMockConnection();
        mockConnection.stubs().method("setClientID");
        testConnection = new HJBConnection((Connection) mockConnection.proxy(),
                                           "testClientId",
                                           10,
                                           defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        testSession = testConnection.getSession(0);
    }

    protected void tearDown() throws Exception {
    }

    public static final String CR = System.getProperty("line.separator");
    private HJBConnection testConnection;
    private HJBSession testSession;
}
