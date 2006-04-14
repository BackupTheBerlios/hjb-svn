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
package hjb.misc;

import junit.framework.TestCase;

public class YetAnotherBase64Test extends TestCase {

    public void testEncode() {
        YetAnotherBase64 base64 = new YetAnotherBase64();
        assertEquals("did not encode correctly",
                     ENCODED_SOURCE,
                     base64.encode(DECODED_SOURCE.getBytes()));
    }

    public void testDecodeWorksOKWithNoWhiteSpace() {
        YetAnotherBase64 base64 = new YetAnotherBase64();
        assertEquals("did not decode correctly",
                     DECODED_SOURCE.getBytes(),
                     base64.decode(ENCODED_SOURCE));
    }

    public void testDecodeWorksOKWithWhiteSpace() {
        YetAnotherBase64 base64 = new YetAnotherBase64();
        assertEquals("did not decode correctly",
                     DECODED_SOURCE.getBytes(),
                     base64.decode(ENCODED_SOURCE_WITH_WHITE_SPACE));
    }

    protected void assertEquals(String message, byte[] expected, byte[] actual) {
        assertEquals(message + " (array lengths were different)",
                     expected.length,
                     actual.length);
        assertEquals(message, new String(expected), new String(actual));
    }

    public static final String DECODED_SOURCE = "Man is distinguished, not only by his reason, but by this "
            + "singular passion from other animals, "
            + "which is a lust of the mind, that by a perseverance of "
            + "delight in the continued and indefatigable generation of "
            + "knowledge, exceeds the short vehemence of any carnal pleasure.";

    public static final String ENCODED_SOURCE = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0"
            + "aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1"
            + "c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0"
            + "aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdl"
            + "LCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=";

    public static final String ENCODED_SOURCE_WITH_WHITE_SPACE = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0 "
            + " aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1 "
            + " c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0\n"
            + "aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdl\n\r"
            + " LCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=";

}
