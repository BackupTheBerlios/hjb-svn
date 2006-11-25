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

public class ShortCodecTest extends TestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new ShortCodec(),
                     new ShortCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new ShortCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        ShortCodec testCodec = new ShortCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testIsEncodedReturnsFalseForIncorrectlyFormattedStrings() {
        ShortCodec testCodec = new ShortCodec();
        for (int i = 0; i < NOT_OK_ENCODED_SHORTS.length; i++) {
            assertFalse("should be false for " + NOT_OK_ENCODED_SHORTS[i],
                        testCodec.isEncoded(NOT_OK_ENCODED_SHORTS[i]));
        }
    }

    public void testIsEncodedReturnsTrueForCorrectlyFormattedStrings() {
        ShortCodec testCodec = new ShortCodec();
        for (int i = 0; i < OK_ENCODED_SHORTS.length; i++) {
            assertTrue("should be true for " + OK_ENCODED_SHORTS[i],
                       testCodec.isEncoded(OK_ENCODED_SHORTS[i]));
        }
    }

    public void testDecodeReturnsNullForNulls() {
        ShortCodec testCodec = new ShortCodec();
        assertNull("should return null", testCodec.decode(null));
    }

    public void testDecodeThrowsOnPoorlyFormattedStrings() {
        ShortCodec testCodec = new ShortCodec();
        for (int i = 0; i < NOT_OK_ENCODED_SHORTS.length; i++) {
            try {
                testCodec.decode(NOT_OK_ENCODED_SHORTS[i]);
                fail("Should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testDecodeConvertsCorrectlyFormattedStrings() {
        ShortCodec testCodec = new ShortCodec();
        for (int i = 0; i < OK_ENCODED_SHORTS.length; i++) {
            Object o = testCodec.decode(OK_ENCODED_SHORTS[i]);
            assertTrue("incorrect object type created", (o instanceof Short));
            assertEquals("incorrect value retrieved",
                         EXPECTED_DECODED_SHORTS[i],
                         ((Short) o).shortValue(),
                         1e-10d);
        }
    }

    public void testEncodeThrowsHJBExceptionForNulls() {
        ShortCodec testCodec = new ShortCodec();
        try {
            testCodec.encode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testEncodeThrowsHJBExceptionForWrongTypes() {
        ShortCodec testCodec = new ShortCodec();
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
        ShortCodec testCodec = new ShortCodec();
        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++) {
            String result = testCodec.encode(new Short(EXPECTED_DECODED_SHORTS[i]));
            assertEquals("encoding failed",
                         OK_EXPECTED_DECODED_SHORTS[i],
                         result);
        }
    }

    private static final String NOT_OK_ENCODED_SHORTS[] = CodecTestValues.NOT_OK_ENCODED_SHORTS;
    private static final String OK_ENCODED_SHORTS[] = CodecTestValues.OK_EXPECTED_DECODED_SHORTS;
    private static final short EXPECTED_DECODED_SHORTS[] = CodecTestValues.EXPECTED_DECODED_SHORTS;
    private static final String OK_EXPECTED_DECODED_SHORTS[] = CodecTestValues.OK_EXPECTED_DECODED_SHORTS;

}
