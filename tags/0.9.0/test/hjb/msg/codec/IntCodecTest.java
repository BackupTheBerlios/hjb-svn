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

public class IntCodecTest extends TestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new IntCodec(),
                     new IntCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new IntCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        IntCodec testCodec = new IntCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testIsEncodedReturnsFalseForIncorrectlyFormattedStrings() {
        IntCodec testCodec = new IntCodec();
        for (int i = 0; i < NOT_OK_ENCODED_INTEGERS.length; i++) {
            assertFalse("should be false for " + NOT_OK_ENCODED_INTEGERS[i],
                        testCodec.isEncoded(NOT_OK_ENCODED_INTEGERS[i]));
        }
    }

    public void testIsEncodedReturnsTrueForCorrectlyFormattedStrings() {
        IntCodec testCodec = new IntCodec();
        for (int i = 0; i < OK_ENCODED_INTEGERS.length; i++) {
            assertTrue("should be true for " + OK_ENCODED_INTEGERS[i],
                       testCodec.isEncoded(OK_ENCODED_INTEGERS[i]));
        }
    }

    public void testDecodeReturnsNullForNulls() {
        IntCodec testCodec = new IntCodec();
        assertNull("should return null", testCodec.decode(null));
    }

    public void testDecodeThrowsOnPoorlyFormattedStrings() {
        IntCodec testCodec = new IntCodec();
        for (int i = 0; i < NOT_OK_ENCODED_INTEGERS.length; i++) {
            try {
                testCodec.decode(NOT_OK_ENCODED_INTEGERS[i]);
                fail("Should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testDecodeConvertsCorrectlyFormattedStrings() {
        IntCodec testCodec = new IntCodec();
        for (int i = 0; i < OK_ENCODED_INTEGERS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_INTEGERS[i]);
            assertTrue("incorrect object type created", (o instanceof Integer));
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_INTEGERS[i],
                         ((Integer) o).intValue(),
                         1e-10d);
        }
    }

    public void testEncodeThrowsHJBExceptionForNulls() {
        IntCodec testCodec = new IntCodec();
        try {
            testCodec.encode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testEncodeThrowsHJBExceptionForWrongTypes() {
        IntCodec testCodec = new IntCodec();
        Object[] wrongTypes = new Object[] {
                new Object(), new Float(1), new Double(2), new Boolean(false),
        };
        for (int i = 0; i < wrongTypes.length; i++) {
            try {
                testCodec.encode(wrongTypes[i]);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testEncodeWorksForCorrectTypes() {
        IntCodec testCodec = new IntCodec();
        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++) {
            String result = testCodec.encode(new Integer(EXPECTED_DECODED_INTEGERS[i]));
            assertEquals("encoding failed",
                         OK_EXPECTED_DECODED_INTEGERS[i],
                         result);
        }
    }

    private static final String NOT_OK_ENCODED_INTEGERS[] = CodecTestValues.NOT_OK_ENCODED_INTEGERS;
    private static final String OK_ENCODED_INTEGERS[] = CodecTestValues.OK_EXPECTED_DECODED_INTEGERS;
    private static final int EXPECTED_DECODED_INTEGERS[] = CodecTestValues.EXPECTED_DECODED_INTEGERS;
    private static final String OK_EXPECTED_DECODED_INTEGERS[] = CodecTestValues.OK_EXPECTED_DECODED_INTEGERS;

}
