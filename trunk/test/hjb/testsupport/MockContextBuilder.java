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

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.NamingException;

import org.jmock.Mock;
import org.jmock.core.constraint.StringContains;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;

public class MockContextBuilder {

    public Mock returnsEnvironment(Hashtable aHashtable) {
        Mock result = new Mock(Context.class);
        result.stubs().method("getEnvironment").will(new ReturnStub((new Hashtable(aHashtable))));
        result.stubs().method("lookup").will(new ReturnStub(null));
        return result;
    }

    public Mock lookupReturnsMatchingObject(final Hashtable aHashtable,
                                            final ConnectionFactory connectionFactory,
                                            final Destination destination) {
        Mock result = new Mock(Context.class);
        result.stubs().method("lookup").with(new StringContains("Destination")).will(new ReturnStub(destination));
        result.stubs().method("lookup").with(new StringContains("Factory")).will(new ReturnStub(connectionFactory));
        return result;
    }

    public Mock lookupReturnsConnectionFactory(final Hashtable aHashtable,
                                               final ConnectionFactory connectionFactory) {
        Mock result = new Mock(Context.class);
        result.stubs().method("lookup").will(new ReturnStub(connectionFactory));
        return result;
    }

    public Mock lookupReturnsDestination(final Hashtable aHashtable,
                                         final Destination destination) {
        Mock result = new Mock(Context.class);
        result.stubs().method("lookup").will(new ReturnStub(destination));
        return result;
    }

    public Mock lookupThrowsNamingException(final Hashtable aHashtable,
                                            final Object lookedUp) {
        Mock result = new Mock(Context.class);
        result.stubs().method("getEnvironment").will(new ReturnStub((new Hashtable(aHashtable))));
        result.stubs().method("lookup").will(new ThrowStub(new NamingException("thrown as a test")));
        return result;
    }

}
