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
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockSessionBuilder;

public class HJBSessionProducersTest extends MockObjectTestCase {

    public HJBSessionProducersTest() {

    }

    public void testHJBSessionProducersThrowsIllegalArgumentExceptionOnNullConnection() {
        try {
            new HJBSessionProducers(null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testCreateProducerThrowsIfSessionIsNotThere() throws Exception {
        Mock mockProducer = mock(MessageProducer.class);
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        mockSession.stubs()
            .method("createProducer")
            .will(returnValue(testProducer));
        HJBSessionProducers producers = new HJBSessionProducers(testConnection);
        try {
            producers.createProducer(0,
                                     testDestination,
                                     sessionBuilder.defaultProducerArguments());
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            producers.createProducer(1,
                                     testDestination,
                                     sessionBuilder.defaultProducerArguments());
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testCreateProducerThrowsHJBExceptionOnJMSException()
            throws Exception {
        mockSession = sessionBuilder.createMockSessionThatThrowsJMSOn("createProducer");
        Session aSession = (Session) mockSession.proxy();
        updateConnectionMock(aSession);

        HJBSessionProducers producers = new HJBSessionProducers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            producers.createProducer(0,
                                     testDestination,
                                     sessionBuilder.defaultProducerArguments());
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testCreateProducerIsInvokedOnCorrectSession() throws Exception {
        Mock mockProducer = mock(MessageProducer.class);
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        mockSession.stubs()
            .method("createProducer")
            .will(returnValue(testProducer));
        mockProducer.expects(atLeastOnce())
            .method("setDisableMessageTimestamp");
        mockProducer.expects(atLeastOnce()).method("setDisableMessageID");
        registerToVerify(mockSession);
        testSession = (Session) mockSession.proxy();
        updateConnectionMock(testSession);

        HJBSessionProducers producers = new HJBSessionProducers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        producers.createProducer(0,
                                 testDestination,
                                 sessionBuilder.defaultProducerArguments());
        assertEquals("there should be one producer for the session",
                     1,
                     producers.getProducers(0).length);
    }

    public void testGetProducersThrowsIfSessionIsNotThere() throws Exception {
        HJBSessionProducers producers = new HJBSessionProducers(testConnection);
        try {
            producers.getProducers(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetProducersReturnsEmptyForNewEmptySessions()
            throws Exception {
        HJBSessionProducers producers = new HJBSessionProducers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals(0, producers.getProducers(0).length);
    }

    public void testGetProducerThrowsIfSessionIsNotThere() throws Exception {
        HJBSessionProducers producers = new HJBSessionProducers(testConnection);
        try {
            producers.getProducer(0, 0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetProducerThrowsForNewEmptySessions() throws Exception {
        HJBSessionProducers producers = new HJBSessionProducers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            producers.getProducer(0, 0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetProducerThrowsForInvalidProducer() throws Exception {
        Mock mockMessageProducer = new Mock(MessageProducer.class);
        MessageProducer testMessageProducer = (MessageProducer) mockMessageProducer.proxy();
        mockSession.stubs()
            .method("createProducer")
            .will(returnValue(testMessageProducer));
        mockMessageProducer.expects(atLeastOnce())
            .method("setDisableMessageTimestamp");
        mockMessageProducer.expects(atLeastOnce())
            .method("setDisableMessageID");

        HJBSessionProducers producers = new HJBSessionProducers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     producers.createProducer(0,
                                              testDestination,
                                              sessionBuilder.defaultProducerArguments()));
        try {
            producers.getProducer(0, 1);
            fail("should have thrown an exception - producer 1 does not exist yet");
        } catch (HJBException hjbe) {}
    }

    public void testGetProducerReturnsCreatedProducers() throws Exception {
        Mock mockMessageProducer = new Mock(MessageProducer.class);
        MessageProducer testMessageProducer = (MessageProducer) mockMessageProducer.proxy();
        mockSession.stubs()
            .method("createProducer")
            .will(returnValue(testMessageProducer));
        mockMessageProducer.expects(atLeastOnce())
            .method("setDisableMessageTimestamp");
        mockMessageProducer.expects(atLeastOnce())
            .method("setDisableMessageID");

        HJBSessionProducers producers = new HJBSessionProducers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     producers.createProducer(0,
                                              testDestination,
                                              sessionBuilder.defaultProducerArguments()));
        assertNotNull("got first producer", producers.getProducer(0, 0));
        assertEquals("Index should be 1",
                     1,
                     producers.createProducer(0,
                                              testDestination,
                                              sessionBuilder.defaultProducerArguments()));
        assertNotNull("got second producer", producers.getProducer(0, 1));
    }

    protected void setUp() throws Exception {
        super.setUp();
        initialiseMockBuilders();

        mockSession = new MockSessionBuilder().createMockSession();
        registerToVerify(mockSession);
        testSession = (Session) mockSession.proxy();

        mockDestination = new Mock(Destination.class);
        testDestination = (Destination) mockDestination.proxy();

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
        testConnection = new HJBConnection(aConnection, 0);
    }

    private Mock mockConnection;
    private HJBConnection testConnection;
    private Mock mockDestination;
    private Destination testDestination;
    private Mock mockSession;
    private Session testSession;

    private MockConnectionBuilder connectionBuilder;
    private MockSessionBuilder sessionBuilder;

}
