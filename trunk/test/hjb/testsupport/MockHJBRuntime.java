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
package hjb.testsupport;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;
import javax.naming.Context;

import org.jmock.Mock;

import hjb.http.HJBConstants;
import hjb.jms.HJBProvider;
import hjb.jms.HJBRoot;

public class MockHJBRuntime {

    public void make1SessionAnd1Destination(HJBRoot root,
                                            String providerName,
                                            String factoryName,
                                            String destinationName,
                                            Destination testDestination) {
        MockConnectionFactoryBuilder factoryBuilder = new MockConnectionFactoryBuilder();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory();
        ConnectionFactory testFactory = (ConnectionFactory) mockFactory.proxy();

        Hashtable testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        MockContextBuilder contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.lookupReturnsMatchingObject(testEnvironment,
                                                                         testFactory,
                                                                         testDestination);
        SharedMock sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);
        updateProvider(root, testEnvironment, providerName);
        root.getProvider(providerName).registerConnectionFactory(factoryName);
        root.getProvider(providerName).registerDestination(destinationName);
        root.getProvider(providerName)
            .getConnectionFactory(factoryName)
            .createConnection();
        root.getProvider(providerName)
            .getConnectionFactory(factoryName)
            .getConnection(0)
            .createSession(HJBConstants.DEFAULT_TRANSACTED,
                           HJBConstants.DEFAULT_ACKNOWLEDGEMENT_MODE);
    }

    public void make1SessionAnd1Destination(HJBRoot root,
                                            Session testSession,
                                            String providerName,
                                            String factoryName,
                                            String destinationName,
                                            Destination testDestination) {
        MockConnectionBuilder connectionBuilder = new MockConnectionBuilder();
        Mock mockConnection = connectionBuilder.createMockConnection(testSession);
        MockConnectionFactoryBuilder factoryBuilder = new MockConnectionFactoryBuilder();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory((Connection) mockConnection.proxy());
        ConnectionFactory testFactory = (ConnectionFactory) mockFactory.proxy();

        Hashtable testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        MockContextBuilder contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.lookupReturnsMatchingObject(testEnvironment,
                                                                         testFactory,
                                                                         testDestination);
        SharedMock sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);
        updateProvider(root, testEnvironment, providerName);
        root.getProvider(providerName).registerConnectionFactory(factoryName);
        root.getProvider(providerName).registerDestination(destinationName);
        root.getProvider(providerName)
            .getConnectionFactory(factoryName)
            .createConnection();
        root.getProvider(providerName)
            .getConnectionFactory(factoryName)
            .getConnection(0)
            .createSession(HJBConstants.DEFAULT_TRANSACTED,
                           HJBConstants.DEFAULT_ACKNOWLEDGEMENT_MODE);
    }

    public void make1Destination(HJBRoot root,
                                 String providerName,
                                 String destinationName,
                                 Destination testDestination) {
        MockConnectionFactoryBuilder factoryBuilder = new MockConnectionFactoryBuilder();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory();
        ConnectionFactory testFactory = (ConnectionFactory) mockFactory.proxy();

        Hashtable testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        MockContextBuilder contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.lookupReturnsMatchingObject(testEnvironment,
                                                                         testFactory,
                                                                         testDestination);
        SharedMock sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);
        updateProvider(root, testEnvironment, providerName);
        root.getProvider(providerName).registerDestination(destinationName);
    }

    public void make1Session(HJBRoot root,
                             String providerName,
                             String factoryName) {
        make1Connection(root, providerName, factoryName);
        root.getProvider(providerName)
            .getConnectionFactory(factoryName)
            .getConnection(0)
            .createSession(false, 0);
    }

    public void make1Session(HJBRoot root,
                             Session testSession,
                             String providerName,
                             String factoryName) {
        make1Connection(root, testSession, providerName, factoryName);
        root.getProvider(providerName)
            .getConnectionFactory(factoryName)
            .getConnection(0)
            .createSession(false, 0);
    }

    public void make1Connection(HJBRoot root,
                                String providerName,
                                String factoryName) {
        make1Factory(root, providerName, factoryName);
        root.getProvider(providerName)
            .getConnectionFactory(factoryName)
            .createConnection();
    }

    public void make1Connection(HJBRoot root,
                                Session testSession,
                                String providerName,
                                String factoryName) {
        MockConnectionBuilder connectionBuilder = new MockConnectionBuilder();
        Mock mockConnection = connectionBuilder.createMockConnection(testSession);
        Connection testConnection = (Connection) mockConnection.proxy();
        make1Factory(root, testConnection, providerName, factoryName);
        root.getProvider(providerName)
            .getConnectionFactory(factoryName)
            .createConnection();
    }

    public void make1Connection(HJBRoot root,
                                Connection testConnection,
                                String providerName,
                                String factoryName) {
        make1Factory(root, testConnection, providerName, factoryName);
        root.getProvider(providerName)
            .getConnectionFactory(factoryName)
            .createConnection();
    }

    public void make1Factory(HJBRoot root,
                             ConnectionFactory testFactory,
                             String providerName,
                             String factoryName) {
        Hashtable testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        MockContextBuilder contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.lookupReturnsConnectionFactory(testEnvironment,
                                                                            testFactory);

        SharedMock sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);
        updateProvider(root, testEnvironment, providerName);
        root.getProvider(providerName).registerConnectionFactory(factoryName);        
    }
    
    public void make1Factory(HJBRoot root,
                             Connection testConnection,
                             String providerName,
                             String factoryName) {
        MockConnectionFactoryBuilder factoryBuilder = new MockConnectionFactoryBuilder();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory(testConnection);
        ConnectionFactory testFactory = (ConnectionFactory) mockFactory.proxy();

        Hashtable testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        MockContextBuilder contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.lookupReturnsConnectionFactory(testEnvironment,
                                                                            testFactory);

        SharedMock sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);
        updateProvider(root, testEnvironment, providerName);
        root.getProvider(providerName).registerConnectionFactory(factoryName);
    }

    public void make1Factory(HJBRoot root,
                             String providerName,
                             String factoryName) {
        make1Provider(root, providerName);
        root.getProvider(providerName).registerConnectionFactory(factoryName);
    }

    public void make1Provider(HJBRoot root, String providerName) {
        MockConnectionFactoryBuilder factoryBuilder = new MockConnectionFactoryBuilder();
        Mock mockFactory = factoryBuilder.createMockConnectionFactory();
        ConnectionFactory testFactory = (ConnectionFactory) mockFactory.proxy();

        Mock mockDestination = new Mock(Destination.class);
        Destination testDestination = (Destination) mockDestination.proxy();

        Hashtable testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        MockContextBuilder contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.lookupReturnsMatchingObject(testEnvironment,
                                                                         testFactory,
                                                                         testDestination);

        SharedMock sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);
        updateProvider(root, testEnvironment, providerName);
    }

    public void make1ProviderWithContextThatThrows(HJBRoot root, String providerName, Exception exception) {
        Hashtable testEnvironment = new Hashtable();
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        testEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "hjb.testsupport.MockInitialContextFactory");
        MockContextBuilder contextBuilder = new MockContextBuilder();
        Mock newContextMock = contextBuilder.lookupThrows(exception);

        SharedMock sharedMock = SharedMock.getInstance();
        sharedMock.setCurrentMock(newContextMock);
        updateProvider(root, testEnvironment, providerName);
    }

    public void updateProvider(HJBRoot root,
                               Hashtable testEnvironment,
                               String providerName) {
        testEnvironment.put(HJBProvider.HJB_PROVIDER_NAME, providerName);
        root.addProvider(testEnvironment);
    }
}
