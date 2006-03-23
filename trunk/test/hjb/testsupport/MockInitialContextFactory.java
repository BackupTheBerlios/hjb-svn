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
/**
 * 
 */
package hjb.testsupport;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * An <code>InitialContextFactory</code> implementation that allows the
 * retrieved <code>Context</code> to be mocked.
 */
public class MockInitialContextFactory implements InitialContextFactory {

    public Context getInitialContext(Hashtable environment)
            throws NamingException {
        return (Context) sharedMock.getCurrentMock().proxy();
    }

    private SharedMock sharedMock = SharedMock.getInstance();

}