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

public class LongCodecTest extends TestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new LongCodec(),
                     new LongCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new LongCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        LongCodec testCodec = new LongCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testIsEncodedReturnsFalseForIncorrectlyFormattedStrings() {
        LongCodec testCodec = new LongCodec();
        for (int i = 0; i < NOT_OK_ENCODED_LONGS.length; i++) {
            assertFalse("should be false for " + NOT_OK_ENCODED_LONGS[i],
                        testCodec.isEncoded(NOT_OK_ENCODED_LONGS[i]));
        }
    }

    public void testIsEncodedReturnsTrueForCorrectlyFormattedStrings() {
        LongCodec testCodec = new LongCodec();
        for (int i = 0; i < OK_ENCODED_LONGS.length; i++) {
            assertTrue("should be true for " + OK_ENCODED_LONGS[i],
                       testCodec.isEncoded(OK_ENCODED_LONGS[i]));
        }
    }

    public void testDecodeReturnsNullForNulls() {
        LongCodec testCodec = new LongCodec();
        assertNull("should return null", testCodec.decode(null));
    }

    public void testDecodeThrowsOnPoorlyFormattedStrings() {
        LongCodec testCodec = new LongCodec();
        for (int i = 0; i < NOT_OK_ENCODED_LONGS.length; i++) {
            try {
                testCodec.decode(NOT_OK_ENCODED_LONGS[i]);
                fail("Should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testDecodeConvertsCorrectlyFormattedStrings() {
        LongCodec testCodec = new LongCodec();
        for (int i = 0; i < OK_ENCODED_LONGS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_LONGS[i]);
            assertTrue("incorrect object type created", (o instanceof Long));
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_LONGS[i],
                         ((Long) o).longValue(),
                         1e-10d);
        }
    }

    public void testEncodeThrowsHJBExceptionForNulls() {
        LongCodec testCodec = new LongCodec();
        try {
            testCodec.encode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testEncodeThrowsHJBExceptionForWrongTypes() {
        LongCodec testCodec = new LongCodec();
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
        LongCodec testCodec = new LongCodec();
        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++) {
            String result = testCodec.encode(new Long(EXPECTED_DECODED_LONGS[i]));
            assertEquals("encoding failed",
                         OK_EXPECTED_DECODED_LONGS[i],
                         result);
        }
    }

    private static final String NOT_OK_ENCODED_LONGS[] = CodecTestValues.NOT_OK_ENCODED_LONGS;
    private static final String OK_ENCODED_LONGS[] = CodecTestValues.OK_EXPECTED_DECODED_LONGS;
    private static final long EXPECTED_DECODED_LONGS[] = CodecTestValues.EXPECTED_DECODED_LONGS;
    private static final String OK_EXPECTED_DECODED_LONGS[] = CodecTestValues.OK_EXPECTED_DECODED_LONGS;

}
