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
package hjb.http.cmd;

import hjb.misc.HJBStrings;

/**
 * <code>PathNaming</code> provides access to the values used in HJB URLs.
 * 
 * @author Tim Emiola
 */
public class PathNaming {

    private static final HJBStrings STRINGS = new HJBStrings();
    public static final String DESTINATION = STRINGS.getString(HJBStrings.HJB_PATH_DESTINATION);
    public static final String CONNECTION = STRINGS.getString(HJBStrings.HJB_PATH_CONNECTION);
    public static final String SESSION = STRINGS.getString(HJBStrings.HJB_PATH_SESSION);
    public static final String CONSUMER = STRINGS.getString(HJBStrings.HJB_PATH_CONSUMER);
    public static final String PRODUCER = STRINGS.getString(HJBStrings.HJB_PATH_PRODUCER);
    public static final String BROWSER = STRINGS.getString(HJBStrings.HJB_PATH_BROWSER);
    public static final String SUBSCRIBER = STRINGS.getString(HJBStrings.HJB_PATH_SUBSCRIBER);

}
