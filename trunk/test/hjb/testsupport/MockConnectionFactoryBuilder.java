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
import javax.jms.ConnectionFactory;

import org.jmock.Mock;
import org.jmock.core.stub.ReturnStub;

public class MockConnectionFactoryBuilder {

    public MockConnectionFactoryBuilder() {
        connectionBuilder = new MockConnectionBuilder();
    }

    public Mock createMockConnectionFactory() {
        return createMockConnectionFactory((Connection) connectionBuilder.createMockConnectionReturnedByConnectionFactory().proxy());
    }

    public Mock createMockConnectionFactory(Connection aConnection) {
        Mock result = new Mock(ConnectionFactory.class);
        result.stubs().method("createConnection").will(new ReturnStub(aConnection));
        return result;
    }

    private MockConnectionBuilder connectionBuilder;
}
