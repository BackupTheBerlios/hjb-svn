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

public class DoubleCodecTest extends TestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new DoubleCodec(),
                     new DoubleCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new DoubleCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        DoubleCodec testCodec = new DoubleCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testIsEncodedReturnsFalseForIncorrectlyFormattedStrings() {
        DoubleCodec testCodec = new DoubleCodec();
        for (int i = 0; i < NOT_OK_ENCODED_DOUBLES.length; i++) {
            assertFalse("should be false for " + NOT_OK_ENCODED_DOUBLES[i],
                        testCodec.isEncoded(NOT_OK_ENCODED_DOUBLES[i]));
        }
    }

    public void testIsEncodedReturnsTrueForCorrectlyFormattedStrings() {
        DoubleCodec testCodec = new DoubleCodec();
        for (int i = 0; i < OK_ENCODED_DOUBLES.length; i++) {
            assertTrue("should be true for " + OK_ENCODED_DOUBLES[i],
                       testCodec.isEncoded(OK_ENCODED_DOUBLES[i]));
        }
    }

    public void testDecodeReturnsNullForNulls() {
        DoubleCodec testCodec = new DoubleCodec();
        assertNull("should return null", testCodec.decode(null));
    }

    public void testDecodeThrowsOnPoorlyFormattedStrings() {
        DoubleCodec testCodec = new DoubleCodec();
        for (int i = 0; i < NOT_OK_ENCODED_DOUBLES.length; i++) {
            try {
                testCodec.decode(NOT_OK_ENCODED_DOUBLES[i]);
                fail("Should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testDecodeConvertsCorrectlyFormattedStrings() {
        DoubleCodec testCodec = new DoubleCodec();
        for (int i = 0; i < OK_ENCODED_DOUBLES.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_DOUBLES[i]);
            assertTrue("incorrect object type created", (o instanceof Double));
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_DOUBLES[i],
                         ((Double) o).doubleValue(),
                         1e-10d);
        }
    }

    public void testEncodeThrowsHJBExceptionForNulls() {
        DoubleCodec testCodec = new DoubleCodec();
        try {
            testCodec.encode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testEncodeThrowsHJBExceptionForWrongTypes() {
        DoubleCodec testCodec = new DoubleCodec();
        Object[] wrongTypes = new Object[] {
                new Object(), new Float(1), new Integer(2), new Boolean(false),
        };
        for (int i = 0; i < wrongTypes.length; i++) {
            try {
                testCodec.encode(wrongTypes[i]);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testEncodeWorksForCorrectTypes() {
        DoubleCodec testCodec = new DoubleCodec();
        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++) {
            String result = testCodec.encode(new Double(EXPECTED_DECODED_DOUBLES[i]));
            assertEquals("encoding failed",
                         OK_EXPECTED_DECODED_DOUBLES[i],
                         result);
        }
    }

    private static final String NOT_OK_ENCODED_DOUBLES[] = CodecTestValues.NOT_OK_ENCODED_DOUBLES;
    private static final String OK_ENCODED_DOUBLES[] = CodecTestValues.OK_EXPECTED_DECODED_DOUBLES;
    private static final double EXPECTED_DECODED_DOUBLES[] = CodecTestValues.EXPECTED_DECODED_DOUBLES;
    private static final String OK_EXPECTED_DECODED_DOUBLES[] = CodecTestValues.OK_EXPECTED_DECODED_DOUBLES;

}
