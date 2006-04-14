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

import junit.framework.TestCase;

import hjb.misc.HJBException;

public class FloatCodecTest extends TestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new FloatCodec(),
                     new FloatCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new FloatCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        FloatCodec testCodec = new FloatCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testIsEncodedReturnsFalseForIncorrectlyFormattedStrings() {
        FloatCodec testCodec = new FloatCodec();
        for (int i = 0; i < NOT_OK_ENCODED_FLOATS.length; i++) {
            assertFalse("should be false for " + NOT_OK_ENCODED_FLOATS[i],
                        testCodec.isEncoded(NOT_OK_ENCODED_FLOATS[i]));
        }
    }

    public void testIsEncodedReturnsTrueForCorrectlyFormattedStrings() {
        FloatCodec testCodec = new FloatCodec();
        for (int i = 0; i < OK_ENCODED_FLOATS.length; i++) {
            assertTrue("should be true for " + OK_ENCODED_FLOATS[i],
                       testCodec.isEncoded(OK_ENCODED_FLOATS[i]));
        }
    }

    public void testDecodeReturnsNullForNulls() {
        FloatCodec testCodec = new FloatCodec();
        assertNull("should return null", testCodec.decode(null));
    }

    public void testDecodeThrowsOnPoorlyFormattedStrings() {
        FloatCodec testCodec = new FloatCodec();
        for (int i = 0; i < NOT_OK_ENCODED_FLOATS.length; i++) {
            try {
                testCodec.decode(NOT_OK_ENCODED_FLOATS[i]);
                fail("Should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testDecodeConvertsCorrectlyFormattedStrings() {
        FloatCodec testCodec = new FloatCodec();
        for (int i = 0; i < OK_ENCODED_FLOATS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_FLOATS[i]);
            assertTrue("incorrect object type created", (o instanceof Float));
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_FLOATS[i],
                         ((Float) o).floatValue(),
                         Math.max(((Float) o).floatValue() / 1e6, 1e-9));
        }
    }

    public void testEncodeThrowsHJBExceptionForNulls() {
        FloatCodec testCodec = new FloatCodec();
        try {
            testCodec.encode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testEncodeThrowsHJBExceptionForWrongTypes() {
        FloatCodec testCodec = new FloatCodec();
        Object[] wrongTypes = new Object[] {
                new Object(),
                new Integer(1),
                new Double(2),
                new Boolean(false),
        };
        for (int i = 0; i < wrongTypes.length; i++) {
            try {
                testCodec.encode(wrongTypes[i]);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testEncodeWorksForCorrectTypes() {
        FloatCodec testCodec = new FloatCodec();
        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++) {
            String result = testCodec.encode(new Float(EXPECTED_DECODED_FLOATS[i]));
            assertEquals("encoding failed",
                         OK_EXPECTED_DECODED_FLOATS[i],
                         result);
        }
    }

    private static final String NOT_OK_ENCODED_FLOATS[] = CodecTestValues.NOT_OK_ENCODED_FLOATS;
    private static final String OK_ENCODED_FLOATS[] = CodecTestValues.OK_EXPECTED_DECODED_FLOATS;
    private static final float EXPECTED_DECODED_FLOATS[] = CodecTestValues.EXPECTED_DECODED_FLOATS;
    private static final String OK_EXPECTED_DECODED_FLOATS[] = CodecTestValues.OK_EXPECTED_DECODED_FLOATS;

}
