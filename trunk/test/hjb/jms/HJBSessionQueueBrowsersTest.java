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
package hjb.jms;

import javax.jms.Connection;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.jmock.Mock;

import hjb.misc.HJBException;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockSessionBuilder;

public class HJBSessionQueueBrowsersTest extends BaseHJBTestCase {

    public void testHJBSessionQueueBrowsersThrowsIllegalArgumentExceptionOnNullSession() {
        try {
            new HJBSessionQueueBrowsers(null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testCreateBrowserThrowsHJBExceptionOnJMSException()
            throws Exception {
        mockSession = sessionBuilder.createMockSessionThatThrowsJMSOn("createBrowser");
        HJBSession aSession = new HJBSession((Session) mockSession.proxy(), 0, defaultTestClock());
        updateConnectionMock(aSession);

        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(aSession);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            browsers.createBrowser(testQueue, "test");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            browsers.createBrowser(testQueue);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetBrowserThrowsForInvalidBrowser() throws Exception {
        Mock mockQueueBrowser = new Mock(QueueBrowser.class);
        QueueBrowser testQueueBrowser = (QueueBrowser) mockQueueBrowser.proxy();
        mockSession.stubs()
            .method("createBrowser")
            .will(returnValue(testQueueBrowser));

        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testSession);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        browsers.createBrowser(testQueue);
        try {
            browsers.getBrowser(1);
            fail("should have thrown an exception - producer 1 does not exist yet");
        } catch (HJBException hjbe) {}
    }

    public void testGetBrowserReturnsCreatedBrowsers() throws Exception {
        Mock mockQueueBrowser = new Mock(QueueBrowser.class);
        QueueBrowser testQueueBrowser = (QueueBrowser) mockQueueBrowser.proxy();
        mockSession.stubs()
            .method("createBrowser")
            .will(returnValue(testQueueBrowser));

        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testSession);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0", 0, browsers.createBrowser(testQueue));
        assertNotNull("got first browser", browsers.getBrowser(0));
        assertEquals("Index should be 1", 1, browsers.createBrowser(testQueue,
                                                                    "test"));
        assertNotNull("got second browser", browsers.getBrowser(1));
    }

    protected void setUp() throws Exception {
        super.setUp();
        initialiseMockBuilders();

        mockSession = new MockSessionBuilder().createMockSession();
        registerToVerify(mockSession);
        testSession = new HJBSession((Session) mockSession.proxy(), 0, defaultTestClock());

        mockQueue = new Mock(Queue.class);
        testQueue = (Queue) mockQueue.proxy();

        updateConnectionMock(testSession);
    }

    protected void initialiseMockBuilders() {
        connectionBuilder = new MockConnectionBuilder();
        sessionBuilder = new MockSessionBuilder();
    }

    protected void updateConnectionMock(Session aSession) {
        mockConnection = connectionBuilder.createMockConnection(aSession);
        registerToVerify(mockConnection);
        Connection aConnection = (Connection) mockConnection.proxy();
        testConnection = new HJBConnection(aConnection, 0, defaultTestClock());
    }

    private Mock mockConnection;
    private HJBConnection testConnection;
    private Mock mockQueue;
    private Queue testQueue;
    private Mock mockSession;
    private HJBSession testSession;

    private MockConnectionBuilder connectionBuilder;
    private MockSessionBuilder sessionBuilder;

}
