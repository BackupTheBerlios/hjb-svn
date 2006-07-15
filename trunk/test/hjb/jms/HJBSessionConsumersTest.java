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
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockSessionBuilder;

public class HJBSessionConsumersTest extends MockObjectTestCase {

    public void testHJBSessionConsumersThrowsIllegalArgumentExceptionOnNullConnection() {
        try {
            new HJBSessionConsumers(null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
    }

    public void testCreateConsumerThrowsIfSessionIsNotThere() throws Exception {
        mockSession.stubs().method("createConsumer");
        HJBSessionConsumers consumers = new HJBSessionConsumers(testConnection);
        try {
            consumers.createConsumer(0, testDestination);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            consumers.createConsumer(1, testDestination, "test-selector");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        try {
            consumers.createConsumer(1, testDestination, "test-selector", true);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        assertEquals("Index should be 0",
                     0,
                     consumers.createConsumer(0, testDestination));
        assertEquals("Index should be 1",
                     1,
                     consumers.createConsumer(0,
                                              testDestination,
                                              "test-selector"));
        assertEquals("Index should be 2",
                     2,
                     consumers.createConsumer(0,
                                              testDestination,
                                              "test-selector",
                                              false));
    }

    public void testCreateConsumerThrowsHJBExceptionOnJMSException()
            throws Exception {
        mockSession = sessionBuilder.createMockSessionThatThrowsJMSOn("createConsumer");
        Session aSession = (Session) mockSession.proxy();
        updateConnectionMock(aSession);

        HJBSessionConsumers consumers = new HJBSessionConsumers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            consumers.createConsumer(0, testDestination);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            consumers.createConsumer(0, testDestination, "test-selector");
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            consumers.createConsumer(0, testDestination, "test-selector", false);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testCreateConsumerIsInvokedOnStoredSession() throws Exception {
        mockSession.expects(atLeastOnce()).method("createConsumer");
        registerToVerify(mockSession);
        testSession = (Session) mockSession.proxy();
        updateConnectionMock(testSession);

        HJBSessionConsumers consumers = new HJBSessionConsumers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     consumers.createConsumer(0,
                                              testDestination,
                                              "test-selector",
                                              true));
        assertEquals("there should be 1 consumers",
                     1,
                     consumers.getConsumers(0).length);
        assertEquals("Index should be 1",
                     1,
                     consumers.createConsumer(0,
                                              testDestination,
                                              "test-selector"));
        assertEquals("there should be 2 consumers",
                     2,
                     consumers.getConsumers(0).length);
        assertEquals("Index should be 2",
                     2,
                     consumers.createConsumer(0,
                                              testDestination,
                                              "test-selector",
                                              true));
        assertEquals("there should be 3 consumers",
                     3,
                     consumers.getConsumers(0).length);
    }

    public void testGetConsumersThrowsIfSessionIsNotThere() throws Exception {
        HJBSessionConsumers consumers = new HJBSessionConsumers(testConnection);
        try {
            consumers.getConsumers(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetConsumersReturnsEmptyForNewEmptySessions()
            throws Exception {
        HJBSessionConsumers consumers = new HJBSessionConsumers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals(0, consumers.getConsumers(0).length);
    }

    public void testGetConsumerThrowsIfSessionIsNotThere() throws Exception {
        HJBSessionConsumers consumers = new HJBSessionConsumers(testConnection);
        try {
            consumers.getConsumer(0, 0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetConsumerThrowsForNewEmptySessions() throws Exception {
        HJBSessionConsumers consumers = new HJBSessionConsumers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        try {
            consumers.getConsumer(0, 0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetConsumerThrowsForInvalidConsumer() throws Exception {
        Mock mockMessageConsumer = new Mock(MessageConsumer.class);
        MessageConsumer testMessageConsumer = (MessageConsumer) mockMessageConsumer.proxy();
        mockSession.stubs()
            .method("createConsumer")
            .will(returnValue(testMessageConsumer));

        HJBSessionConsumers consumers = new HJBSessionConsumers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        consumers.createConsumer(0, testDestination);
        try {
            consumers.getConsumer(0, 1);
            fail("should have thrown an exception - consumer 1 does not exist yet");
        } catch (HJBException hjbe) {}
    }

    public void testGetConsumerReturnsCreatedConsumers() throws Exception {
        Mock mockMessageConsumer = new Mock(MessageConsumer.class);
        MessageConsumer testMessageConsumer = (MessageConsumer) mockMessageConsumer.proxy();
        mockSession.stubs()
            .method("createConsumer")
            .will(returnValue(testMessageConsumer));

        HJBSessionConsumers consumers = new HJBSessionConsumers(testConnection);
        testConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals("Index should be 0",
                     0,
                     consumers.createConsumer(0, testDestination));
        assertNotNull("got first consumer", consumers.getConsumer(0, 0));
        assertEquals("Index should be 1",
                     1,
                     consumers.createConsumer(0, testDestination));
        assertNotNull("got second consumer", consumers.getConsumer(0, 1));
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
