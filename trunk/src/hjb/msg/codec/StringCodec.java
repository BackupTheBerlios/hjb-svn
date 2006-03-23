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
package hjb.msg.codec;

import hjb.misc.HJBException;

/**
 * <code>StringCodec</code> is used to encode/decode between
 * <code>String</code> property values in JMS {@link javax.jms.Message}s and
 * their string representations in {@link hjb.msg.HJBMessage}s
 * 
 * @author Tim Emiola
 */
public class StringCodec extends BaseValueCodec {

    public boolean isEncoded(String value) {
        return (null != value);
    }

    public Object decode(String value) {
        return value;
    }

    public String encode(Object value) throws HJBException {
        if (null == value) {
            return handleBadEncodedType(value);
        }
        return value.toString();
    }

}
