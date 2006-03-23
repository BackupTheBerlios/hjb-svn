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

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hjb.misc.HJBException;

/**
 * <code>LongCodec</code> is used to encode/decode between long property
 * values in JMS {@link javax.jms.Message}s and string values in
 * {@link hjb.msg.HJBMessage}s
 * 
 * @author Tim Emiola
 */
public class LongCodec extends BaseValueCodec {

    public boolean isEncoded(String value) {
        try {
            if (null == value) return false;
            Matcher m = getEncodingMatcher().matcher(value);
            boolean result = m.matches();
            if (result) Long.valueOf(m.group(1));
            return result;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public Object decode(String value) throws HJBException {
        try {
            if (null == value) return null;
            if (!isEncoded(value)) {
                handleDecodingFailure(value);
            }
            Matcher m = getEncodingMatcher().matcher(value);
            m.matches();
            return Long.valueOf(m.group(1));
        } catch (NumberFormatException e) {
            handleDecodingFailure(value, e);
            return null;
        }
    }

    public String encode(Object value) throws HJBException {
        if (!(value instanceof Long)) {
            return handleBadEncodedType(value);
        }
        return getEncoder().format(new Object[] {
            value
        });
    }

    protected long decodeAsLong(String value) throws HJBException {
        return ((Long) decode(value)).longValue();
    }

    protected Pattern getEncodingMatcher() {
        return ENCODING_MATCHER;
    }

    protected MessageFormat getEncoder() {
        return ENCODER;
    }

    protected static String LONG_REGEX = "[-+]?\\d+[lL]?";

    private static Pattern ENCODING_MATCHER = Pattern.compile("^\\s*\\(long\\s*("
            + LONG_REGEX + ")\\)\\s*$");

    private static MessageFormat ENCODER = new MessageFormat("(long {0,number,0})");

}
