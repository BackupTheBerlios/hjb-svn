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
package hjb.misc;

/**
 * <code>HJBException</code> is used to report any general problems that would
 * stop processing of a request by HJB.
 * 
 * @author Tim Emiola
 */
public class HJBException extends RuntimeException {

    public HJBException() {
        super(STRINGS.getString(HJBStrings.UNKNOWN_CAUSE));
    }

    public HJBException(String message) {
        super(message);
    }

    public HJBException(String message, Throwable cause) {
        super(message, cause);
    }

    public HJBException(Throwable cause) {
        super(STRINGS.getString(HJBStrings.UNKNOWN_CAUSE), cause);
    }

    private static final HJBStrings STRINGS = new HJBStrings();
    private static final long serialVersionUID = 3039293308900380544L;
}
