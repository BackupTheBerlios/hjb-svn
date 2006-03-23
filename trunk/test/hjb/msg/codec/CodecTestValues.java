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

public class CodecTestValues {

    public static final String NOT_OK_ENCODED_BYTES[] = new String[] {
            "anything",
            "byte 1)",
            "btit 3",
            "(short 5)",
            "(int 7)",
            "(long 9)",
    };

    public static final String OK_ENCODED_BYTES[] = new String[] {
            "(byte 1)",
            "(byte 3)",
            "(byte   7)",
            "(byte\t\t9)",
            "(byte 11)",
            "(byte -1)",
    };

    public static final byte EXPECTED_DECODED_BYTES[] = new byte[] {
            1, 3, 7, 9, 11, -1,
    };

    public static final String OK_EXPECTED_DECODED_BYTES[] = new String[] {
            "(byte 1)",
            "(byte 3)",
            "(byte 7)",
            "(byte 9)",
            "(byte 11)",
            "(byte -1)",
    };

    public static final String NOT_OK_ENCODED_DOUBLES[] = new String[] {
            "anything",
            "double 1)",
            "dble 3fggf",
            "(short 5)",
            "(int 7)",
            "(double 9pi999)",
    };

    public static final String OK_ENCODED_DOUBLES[] = new String[] {
            "(double 1.00)",
            "(double 0.3457)",
            "(double 7.7e11)",
            "(double\t\t9.999D)",
            "(double\t\t\t11000.01f)",
            "(double      -15.85e-12)",
    };

    public static final double EXPECTED_DECODED_DOUBLES[] = new double[] {
            1d, 0.3457D, 7.7e11d, 9.999d, 11000.01D, -15.85e-12d,
    };

    public static final String OK_EXPECTED_DECODED_DOUBLES[] = new String[] {
            "(double 1E0)",
            "(double 345.7E-3)",
            "(double 770E9)",
            "(double 9.999E0)",
            "(double 11.00001E3)",
            "(double -15.85E-12)",
    };

    public static final String NOT_OK_ENCODED_FLOATS[] = new String[] {
            "anything",
            "float 1cd)",
            "dble 3fggf",
            "(short 5)",
            "(int 7)",
            "(flat 9pi999)",
    };

    public static final String OK_ENCODED_FLOATS[] = new String[] {
            "(float 1.00)",
            "(float 0.3457)",
            "(float 7.7e11)",
            "(float\t\t9.999D)",
            "(float\t\t\t11000.0f)",
            "(float      -15.85e-12)",
    };

    public static final float EXPECTED_DECODED_FLOATS[] = new float[] {
            1f, 0.3457f, 7.7e11f, 9.999f, 11000.0F, -15.85e-12f,
    };

    public static final String OK_EXPECTED_DECODED_FLOATS[] = new String[] {
            "(float 1E0)",
            "(float 345.7E-3)",
            "(float 770E9)",
            "(float 9.999E0)",
            "(float 11E3)",
            "(float -15.85E-12)",
    };

    public static final String NOT_OK_ENCODED_INTEGERS[] = new String[] {
            "anything",
            "int 1)",
            "dble 3fggf",
            "(short 5)",
            "(float 7)",
            "(int 9pi999)",
    };

    public static final String OK_ENCODED_INTEGERS[] = new String[] {
            "(int 1)",
            "(int 3456)",
            "(int 77)",
            "(int\t\t65536)",
            "(int\t\t\t-200000)",
    };

    public static final int EXPECTED_DECODED_INTEGERS[] = new int[] {
            1, 3456, 77, 65536, -200000,
    };

    public static final String OK_EXPECTED_DECODED_INTEGERS[] = new String[] {
            "(int 1)",
            "(int 3456)",
            "(int 77)",
            "(int 65536)",
            "(int -200000)",
    };

    public static final String NOT_OK_ENCODED_CHARS[] = new String[] {
            "anything",
            "int 1)",
            "dble 3fggf",
            "(short 5)",
            "(float 7)",
            "(int 9pi999)",
    };

    public static final String OK_ENCODED_CHARS[] = new String[] {
            "(char \\u61)",
            "(char \\u0AA)",
            "(char \\u0009)",
            "(char\t\t\\u2620)",
            "(char\t\t\t\\u18FF)",
    };

