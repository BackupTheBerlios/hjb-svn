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

import java.util.ArrayList;

import javax.jms.JMSException;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;

public class OrderedTypedValueCodecTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new OrderedTypedValueCodec(),
                     new OrderedTypedValueCodec());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new OrderedTypedValueCodec(),
                      new Object());
    }

    public void testIsEncodedReturnsFalseForNulls() {
        OrderedTypedValueCodec testCodec = new OrderedTypedValueCodec();
        assertFalse("should be false for nulls ", testCodec.isEncoded(null));
    }

    public void testthrowsJMSExceptionOnMethodsDecodeThrowsHJBExceptionForNulls() {
        OrderedTypedValueCodec testCodec = new OrderedTypedValueCodec();
        try {
            testCodec.decode(null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testDecodeConvertsCorrectlyFormattedStrings() {
        OrderedTypedValueCodec testCodec = new OrderedTypedValueCodec();
        for (int k = 0; k < ORDERED_OK_ENCODED_VALUES.length; k++) {
            for (int i = 0; i < ORDERED_OK_ENCODED_VALUES[k].length; i++) {
                Object o = testCodec.decode(ORDERED_OK_ENCODED_VALUES[k][i]);
                assertEquals("object type not correct for "
                                     + ORDERED_OK_ENCODED_VALUES[k][i],
                             ORDERED_CODEC_CLASSES[k].getName(),
                             o.getClass().getName());
                if (byte[].class != ORDERED_CODEC_CLASSES[k]) {
                    assertEquals("incorrect value retrieved",
                                 ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k][i],
                                 o);
                } else {
                    assertEquals("incorrect value retrieved",
                                 new String((byte[]) ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k][i]),
                                 new String(((byte[]) o)));
                }
            }
        }
    }

    protected void throwsJMSExceptionOnMethods(Mock aMock, String[] methodNames) {
        for (int i = 0; i < methodNames.length; i++) {
            aMock.expects(atLeastOnce())
                .method(methodNames[i])
                .will(throwException(new JMSException("thrown as a test")));
        }
    }

    protected void throwsJMSExceptionOnMethods(Mock aMock, ArrayList methodNames) {
        throwsJMSExceptionOnMethods(aMock,
                                    (String[]) methodNames.toArray(new String[0]));
    }

    private static final Class[] ORDERED_CODEC_CLASSES = {
            Byte.class,
            Short.class,
            Character.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Boolean.class,
            byte[].class,
            String.class,
    };

    private static final Object[][] ORDERED_EXPECTED_DECODED_VALUES_OBJECTS = CodecTestValues.ORDERED_EXPECTED_DECODED_VALUES_OBJECTS;
    private static final String[][] ORDERED_OK_ENCODED_VALUES = CodecTestValues.ORDERED_OK_ENCODED_VALUES;
}
