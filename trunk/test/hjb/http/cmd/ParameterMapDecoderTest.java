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
package hjb.http.cmd;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class ParameterMapDecoderTest extends TestCase {

    public void testDecodeReturnsEmptyMapOnNull() {
        ParameterMapDecoder decoder = new ParameterMapDecoder();
        assertNotNull(decoder.decode(null));
        assertEquals(0, ((Map) decoder.decode(null)).size());
    }

    public void testDecoderIgnoresNonStringKeys() {
        Map testMap = new HashMap();
        testMap.put(new Integer(1), new String[] {
            "toTest"
        });
        testMap.put("1", new String[] {
            "toTest"
        });

        ParameterMapDecoder decoder = new ParameterMapDecoder();
        assertEquals(1, ((Map) decoder.decode(testMap)).size());
    }

    public void testDecoderIgnoresNonStringArrayValues() {
        Map testMap = new HashMap();
        testMap.put("0", new Object());
        testMap.put("1", new String[] {
            "toTest"
        });

        ParameterMapDecoder decoder = new ParameterMapDecoder();
        assertEquals(1, ((Map) decoder.decode(testMap)).size());
    }

    public void testDecoderIgnoresEmptyStringArrayValues() {
        Map testMap = new HashMap();
        testMap.put("0", new String[0]);
        testMap.put("1", new String[] {
            "toTest"
        });

        ParameterMapDecoder decoder = new ParameterMapDecoder();
        assertEquals(1, ((Map) decoder.decode(testMap)).size());
    }

    public void testDecoderUsesTheFirstValueOfAStringArray() {
        Map testMap = new HashMap();
        testMap.put("1", new String[] {
                "toTest", "wontShow"
        });
        Map expectedMap = new HashMap();
        expectedMap.put("1", "toTest");

        ParameterMapDecoder decoder = new ParameterMapDecoder();
        assertEquals(expectedMap, decoder.decode(testMap));
    }

    public void testDecoderDecodesValues() {
        Map testMap = new HashMap();
        testMap.put("byte", new String[] {
            "(byte 1)"
        });
        testMap.put("int", new String[] {
            "(int 1)"
        });
        testMap.put("short", new String[] {
            "(short 1)"
        });
        testMap.put("long", new String[] {
            "(long 1)"
        });
        testMap.put("float", new String[] {
            "(float 1)"
        });
        testMap.put("double", new String[] {
            "(double 1)"
        });
        testMap.put("boolean", new String[] {
            "(boolean blah)"
        });
        testMap.put("string", new String[] {
            "foobarbaz"
        });
        testMap.put("char", new String[] {
            "(char \\u0061)"
        });
        testMap.put("byteArray", new String[] {
            "(base64 VEVTVA==)"
        });
        Map expectedMap = new HashMap();
        expectedMap.put("byte", new Byte((byte) 1));
        expectedMap.put("int", new Integer(1));
        expectedMap.put("short", new Short((short) 1));
        expectedMap.put("long", new Long(1));
        expectedMap.put("double", new Double(1));
        expectedMap.put("float", new Float(1));
        expectedMap.put("boolean", new Boolean(false));
        expectedMap.put("string", "foobarbaz");
        expectedMap.put("byteArray", "TEST".getBytes());
        expectedMap.put("char", new Character('\u0061'));

        ParameterMapDecoder decoder = new ParameterMapDecoder();
        Map actualMap = new HashMap(decoder.decode(testMap));
        // check the byte array on its own
        assertEquals("TEST", new String((byte[]) actualMap.get("byteArray")));
        actualMap.remove("byteArray");
        expectedMap.remove("byteArray");
        assertEquals(expectedMap, actualMap);
    }

}
