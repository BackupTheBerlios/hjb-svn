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
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.jmock.Mock;

import hjb.misc.HJBException;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockSessionBuilder;

public class HJBSessionConsumersTest extends BaseHJBTestCase {

    public void testHJBSessionConsumersThrowsIllegalArgumentExceptionOnNullSession() {
        try {
            new HJBSessionConsumers(null, defaultTestClock());
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testHJBSessionConsumersThrowsIllegalArgumentExceptionOnNullClock() {
        try {
            HJBSession aSession = new HJBSession((Session) mockSession.proxy(),
                                                 0,
                                                 defaultTestClock());
            new HJBSessionConsumers(aSession, null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testCreateConsumerThrowsHJBExceptionOnJMSException()
            throws Exception {
        mockSession = sessionBuilder.createMockSessionThatThrowsJMSOn("createConsumer");
        HJBSession aSession = new HJBSession((Session) mockSession.proxy(),
                                             0,
                                             defaultTestClock());
        updateConnectionMock(aSession);

        HJBSessionConsumers consumers = new HJBSessionConsumers(aSession,
                                                                defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            consumers.createConsumer(testDestination);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            consumers.createConsumer(testDestination, "test-selector");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            consumers.createConsumer(testDestination, "test-selector", false);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testCreateConsumerIsInvokedOnStoredSession() throws Exception {
        mockSession.expects(atLeastOnce()).method("createConsumer");
        registerToVerify(mockSession);
        testSession = new HJBSession((Session) mockSession.proxy(),
                                     0,
                                     defaultTestClock());
        updateConnectionMock(testSession);

        HJBSessionConsumers consumers = new HJBSessionConsumers(testSession,
                                                                defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     consumers.createConsumer(testDestination,
                                              "test-selector",
                                              true));
        assertEquals("there should be 1 consumers",
                     1,
                     consumers.asArray().length);
        assertEquals("Index should be 1",
                     1,
                     consumers.createConsumer(testDestination, "test-selector"));
        assertEquals("there should be 2 consumers",
                     2,
                     consumers.asArray().length);
        assertEquals("Index should be 2",
                     2,
                     consumers.createConsumer(testDestination,
                                              "test-selector",
                                              true));
        assertEquals("there should be 3 consumers",
                     3,
                     consumers.asArray().length);
    }

    public void testShouldReturnTheCorrectNumberOfItemDescriptions() throws Exception {
        testSession = new HJBSession((Session) mockSession.proxy(),
                                     0,
                                     defaultTestClock());
        updateConnectionMock(testSession);

        HJBSessionConsumers consumers = new HJBSessionConsumers(testSession,
                                                                defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals(0, consumers.getItemDescriptions().length);
        consumers.createConsumer(testDestination, "test-selector", true);
        assertEquals(1, consumers.getItemDescriptions().length);
        consumers.createConsumer(testDestination, "test-selector");
        assertEquals(2, consumers.getItemDescriptions().length);
    }

    public void testGetConsumerThrowsForInvalidConsumer() throws Exception {
        Mock mockMessageConsumer = new Mock(MessageConsumer.class);
        MessageConsumer testMessageConsumer = (MessageConsumer) mockMessageConsumer.proxy();
        mockSession.stubs()
            .method("createConsumer")
            .will(returnValue(testMessageConsumer));

        HJBSessionConsumers consumers = new HJBSessionConsumers(testSession,
                                                                defaultTestClock());
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        consumers.createConsumer(testDestination);
        try {
            consumers.getConsumer(1);
            fail("should have thrown an exception - consumer 1 does not exist yet");
        } catch (HJBException hjbe) {}
    }

    public void testGetConsumerReturnsCreatedConsumers() throws Exception {
        Mock mockMessageConsumer = new Mock(MessageConsumer.class);
        MessageConsumer testMessageConsumer = (MessageConsumer) mockMessageConsumer.proxy();
        mockSession.stubs()
            .method("createConsumer")
            .will(returnValue(testMessageConsumer));

        HJBSessionConsumers consumers = new HJBSessionConsumers(testSession,
                                                                defaultTestClock());
        assertEquals("Index should be 0",
                     0,
                     consumers.createConsumer(testDestination));
        assertNotNull("got first consumer", consumers.getConsumer(0));
        assertEquals("Index should be 1",
                     1,
                     consumers.createConsumer(testDestination));
        assertNotNull("got second consumer", consumers.getConsumer(1));
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
