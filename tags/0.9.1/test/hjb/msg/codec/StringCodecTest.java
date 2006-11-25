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

public class StringCodecTest extends TestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StringCodec(),
                     new StringCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StringCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        StringCodec testCodec = new StringCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testIsEncodedReturnsTrueForNonNullStrings() {
        StringCodec testCodec = new StringCodec();
        assertTrue("should be true", testCodec.isEncoded("anything"));
    }

    public void testDecodeReturnsNullForNulls() {
        StringCodec testCodec = new StringCodec();
        assertNull("should return null", testCodec.decode(null));
    }

    public void testDecodeReturnsNonNullStringsUnaltered() {
        StringCodec testCodec = new StringCodec();
        assertEquals("should return the test string unaltered",
                     "testString",
                     testCodec.decode("testString"));
    }

    public void testEncodeThrowsHJBExceptionForNulls() {
        StringCodec testCodec = new StringCodec();
        try {
            testCodec.encode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testEncodeReturnsToStringForNonNulls() {
        StringCodec testCodec = new StringCodec();
        Object[] wrongTypes = new Object[] {
                new Object(), new Float(1), new Double(2), new Boolean(false),
        };
        for (int i = 0; i < wrongTypes.length; i++) {
            assertEquals("toString value was not returned",
                         "" + wrongTypes[i],
                         testCodec.encode(wrongTypes[i]));
        }
    }

}
