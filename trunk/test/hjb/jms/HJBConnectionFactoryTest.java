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

import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.testsupport.MockConnectionBuilder;
import hjb.testsupport.MockConnectionFactoryBuilder;

public class HJBConnectionFactoryTest extends MockObjectTestCase {

    public void testCannotConstructWithNull() throws Exception {
        try {
            new HJBConnectionFactory(null);
            fail("Allowed creation from a null ConnectionFactory");
        } catch (IllegalArgumentException iae) {}
    }

    public void testCanConstructFromAConnectionFactory() {
        try {
            new HJBConnectionFactory(testConnectionFactory);
        } catch (Exception e) {
            fail("Disallowed creation from a valid ConnectionFactory");
        }
    }

    public void testCreateConnectionAddsToConnectionsOnSuccess()
            throws Exception {
        HJBConnectionFactory rcf = new HJBConnectionFactory(testConnectionFactory);
        rcf.createConnection();
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be two connections", 2, rcf.getActiveConnections()
            .size());
    }

    public void testCreateHJBConnectionAddsToConnectionsOnSuccess()
            throws Exception {
        HJBConnectionFactory rcf = new HJBConnectionFactory(testConnectionFactory);
        assertEquals("index should be 0", 0, rcf.createHJBConnection());
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        assertEquals("index should be 1",
                     1,
                     rcf.createHJBConnection("testUser", "testPassword"));
        assertEquals("should be two connections", 2, rcf.getActiveConnections()
            .size());
    }

    public void testGetConnectionsIsACopy() throws Exception {
        HJBConnectionFactory rcf = new HJBConnectionFactory(testConnectionFactory);
        rcf.createConnection();
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        Map savedConnections = rcf.getActiveConnections();
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, savedConnections.size());
        assertEquals("should be two connections", 2, rcf.getActiveConnections()
            .size());
    }

    public void testCreateConnectionThrowsHJBExceptionOnJMSException()
            throws Exception {
        Mock mockFactory = mock(ConnectionFactory.class);
        mockFactory.stubs()
            .method("createConnection")
            .will(throwException(new JMSException("thrown as a test")));
        testConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(testConnectionFactory);
        try {
            rcf.createConnection();
            fail("Expected JMS exception was not thrown");
        } catch (HJBException hjbe) {}
        assertEquals("should be no connections", 0, rcf.getActiveConnections()
            .size());
        try {
            rcf.createConnection("testUser", "testPassword");
            fail("Expected JMS exception was not thrown");
        } catch (HJBException hjbe) {}
        assertEquals("should be no connections", 0, rcf.getActiveConnections()
            .size());
        try {
            rcf.createHJBConnection();
            fail("Expected JMS exception was not thrown");
        } catch (HJBException hjbe) {}
        assertEquals("should be no connections", 0, rcf.getActiveConnections()
            .size());
        try {
            rcf.createHJBConnection("testUser", "testPassword");
            fail("Expected JMS exception was not thrown");
        } catch (HJBException hjbe) {}
        assertEquals("should be no connections", 0, rcf.getActiveConnections()
            .size());
    }

    public void testRemoveConnectionsStopsAndClosesThem() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionReturnedByConnectionFactory();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("stop");
        mockConnection.expects(once()).method("close");
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        rcf.removeConnections();
        assertEquals("should be no connections", 0, rcf.getActiveConnections()
            .size());
    }

    public void testDeleteConnectionStopsAndClosesThem() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionReturnedByConnectionFactory();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("stop");
        mockConnection.expects(once()).method("close");
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        rcf.deleteConnection(0);
        assertEquals("should be no connections", 0, rcf.getActiveConnections()
            .size());
    }

    public void testStartConnection() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionReturnedByConnectionFactory();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("start");
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        rcf.startConnection(0);
    }

    public void testStartConnectionThrowsIfConnectionIsNotThere()
            throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionReturnedByConnectionFactory();
        registerToVerify(mockConnection);
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        try {
            rcf.startConnection(-1);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            rcf.startConnection(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        try {
            rcf.startConnection(1);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testJMSExceptionOnStartConnectionThrows() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionThatThrowsJMSOn("start");
        registerToVerify(mockConnection);
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        try {
            rcf.startConnection(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testStopConnection() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionReturnedByConnectionFactory();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("stop");
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        rcf.stopConnection(0);
    }

    public void testStopConnectionThrowsIfConnectionIsNotThere()
            throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionReturnedByConnectionFactory();
        registerToVerify(mockConnection);
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        try {
            rcf.stopConnection(-1);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            rcf.stopConnection(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        try {
            rcf.stopConnection(1);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetMetaData() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionReturnedByConnectionFactory();
        registerToVerify(mockConnection);
        mockConnection.expects(once()).method("getMetaData");
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        rcf.getConnectionMetaData(0);
    }

    public void testJMSExceptionOnStopConnectionThrows() throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionThatThrowsJMSOn("stop");
        registerToVerify(mockConnection);
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        try {
            rcf.stopConnection(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testGetConnectionMetaDataThrowsIfConnectionIsNotThere()
            throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionReturnedByConnectionFactory();
        registerToVerify(mockConnection);
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        try {
            rcf.getConnectionMetaData(-1);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
        try {
            rcf.getConnectionMetaData(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}

        rcf.createConnection("testUser", "testPassword");
        try {
            rcf.getConnectionMetaData(1);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    public void testJMSExceptionOnGetConnectionMetaDataThrows()
            throws Exception {
        Mock mockConnection = connectionBuilder.createMockConnectionThatThrowsJMSOn("getMetaData");
        registerToVerify(mockConnection);
        Connection testConnection = (Connection) mockConnection.proxy();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory aConnectionFactory = (ConnectionFactory) mockFactory.proxy();

        HJBConnectionFactory rcf = new HJBConnectionFactory(aConnectionFactory);
        rcf.createConnection("testUser", "testPassword");
        assertEquals("should be one connection", 1, rcf.getActiveConnections()
            .size());
        try {
            rcf.getConnectionMetaData(0);
            fail("should have thrown an exception");
        } catch (HJBException hjbe) {}
    }

    protected void setUp() throws Exception {
        super.setUp();
        connectionBuilder = new MockConnectionBuilder();
        factoryBuilder = new MockConnectionFactoryBuilder();
        testConnectionFactory = (ConnectionFactory) factoryBuilder.createMockConnectionFactory()
            .proxy();
    }

    private MockConnectionBuilder connectionBuilder;
    private MockConnectionFactoryBuilder factoryBuilder;
    private ConnectionFactory testConnectionFactory;

}
