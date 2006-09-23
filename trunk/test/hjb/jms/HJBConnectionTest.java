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
import javax.jms.JMSException;
import javax.jms.Session;

import org.jmock.Mock;

import hjb.misc.HJBException;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockSessionBuilder;

public class HJBConnectionTest extends BaseHJBTestCase {

    public void testHJBConnectionSetsExceptionListenerOnConstruction() {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        Connection testConnection = (Connection) mockConnection.proxy();
        new HJBConnection(testConnection, 0, defaultTestClock());
    }

    public void testHJBConnectionThrowsIllegalArgumentExceptionOnNullClock() {
        try {
            Mock mockConnection = connectionBuilder.createMockConnection();
            registerToVerify(mockConnection);
            Connection testConnection = (Connection) mockConnection.proxy();
            new HJBConnection(testConnection, 0, null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testHJBConnectionThrowsIllegalArgumentExceptionOnNullConnection() {
        try {
            new HJBConnection(null, 0, defaultTestClock());
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }


    public void testCreateSessionInvokesDecorateeConnection() {
        Mock mockConnection = connectionBuilder.createMockConnection();
        Mock mockSession = sessionBuilder.createMockSession();
        registerToVerify(mockConnection);
        registerToVerify(mockSession);
        mockConnection.expects(once())
            .method("createSession")
            .will(returnValue((Session) mockSession.proxy()));
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        h.createSession(true, 1);
    }

    public void testCreateSessionAddsANewCommandRunner() {
        Mock mockConnection = connectionBuilder.createMockConnection();
        Mock mockSession = sessionBuilder.createMockSession();
        registerToVerify(mockConnection);
        registerToVerify(mockSession);
        mockConnection.expects(once())
            .method("createSession")
            .will(returnValue((Session) mockSession.proxy()));

        HJBConnection h = new HJBConnection((Connection) mockConnection.proxy(),
                                            0,
                                            defaultTestClock());
        try {
            h.getSessionCommandRunner(0);
            fail("should throw exception");
        } catch (IndexOutOfBoundsException e) {}
        h.createSession(true, 1);
        assertNotNull("should exist", h.getSessionCommandRunner(0));
        try {
            h.getSessionCommandRunner(1);
            fail("should throw exception");
        } catch (IndexOutOfBoundsException e) {}
        h.createSession(true, 1);
        assertNotNull("should exist", h.getSessionCommandRunner(1));
    }

    public void testGetSessions() {
        Mock mockConnection = connectionBuilder.createMockConnection();
        Mock mockSession = sessionBuilder.createMockSession();
        registerToVerify(mockConnection);
        registerToVerify(mockSession);
        mockConnection.expects(once())
            .method("createSession")
            .will(returnValue((Session) mockSession.proxy()));

        HJBConnection h = new HJBConnection((Connection) mockConnection.proxy(),
                                            0,
                                            defaultTestClock());
        h.createSession(true, 1);
        assertEquals("1 session should have been added",
                     1,
                     h.getActiveSessions().size());
        h.createSession(true, 1);
        assertEquals("2 sessions should have been added",
                     2,
                     h.getActiveSessions().size());
    }

    public void testCreateSessionThrowsHJBExceptionOnJMSException() {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once())
            .method("createSession")
            .will(throwException(new JMSException("thrown as a test")));
        Connection testConnection = (Connection) mockConnection.proxy();

        try {
            HJBConnection h = new HJBConnection(testConnection,
                                                0,
                                                defaultTestClock());
            h.createSession(true, 1);
            fail("An HJBException should have been thrown");
        } catch (HJBException e) {}
    }

    public void testGetClientIDInvokesDecorateeConnection() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        h.getClientID();
    }

    public void testGetClientIDPropagatesJMSException() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once())
            .method("getClientID")
            .will(throwException(new JMSException("thrown as a test")));
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        try {
            h.getClientID();
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testSetClientIDInvokesDecorateeInstance() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("setClientID");
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        h.setClientID("test");
    }

    public void testSetClientIDPropagatesJMSException() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once())
            .method("setClientID")
            .will(throwException(new JMSException("thrown as a test")));
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        try {
            h.setClientID("test");
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testGetMetaDataInvokesDecorateeConnection() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("getMetaData");
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        h.getMetaData();
    }

    public void testGetMetaDataPropagatesJMSException() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once())
            .method("getMetaData")
            .will(throwException(new JMSException("thrown as test")));
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        try {
            h.getMetaData();
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testGetExceptionListenerInvokesDecorateeConnection()
            throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("getExceptionListener");
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        h.getExceptionListener();
    }

    public void testGetExceptionListenerPropagatesJMSException()
            throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once())
            .method("getExceptionListener")
            .will(throwException(new JMSException("thrown as test")));
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        try {
            h.getExceptionListener();
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testSetExceptionListenerNeverInvokesDecorateeConnection() {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        h.setExceptionListener(null);
        h.setExceptionListener(null);
    }

    public void testStartInvokesDecorateeConnection() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("start");
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        h.start();
    }

    public void testStartPropagatesJMSException() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once())
            .method("start")
            .will(throwException(new JMSException("thrown as test")));
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        try {
            h.start();
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testStopInvokesDecorateeConnection() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("stop");
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        h.stop();
    }

    public void testStopPropagatesJMSException() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once())
            .method("stop")
            .will(throwException(new JMSException("thrown as test")));
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        try {
            h.stop();
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCloseInvokesDecorateeConnection() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("close");
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        h.close();
    }

    public void testClosePropagatesJMSException() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(once())
            .method("close")
            .will(throwException(new JMSException("thrown as test")));
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        try {
            h.close();
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCreateConnectionConsumerAlwaysThrowsHJBException() {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(never()).method("createConnectionConsumer");
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        try {
            h.createConnectionConsumer(null, null, null, 0);
            fail("An HJBException should have been thrown");
        } catch (HJBException e) {}
    }

    public void testCreateDurableConnectionConsumerAlwaysThrowsHJBException() {
        Mock mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        mockConnection.expects(never())
            .method("createDurableConnectionConsumer");
        Connection testConnection = (Connection) mockConnection.proxy();

        HJBConnection h = new HJBConnection(testConnection,
                                            0,
                                            defaultTestClock());
        try {
            h.createDurableConnectionConsumer(null, null, null, null, 0);
            fail("An HJBException should have been thrown");
        } catch (HJBException e) {}
    }

    protected void setUp() throws Exception {
        super.setUp();
        connectionBuilder = new MockConnectionBuilder();
        sessionBuilder = new MockSessionBuilder();
    }

    private MockConnectionBuilder connectionBuilder;
    private MockSessionBuilder sessionBuilder;

}
