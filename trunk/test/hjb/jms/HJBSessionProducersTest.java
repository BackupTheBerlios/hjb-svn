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

import hjb.misc.HJBException;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockSessionBuilder;

public class HJBSessionProducersTest extends BaseHJBTestCase {

    public void testConstructionThrowsIllegalArgumentExceptionOnNullSession() {
        try {
            new HJBSessionProducers(null, defaultTestClock());
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructionThrowsIllegalArgumentExceptionOnNullClock() {
        try {
            HJBSession aSession = new HJBSession((Session) mockSession.proxy(),
                                                 0,
                                                 defaultTestClock());
            new HJBSessionProducers(aSession, null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testCreateProducerThrowsHJBExceptionOnJMSException()
            throws Exception {
        mockSession = sessionBuilder.createMockSessionThatThrowsJMSOn("createProducer");
        HJBSession aSession = new HJBSession((Session) mockSession.proxy(),
                                             0,
                                             defaultTestClock());
        updateConnectionMock(aSession);

        HJBSessionProducers producers = new HJBSessionProducers(aSession,
                                                                defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            producers.createProducer(testDestination,
                                     sessionBuilder.defaultProducerArguments());
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

        HJBSessionProducers producers = new HJBSessionProducers(testSession,
                                                                defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     producers.createProducer(testDestination,
                                              sessionBuilder.defaultProducerArguments()));
        try {
            producers.getProducer(1);
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

        HJBSessionProducers producers = new HJBSessionProducers(testSession,
                                                                defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     producers.createProducer(testDestination,
                                              sessionBuilder.defaultProducerArguments()));
        assertNotNull("did not find first producer", producers.getProducer(0));
        assertEquals("Index should be 1",
                     1,
                     producers.createProducer(testDestination,
                                              sessionBuilder.defaultProducerArguments()));
        assertNotNull("did not find second producer", producers.getProducer(1));
    }

    public void testShouldReturnTheCorrectNumberOfItemDescriptions()
            throws Exception {
        Mock mockMessageProducer = new Mock(MessageProducer.class);
        MessageProducer testMessageProducer = (MessageProducer) mockMessageProducer.proxy();
        mockSession.stubs()
            .method("createProducer")
            .will(returnValue(testMessageProducer));
        mockMessageProducer.expects(atLeastOnce())
            .method("setDisableMessageTimestamp");
        mockMessageProducer.expects(atLeastOnce())
            .method("setDisableMessageID");

        HJBSessionProducers producers = new HJBSessionProducers(testSession,
                                                                defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals(0, producers.getItemDescriptions().length);
        producers.createProducer(testDestination,
                                 sessionBuilder.defaultProducerArguments());
        assertEquals(1, producers.getItemDescriptions().length);
        producers.createProducer(testDestination,
                                 sessionBuilder.defaultProducerArguments());
        assertEquals(2, producers.getItemDescriptions().length);
    }

    protected void setUp() throws Exception {
        super.setUp();
        initialiseMockBuilders();

        mockSession = new MockSessionBuilder().createMockSession();
        registerToVerify(mockSession);
        testSession = new HJBSession((Session) mockSession.proxy(),
                                     0,
                                     defaultTestClock());

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
        testConnection = new HJBConnection(aConnection, 0, defaultTestClock());
    }

    private Mock mockConnection;
    private HJBConnection testConnection;
    private Mock mockDestination;
    private Destination testDestination;
    private Mock mockSession;
    private HJBSession testSession;

    private MockConnectionBuilder connectionBuilder;
    private MockSessionBuilder sessionBuilder;

}
