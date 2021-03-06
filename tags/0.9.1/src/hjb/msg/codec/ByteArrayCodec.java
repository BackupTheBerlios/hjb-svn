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
import hjb.misc.YetAnotherBase64;

/**
 * <code>ByteArrayCodec</code> is used to encode/decode between byte array
 * values in JMS {@link javax.jms.Message}s and encoded strings in
 * {@link hjb.msg.HJBMessage}s.
 * 
 * <p>
 * This is implemented by encoding/decoding the byte array using base64 encoding
 * as described in http://www.ietf.org/rfc/rfc3548.txt.
 * </p>
 * 
 * <p>
 * An encoded <code>byte[]</code> has the following form:
 * 
 * <pre>
 *    (base64 &lt;encodedarraywithoutnewlines&gt;)
 * </pre>
 * 
 * </p>
 * 
 * @author Tim Emiola
 */
public class ByteArrayCodec extends BaseValueCodec {

    public boolean isEncoded(String value) {
        try {
            if (null == value) return false;
            Matcher m = getEncodingMatcher().matcher(value);
            boolean result = m.matches();
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
            return decodeAsByteArray(m.group(1));
        } catch (NumberFormatException e) {
            handleDecodingFailure(value, e);
            return null;
        }
    }

    public String encode(Object value) throws HJBException {
        if (!(value instanceof byte[])) {
            return handleBadEncodedType(value);
        }
        String result = getBase64er().encode((byte[]) value);
        return getEncoder().format(new Object[] {
            result
        });
    }

    protected byte[] decodeAsByteArray(String value) throws HJBException {
        return getBase64er().decode(value);
    }

    protected Pattern getEncodingMatcher() {
        return ENCODING_MATCHER;
    }

    protected MessageFormat getEncoder() {
        return ENCODER;
    }

    protected YetAnotherBase64 getBase64er() {
        return BASE64ER;
    }

    private static YetAnotherBase64 BASE64ER = new YetAnotherBase64();

    protected static String ALLOW_ALL_REGEX = "(.*?)";

    private static final Pattern ENCODING_MATCHER = Pattern.compile("^\\s*\\(base64\\s*("
            + ALLOW_ALL_REGEX + ")\\)\\s*$");

    private static final MessageFormat ENCODER = new MessageFormat("(base64 {0})");
}
