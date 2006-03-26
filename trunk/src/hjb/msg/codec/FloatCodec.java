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
 * <code>FloatCodec</code> is used to encode/decode between float property
 * values in JMS {@link javax.jms.Message}s and string values in
 * {@link hjb.msg.HJBMessage}s
 * 
 * @author Tim Emiola
 */
public class FloatCodec extends BaseValueCodec {

    public boolean isEncoded(String value) {
        try {
            if (null == value) return false;
            Matcher m = getEncodingMatcher().matcher(value);
            boolean result = m.matches();
            if (result) Float.valueOf(m.group(1));
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
            return Float.valueOf(m.group(1));
        } catch (NumberFormatException e) {
            handleDecodingFailure(value, e);
            return null;
        }
    }

    public String encode(Object value) throws HJBException {
        if (!(value instanceof Float)) {
            return handleBadEncodedType(value);
        }
        return getEncoder().format(new Object[] {
            value
        });
    }

    protected float decodeAsFloat(String value) throws HJBException {
        return ((Float) decode(value)).floatValue();
    }

    protected Pattern getEncodingMatcher() {
        return ENCODING_MATCHER;
    }

    protected MessageFormat getEncoder() {
        return ENCODER;
    }

    protected static String FLOATING_POINT_REGEX = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?[fFdD]?";

    private static final Pattern ENCODING_MATCHER = Pattern.compile("^\\s*\\(float\\s*("
            + FLOATING_POINT_REGEX + ")\\)\\s*$");

    private static final MessageFormat ENCODER = new MessageFormat("(float {0,number,##0.###E0})");

}
