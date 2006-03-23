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

public class ByteCodecTest extends TestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new ByteCodec(),
                     new ByteCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new ByteCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        ByteCodec testCodec = new ByteCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testIsEncodedReturnsFalseForIncorrectlyFormattedStrings() {
        ByteCodec testCodec = new ByteCodec();
        for (int i = 0; i < NOT_OK_ENCODED_BYTES.length; i++) {
            assertFalse("should be false for " + NOT_OK_ENCODED_BYTES[i],
                        testCodec.isEncoded(NOT_OK_ENCODED_BYTES[i]));
        }
    }

    public void testIsEncodedReturnsTrueForCorrectlyFormattedStrings() {
        ByteCodec testCodec = new ByteCodec();
        for (int i = 0; i < OK_ENCODED_BYTES.length; i++) {
            assertTrue("should be true for " + OK_ENCODED_BYTES[i],
                       testCodec.isEncoded(OK_ENCODED_BYTES[i]));
        }
    }

    public void testDecodeReturnsNullForNulls() {
        ByteCodec testCodec = new ByteCodec();
        assertNull("should return null", testCodec.decode(null));
    }

    public void testDecodeThrowsOnPoorlyFormattedStrings() {
        ByteCodec testCodec = new ByteCodec();
        for (int i = 0; i < NOT_OK_ENCODED_BYTES.length; i++) {
            try {
                testCodec.decode(NOT_OK_ENCODED_BYTES[i]);
                fail("Should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testDecodeConvertsCorrectlyFormattedStrings() {
        ByteCodec testCodec = new ByteCodec();
        for (int i = 0; i < OK_ENCODED_BYTES.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_BYTES[i]);
            assertTrue("object type not correct" + o.getClass().getName(),
                       (o instanceof Byte));
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_BYTES[i],
                         ((Byte) o).byteValue());
        }
    }

    public void testEncodeThrowsHJBExceptionForNulls() {
        ByteCodec testCodec = new ByteCodec();
        try {
            testCodec.encode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testEncodeThrowsHJBExceptionForWrongTypes() {
        ByteCodec testCodec = new ByteCodec();
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
        ByteCodec testCodec = new ByteCodec();
        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++) {
            String result = testCodec.encode(new Byte(EXPECTED_DECODED_BYTES[i]));
            assertEquals("encoding failed",
                         OK_EXPECTED_DECODED_BYTES[i],
                         result);
        }
    }

    private static final String NOT_OK_ENCODED_BYTES[] = CodecTestValues.NOT_OK_ENCODED_BYTES;
    private static final String OK_ENCODED_BYTES[] = CodecTestValues.OK_EXPECTED_DECODED_BYTES;
    private static final byte EXPECTED_DECODED_BYTES[] = CodecTestValues.EXPECTED_DECODED_BYTES;
    private static final String OK_EXPECTED_DECODED_BYTES[] = CodecTestValues.OK_EXPECTED_DECODED_BYTES;
}
