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

import java.util.ArrayList;

import javax.jms.JMSException;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;

public class OrderedTypedValueCodecTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new OrderedTypedValueCodec(),
                     new OrderedTypedValueCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new OrderedTypedValueCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        OrderedTypedValueCodec testCodec = new OrderedTypedValueCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testthrowsJMSExceptionOnMethodsDecodeThrowsHJBExceptionForNulls() {
        OrderedTypedValueCodec testCodec = new OrderedTypedValueCodec();
        try {
            testCodec.decode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testDecodeConvertsCorrectlyFormattedStrings() {
        OrderedTypedValueCodec testCodec = new OrderedTypedValueCodec();
        for (int i = 0; i < OK_ENCODED_BYTES.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_BYTES[i]);
            assertEquals("object type not correct for " + OK_ENCODED_BYTES[i],
                         Byte.class.getName(),
                         o.getClass().getName());
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_BYTES[i],
                         ((Byte) o).byteValue());
        }
        for (int i = 0; i < OK_ENCODED_DOUBLES.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_DOUBLES[i]);
            assertEquals("object type not correct for " + OK_ENCODED_DOUBLES[i],
                         Double.class.getName(),
                         o.getClass().getName());
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_DOUBLES[i],
                         ((Double) o).doubleValue(),
                         1e-10);
        }
        for (int i = 0; i < OK_ENCODED_FLOATS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_FLOATS[i]);
            assertEquals("object type not correct for " + OK_ENCODED_FLOATS[i],
                         Float.class.getName(),
                         o.getClass().getName());
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_FLOATS[i],
                         ((Float) o).floatValue(),
                         Math.max(((Float) o).floatValue() / 1e6, 1e-9));
        }
        for (int i = 0; i < OK_ENCODED_INTEGERS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_INTEGERS[i]);
            assertEquals("object type not correct for "
                                 + OK_ENCODED_INTEGERS[i],
                         Integer.class.getName(),
                         o.getClass().getName());
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_INTEGERS[i],
                         ((Integer) o).intValue());
        }
        for (int i = 0; i < OK_ENCODED_SHORTS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_SHORTS[i]);
            assertEquals("object type not correct for " + OK_ENCODED_SHORTS[i],
                         Short.class.getName(),
                         o.getClass().getName());
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_SHORTS[i],
                         ((Short) o).shortValue());
        }
        for (int i = 0; i < OK_ENCODED_BOOLEANS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_BOOLEANS[i]);
            assertEquals("object type not correct for "
                                 + OK_ENCODED_BOOLEANS[i],
                         Boolean.class.getName(),
                         o.getClass().getName());
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_BOOLEANS[i],
                         ((Boolean) o).booleanValue());
        }
        for (int i = 0; i < OK_ENCODED_CHARS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_CHARS[i]);
            assertEquals("object type not correct for " + OK_ENCODED_CHARS[i],
                         Character.class.getName(),
                         o.getClass().getName());
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_CHARS[i],
                         ((Character) o).charValue());
        }
        for (int i = 0; i < OK_ENCODED_BARRAYS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_BARRAYS[i]);
            assertEquals("object type not correct for " + OK_ENCODED_BARRAYS[i],
                         byte[].class.getName(),
                         o.getClass().getName());
            assertEquals("incorrect value retrieved",
                         new String(EXPECTED_DECODED_BARRAYS[i]),
                         new String(((byte[]) o)));
        }

    }

    protected void throwsJMSExceptionOnMethods(Mock aMock, String[] methodNames) {
        for (int i = 0; i < methodNames.length; i++) {
            aMock.expects(atLeastOnce()).method(methodNames[i]).will(throwException(new JMSException("thrown as a test")));
        }
    }

    protected void throwsJMSExceptionOnMethods(Mock aMock, ArrayList methodNames) {
        throwsJMSExceptionOnMethods(aMock,
                                    (String[]) methodNames.toArray(new String[0]));
    }

    private static final String[] OK_ENCODED_BYTES = CodecTestValues.OK_ENCODED_BYTES;

    private static final byte[] EXPECTED_DECODED_BYTES = CodecTestValues.EXPECTED_DECODED_BYTES;

    private static final String[] OK_ENCODED_BOOLEANS = CodecTestValues.OK_ENCODED_BOOLEANS;

    private static final boolean[] EXPECTED_DECODED_BOOLEANS = CodecTestValues.EXPECTED_DECODED_BOOLEANS;

    private static final String[] OK_ENCODED_DOUBLES = CodecTestValues.OK_ENCODED_DOUBLES;

    private static final double[] EXPECTED_DECODED_DOUBLES = CodecTestValues.EXPECTED_DECODED_DOUBLES;

    private static final String[] OK_ENCODED_FLOATS = CodecTestValues.OK_ENCODED_FLOATS;

    private static final float[] EXPECTED_DECODED_FLOATS = CodecTestValues.EXPECTED_DECODED_FLOATS;

    private static final String[] OK_ENCODED_INTEGERS = CodecTestValues.OK_ENCODED_INTEGERS;

    private static final int[] EXPECTED_DECODED_INTEGERS = CodecTestValues.EXPECTED_DECODED_INTEGERS;

    private static final String[] OK_ENCODED_SHORTS = CodecTestValues.OK_ENCODED_SHORTS;

    private static final short[] EXPECTED_DECODED_SHORTS = CodecTestValues.EXPECTED_DECODED_SHORTS;

    private static final String[] OK_ENCODED_CHARS = CodecTestValues.OK_ENCODED_CHARS;

    private static final char[] EXPECTED_DECODED_CHARS = CodecTestValues.EXPECTED_DECODED_CHARS;

    private static final String[] OK_ENCODED_BARRAYS = CodecTestValues.OK_ENCODED_BARRAYS;

    private static final byte[][] EXPECTED_DECODED_BARRAYS = CodecTestValues.EXPECTED_DECODED_BARRAYS;

}
