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

import javax.jms.Connection;
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
        Mock result = new Mock(Connection.class);
        result.stubs().method("createSession").will(new ReturnStub(sessionBuilder.createMockSession()));
        return result;
    }

    public Mock createMockConnection(Session testSession) {
        Mock result = new Mock(Connection.class);
        result.stubs().method("setExceptionListener");
        result.stubs().method("createSession").will(new ReturnStub(testSession));
        return result;
    }

    public Mock createMockConnectionReturnedByConnectionFactory() {
        Mock result = new Mock(Connection.class);
        result.stubs().method("setExceptionListener");
        result.stubs().method("createSession").will(new ReturnStub(sessionBuilder.createMockSession()));
        return result;
    }

    public Mock createMockConnectionThatThrowsJMSOn(String methodName) {
        Mock result = new Mock(Connection.class);
        result.stubs().method(methodName).will(new ThrowStub(new JMSException("thrown as a test")));
        result.stubs().method("setExceptionListener");
        return result;
    }

    private MockSessionBuilder sessionBuilder;
}