    public static final char EXPECTED_DECODED_CHARS[] = new char[] {
            '\u0061', '\u00AA', '\u0009', '\u2620', '\u18FF',
    };

    public static final String OK_EXPECTED_DECODED_CHARS[] = new String[] {
            "(char \\u0061)",
            "(char \\u00AA)",
            "(char \\u0009)",
            "(char \\u2620)",
            "(char \\u18FF)",
    };

    public static final String NOT_OK_ENCODED_BARRAYS[] = new String[] {
            "anything",
            "int 1)",
            "dble 3fggf",
            "(short 5)",
            "(float 7)",
            "(int 9pi999)",
    };

    public static final String OK_ENCODED_BARRAYS[] = new String[] {
            "(base64 VEVTVA==)",
            "(base64 QU5PVEhFUlRFU1Q=)",
            "(base64\t\tWUVUQU5PVEhFUlRFU1Q=)",
            "(base64\tRE9FU0FOWU9ORUZBTkNZQU5PVEhFUlRFU1Q=)",
    };

    public static final byte[][] EXPECTED_DECODED_BARRAYS = new byte[][] {
            "TEST".getBytes(),
            "ANOTHERTEST".getBytes(),
            "YETANOTHERTEST".getBytes(),
            "DOESANYONEFANCYANOTHERTEST".getBytes(),
    };

    public static final String OK_EXPECTED_DECODED_BARRAYS[] = new String[] {
            "(base64 VEVTVA==)",
            "(base64 QU5PVEhFUlRFU1Q=)",
            "(base64 WUVUQU5PVEhFUlRFU1Q=)",
            "(base64 RE9FU0FOWU9ORUZBTkNZQU5PVEhFUlRFU1Q=)",
    };

    public static final String NOT_OK_ENCODED_LONGS[] = new String[] {
            "anything",
            "long 1)",
            "dble 3fggf",
            "(short 5)",
            "(int 7)",
            "(long 9pi999)",
    };

    public static final String OK_ENCODED_LONGS[] = new String[] {
            "(long 1)",
            "(long 3456)",
            "(long 77)",
            "(long\t\t65536)",
            "(long\t\t\t-200000)",
    };

    public static final long EXPECTED_DECODED_LONGS[] = new long[] {
            1l, 3456l, 77l, 65536, -200000,
    };

    public static final String OK_EXPECTED_DECODED_LONGS[] = new String[] {
            "(long 1)",
            "(long 3456)",
            "(long 77)",
            "(long 65536)",
            "(long -200000)",
    };

    public static final String NOT_OK_ENCODED_SHORTS[] = new String[] {
            "anything",
            "short 1)",
            "dble 3fggf",
            "(long 5)",
            "(int 7)",
            "(short 9pi999)",
            "(short\t\t65536)",
            "(short\t\t\t-200000)",
    };

    public static final String OK_ENCODED_SHORTS[] = new String[] {
            "(short 1)",
            "(short 3456)",
            "(short 77)",
            "(short\t\t2000)",
            "(short\t\t\t-20000)",
    };

    public static final short EXPECTED_DECODED_SHORTS[] = new short[] {
            1, 3456, 77, 2000, -20000,
    };

    public static final String OK_EXPECTED_DECODED_SHORTS[] = new String[] {
            "(short 1)",
            "(short 3456)",
            "(short 77)",
            "(short 2000)",
            "(short -20000)",
    };

    public static final String NOT_OK_ENCODED_BOOLEANS[] = new String[] {
            "anything", "boolean 1)", "dble 3fggf", "(long 5)", "(int 7)",
    };

    public static final String OK_ENCODED_BOOLEANS[] = new String[] {
            "(boolean True)",
            "(boolean False)",
            "(boolean True)",
            "(boolean anything)",
            "(boolean true)",
    };

    public static final boolean EXPECTED_DECODED_BOOLEANS[] = new boolean[] {
            true, false, true, false, true
    };

    public static final String OK_EXPECTED_DECODED_BOOLEANS[] = new String[] {
            "(boolean true)",
            "(boolean false)",
            "(boolean true)",
            "(boolean false)",
            "(boolean true)",
    };

    public static final String NOT_OK_FOR_ANYTHING[] = new String[] {
            "anything", "boolean 1)", "dble 3fggf", "(lng 5)", "(inte 7)",
    };

}
