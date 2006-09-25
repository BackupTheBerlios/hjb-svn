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
import hjb.testsupport.DecoraterMock;
import hjb.testsupport.MockSessionBuilder;

public class HJBConnectionTest extends BaseHJBTestCase {

    public void testConstructionSetsExceptionListener() {
        mockConnection.reset();
        mockConnection.expects(once()).method("setExceptionListener");
        new HJBConnection(((Connection) mockConnection.proxy()),
                          0,
                          defaultTestClock());
    }

    public void testConstructionThrowsIllegalArgumentExceptionOnNullClock() {
        try {
            Connection testConnection = (Connection) mockConnection.proxy();
            new HJBConnection(testConnection, 0, null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }
    
    public void testConstructionSetsCreationTimeCorrectly() {
        Connection testConnection = (Connection) mockConnection.proxy();
        HJBConnection c = new HJBConnection(testConnection, 0, defaultTestClock());
        assertEquals(c.getCreationTime(), defaultTestClock().getCurrentTime());
    }

    public void testConstructionThrowsIllegalArgumentExceptionOnNullConnection() {
        try {
            new HJBConnection(null, 0, defaultTestClock());
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testCreateSessionInvokesDecorateeConnection() {
        Mock mockSession = sessionBuilder.createMockSession();
        registerToVerify(mockSession);
        mockConnection.expects(once())
            .method("createSession")
            .will(returnValue((Session) mockSession.proxy()));
        testConnection.createSession(true, 1);
    }

    public void testCreateSessionAddsANewCommandRunner() {
        Mock mockSession = sessionBuilder.createMockSession();
        registerToVerify(mockSession);
        mockConnection.expects(once())
            .method("createSession")
            .will(returnValue((Session) mockSession.proxy()));
        try {
            testConnection.getSessionCommandRunner(0);
            fail("should throw exception");
        } catch (IndexOutOfBoundsException e) {}
        testConnection.createSession(true, 1);
        assertNotNull("should exist", testConnection.getSessionCommandRunner(0));
        try {
            testConnection.getSessionCommandRunner(1);
            fail("should throw exception");
        } catch (IndexOutOfBoundsException e) {}
        testConnection.createSession(true, 1);
        assertNotNull("should exist", testConnection.getSessionCommandRunner(1));
    }

    public void testGetSessions() {
        Mock mockSession = sessionBuilder.createMockSession();
        registerToVerify(mockSession);
        mockConnection.expects(once())
            .method("createSession")
            .will(returnValue((Session) mockSession.proxy()));
        testConnection.createSession(true, 1);
        assertEquals("1 session should have been added",
                     1,
                     testConnection.getActiveSessions().size());
        testConnection.createSession(true, 1);
        assertEquals("2 sessions should have been added",
                     2,
                     testConnection.getActiveSessions().size());
    }

    public void testCreateSessionThrowsHJBExceptionOnJMSException() {
        mockConnection.expects(once())
            .method("createSession")
            .will(throwException(new JMSException("thrown as a test")));
        try {
            testConnection.createSession(true, 1);
            fail("An HJBException should have been thrown");
        } catch (HJBException e) {}
    }

    public void testGetClientIDInvokesDecorateeConnection() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("getClientID");
    }

    public void testGetClientIDPropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("getClientID",
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testSetClientIDInvokesDecorateeInstance() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("setClientID", new Object[] {
            "test"
        }, new Class[] {
            String.class
        });
    }

    public void testSetClientIDPropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("setClientID",
                                                            new Object[] {
                                                                "test"
                                                            },
                                                            new Class[] {
                                                                String.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testGetMetaDataInvokesDecorateeConnection() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("getMetaData");
    }

    public void testGetMetaDataPropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("getMetaData",
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testGetExceptionListenerNeverInvokesDecorateeConnection() throws Exception {
        mockConnection.expects(never()).method("getExceptionListener");
        testConnection.getExceptionListener();
    }

    public void testStartInvokesDecorateeConnection() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("start");
    }

    public void testStartPropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("start",
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testStopInvokesDecorateeConnection() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("stop");
    }

    public void testStopPropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("stop",
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCloseInvokesDecorateeConnection() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("close");
    }

    public void testClosePropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("close",
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testSetExceptionListenerNeverInvokesDecorateeConnection() {
        testConnection.setExceptionListener(null);
        testConnection.setExceptionListener(null);
        mockConnection.expects(never()).method("setExceptionListener");
    }

    public void testCreateConnectionConsumerAlwaysThrowsHJBException() {
        mockConnection.expects(never()).method("createConnectionConsumer");
        try {
            testConnection.createConnectionConsumer(null, null, null, 0);
            fail("An HJBException should have been thrown");
        } catch (HJBException e) {}
    }

    public void testCreateDurableConnectionConsumerAlwaysThrowsHJBException() {
        mockConnection.expects(never())
            .method("createDurableConnectionConsumer");
        try {
            testConnection.createDurableConnectionConsumer(null,
                                                           null,
                                                           null,
                                                           null,
                                                           0);
            fail("An HJBException should have been thrown");
        } catch (HJBException e) {}
    }

    protected void setUp() throws Exception {
        super.setUp();
        connectionBuilder = new MockConnectionBuilder();
        sessionBuilder = new MockSessionBuilder();
        mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);

        testConnection = new HJBConnection(((Connection) mockConnection.proxy()),
                                           0,
                                           defaultTestClock());
        decoraterMock = new DecoraterMock(mockConnection, testConnection);
    }

    private Mock mockConnection;
    private HJBConnection testConnection;
    private MockConnectionBuilder connectionBuilder;
    private MockSessionBuilder sessionBuilder;
    private DecoraterMock decoraterMock;
}
