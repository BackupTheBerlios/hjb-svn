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
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockSessionBuilder;

public class HJBSessionQueueBrowsersTest extends MockObjectTestCase {

    public HJBSessionQueueBrowsersTest() {

    }

    public void testHJBSessionQueueBrowsersThrowsIllegalArgumentExceptionOnNullConnection() {
        try {
            new HJBSessionQueueBrowsers(null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testCreateBrowserThrowsIfSessionIsNotThere() throws Exception {
        mockSession.stubs().method("createBrowser");
        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testConnection);
        try {
            browsers.createBrowser(0, testQueue, "test");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            browsers.createBrowser(0, testQueue);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            browsers.createBrowser(1, testQueue, "test");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            browsers.createBrowser(1, testQueue);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        browsers.createBrowser(0, testQueue);
    }

    public void testCreateBrowserThrowsHJBExceptionOnJMSException()
            throws Exception {
        mockSession = sessionBuilder.createMockSessionThatThrowsJMSOn("createBrowser");
        Session aSession = (Session) mockSession.proxy();
        updateConnectionMock(aSession);

        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            browsers.createBrowser(0, testQueue, "test");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            browsers.createBrowser(0, testQueue);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testCreateBrowserIsInvokedOnCorrectSession() throws Exception {
        mockSession.expects(once()).method("createBrowser");
        registerToVerify(mockSession);
        testSession = (Session) mockSession.proxy();
        updateConnectionMock(testSession);

        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        browsers.createBrowser(0, testQueue, "test");
        assertEquals("there should be 1 producer for the session",
                     1,
                     browsers.getBrowsers(0).length);
    }

    public void testGetBrowsersThrowsIfSessionIsNotThere() throws Exception {
        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testConnection);
        try {
            browsers.getBrowsers(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetBrowsersReturnsEmptyForNewEmptySessions() throws Exception {
        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals(0, browsers.getBrowsers(0).length);
    }

    public void testGetBrowserThrowsIfSessionIsNotThere() throws Exception {
        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testConnection);
        try {
            browsers.getBrowser(0, 0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetBrowserThrowsForNewEmptySessions() throws Exception {
        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            browsers.getBrowser(0, 0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetBrowserThrowsForInvalidBrowser() throws Exception {
        Mock mockQueueBrowser = new Mock(QueueBrowser.class);
        QueueBrowser testQueueBrowser = (QueueBrowser) mockQueueBrowser.proxy();
        mockSession.stubs().method("createBrowser").will(returnValue(testQueueBrowser));

        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        browsers.createBrowser(0, testQueue);
        try {
            browsers.getBrowser(0, 1);
            fail("should have thrown an exception - producer 1 does not exist yet");
        } catch (HJBException hjbe) {}
    }

    public void testGetBrowserReturnsCreatedBrowsers() throws Exception {
        Mock mockQueueBrowser = new Mock(QueueBrowser.class);
        QueueBrowser testQueueBrowser = (QueueBrowser) mockQueueBrowser.proxy();
        mockSession.stubs().method("createBrowser").will(returnValue(testQueueBrowser));

        HJBSessionQueueBrowsers browsers = new HJBSessionQueueBrowsers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0", 0, browsers.createBrowser(0,
                                                                    testQueue));
        assertNotNull("got first browser", browsers.getBrowser(0, 0));
        assertEquals("Index should be 1", 1, browsers.createBrowser(0,
                                                                    testQueue,
                                                                    "test"));
        assertNotNull("got second browser", browsers.getBrowser(0, 1));
    }

    protected void setUp() throws Exception {
        super.setUp();
        initialiseMockBuilders();

        mockSession = new Mock(Session.class);
        testSession = (Session) mockSession.proxy();

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
        mockConnection.stubs().method("setExceptionListener");
        Connection aConnection = (Connection) mockConnection.proxy();
        testConnection = new HJBConnection(aConnection, 0);
    }

    private Mock mockConnection;
    private HJBConnection testConnection;
    private Mock mockQueue;
    private Queue testQueue;
    private Mock mockSession;
    private Session testSession;

    private MockConnectionBuilder connectionBuilder;
    private MockSessionBuilder sessionBuilder;

}
