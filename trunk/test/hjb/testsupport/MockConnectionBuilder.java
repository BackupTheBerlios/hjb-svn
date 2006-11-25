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

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;
import javax.jms.Session;

import org.jmock.Mock;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;

public class MockConnectionBuilder {

    public MockConnectionBuilder() {
        sessionBuilder = new MockSessionBuilder();
    }

    public Mock createMockConnection() {
        return createMockConnection((Session) sessionBuilder.createMockSession()
            .proxy());
    }

    public Mock createMockConnection(Session testSession) {
        Mock result = new Mock(Connection.class);
        result.stubs().method("setExceptionListener");
        result.stubs().method("getClientID").will(new ReturnStub(null));
        result.stubs()
            .method("createSession")
            .will(new ReturnStub(testSession));
        ConnectionMetaData testMetaData = (ConnectionMetaData) createConnectionMetaDataMock().proxy();
        result.stubs().method("getMetaData").will(new ReturnStub(testMetaData));

        return result;
    }

    public Mock createMockConnectionReturnedByConnectionFactory() {
        Mock result = new Mock(Connection.class);
        result.stubs().method("setExceptionListener");
        result.stubs().method("getClientID").will(new ReturnStub(null));
        ConnectionMetaData testMetaData = (ConnectionMetaData) createConnectionMetaDataMock().proxy();
        result.stubs().method("getMetaData").will(new ReturnStub(testMetaData));
        result.stubs()
            .method("createSession")
            .will(new ReturnStub((Session) sessionBuilder.createMockSession()
                .proxy()));
        return result;
    }

    public Mock createMockConnectionThatThrowsJMSOn(String methodName) {
        Mock result = new Mock(Connection.class);
        result.stubs()
            .method(methodName)
            .will(new ThrowStub(new JMSException("thrown as a test")));
        result.stubs().method("setExceptionListener");
        return result;
    }

    public Mock createConnectionMetaDataMock() {
        Mock mockMetaData = new Mock(ConnectionMetaData.class);
        for (int i = 0; i < METADATA_TEST_VALUES.length; i++) {
            mockMetaData.stubs()
                .method("" + METADATA_TEST_VALUES[i][0])
                .will(new ReturnStub(METADATA_TEST_VALUES[i][1]));
        }
        String[] fakeCustomMetaDataNames = {
                "fakeMetaData1", "fakeMetaData2"
        };
        Enumeration fakeNames = Collections.enumeration(Arrays.asList(fakeCustomMetaDataNames));
        mockMetaData.stubs()
            .method("getJMSXPropertyNames")
            .will(new ReturnStub(fakeNames));
        return mockMetaData;
    }

    public Object[][] METADATA_TEST_VALUES = {
            {
                    "getJMSVersion", "testVersion"
            }, {
                    "getJMSProviderName", "testProviderName"
            }, {
                    "getJMSMajorVersion", new Integer(444)
            }, {
                    "getJMSMinorVersion", new Integer(777)
            }, {
                    "getProviderMajorVersion", new Integer(999)
            }, {
                    "getProviderMinorVersion", new Integer(999)
            }, {
                    "getProviderVersion", "testProviderVersion"
            },
    };

    private MockSessionBuilder sessionBuilder;
}
