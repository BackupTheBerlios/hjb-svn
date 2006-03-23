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

import java.util.Arrays;

import junit.framework.TestCase;

import hjb.misc.HJBException;

public class ByteArrayCodecTest extends TestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new ByteArrayCodec(),
                     new ByteArrayCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new ByteArrayCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        ByteArrayCodec testCodec = new ByteArrayCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testIsEncodedReturnsFalseForIncorrectlyFormattedStrings() {
        ByteArrayCodec testCodec = new ByteArrayCodec();
        for (int i = 0; i < NOT_OK_ENCODED_BARRAYS.length; i++) {
            assertFalse("should be false for " + NOT_OK_ENCODED_BARRAYS[i],
                        testCodec.isEncoded(NOT_OK_ENCODED_BARRAYS[i]));
        }
    }

    public void testIsEncodedReturnsTrueForCorrectlyFormattedStrings() {
        ByteArrayCodec testCodec = new ByteArrayCodec();
        for (int i = 0; i < OK_ENCODED_BARRAYS.length; i++) {
            assertTrue("should be true for " + OK_ENCODED_BARRAYS[i],
                       testCodec.isEncoded(OK_ENCODED_BARRAYS[i]));
        }
    }

    public void testDecodeReturnsNullForNulls() {
        ByteArrayCodec testCodec = new ByteArrayCodec();
        assertNull("should return null", testCodec.decode(null));
    }

    public void testDecodeThrowsOnPoorlyFormattedStrings() {
        ByteArrayCodec testCodec = new ByteArrayCodec();
        for (int i = 0; i < NOT_OK_ENCODED_BARRAYS.length; i++) {
            try {
                testCodec.decode(NOT_OK_ENCODED_BARRAYS[i]);
                fail("Should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testDecodeConvertsCorrectlyFormattedStrings() {
        ByteArrayCodec testCodec = new ByteArrayCodec();
        for (int i = 0; i < OK_ENCODED_BARRAYS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_BARRAYS[i]);
            assertTrue("incorrect object type created", (o instanceof byte[]));
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_BARRAYS[i],
                         (byte[]) o);
        }
    }

    public void testEncodeThrowsHJBExceptionForNulls() {
        ByteArrayCodec testCodec = new ByteArrayCodec();
        try {
            testCodec.encode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testEncodeThrowsHJBExceptionForWrongTypes() {
        ByteArrayCodec testCodec = new ByteArrayCodec();
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
        ByteArrayCodec testCodec = new ByteArrayCodec();
        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++) {
            String result = testCodec.encode(EXPECTED_DECODED_BARRAYS[i]);
            assertEquals("encoding failed",
                         OK_EXPECTED_DECODED_BARRAYS[i],
                         result);
        }
    }

    protected void assertEquals(String message, byte[] expected, byte[] actual) {
        assertTrue(message, Arrays.equals(expected, actual));
    }

    private static final String NOT_OK_ENCODED_BARRAYS[] = CodecTestValues.NOT_OK_ENCODED_BARRAYS;
    private static final String OK_ENCODED_BARRAYS[] = CodecTestValues.OK_EXPECTED_DECODED_BARRAYS;
    private static final byte[] EXPECTED_DECODED_BARRAYS[] = CodecTestValues.EXPECTED_DECODED_BARRAYS;
    private static final String OK_EXPECTED_DECODED_BARRAYS[] = CodecTestValues.OK_EXPECTED_DECODED_BARRAYS;
}
