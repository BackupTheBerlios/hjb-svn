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
package hjb.msg.valuecopiers.mapmessage;

import java.util.ArrayList;

import javax.jms.MapMessage;
import javax.jms.Message;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.msg.valuecopiers.MockMessageBuilder;

/**
 * <code>OrderedStreamMessageValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class OrderedMapMessageValueCopierTest extends MockObjectTestCase {

    public OrderedMapMessageValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new OrderedMapMessageValueCopier(),
                     new OrderedMapMessageValueCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new OrderedMapMessageValueCopier(),
                      new Object());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedMapMessageValueCopier testCopier = new OrderedMapMessageValueCopier();
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        OrderedMapMessageValueCopier testCopier = new OrderedMapMessageValueCopier();
        ArrayList throwsOn = new ArrayList();
        int mocks = 0;

        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getByte",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Byte(EXPECTED_DECODED_BYTES[i]),
                                                                                     throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_BYTES[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("getByte");

        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getShort",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Short(EXPECTED_DECODED_SHORTS[i]),
                                                                                     throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_SHORTS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("getShort");

        for (int i = 0; i < EXPECTED_DECODED_CHARS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getChar",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Character(EXPECTED_DECODED_CHARS[i]),
                                                                                     throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_CHARS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("getChar");

        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getInt",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Integer(EXPECTED_DECODED_INTEGERS[i]),
                                                                                     throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_INTEGERS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("getInt");

        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getLong",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Long(EXPECTED_DECODED_LONGS[i]),
                                                                                     throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_LONGS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("getLong");

        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getFloat",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Float(EXPECTED_DECODED_FLOATS[i]),
                                                                                     throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_FLOATS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("getFloat");

        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getDouble",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Double(EXPECTED_DECODED_DOUBLES[i]),
                                                                                     throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_DOUBLES[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("getDouble");

        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getBoolean",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Boolean(EXPECTED_DECODED_BOOLEANS[i]),
                                                                                     throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_BOOLEANS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("getBoolean");

        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getBytes",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     EXPECTED_DECODED_BARRAYS[i],
                                                                                     throwsOn);
            assertTrue("should be true", testCopier.canBeEncoded("testName",
                                                                 testMessage));
        }
        throwsOn.add("getBytes");

        for (int i = 0; i < NOT_OK_FOR_ANYTHING.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getString",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     NOT_OK_FOR_ANYTHING[i],
                                                                                     throwsOn);
            assertTrue("should be true " + NOT_OK_FOR_ANYTHING[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }

    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedMapMessageValueCopier testCopier = new OrderedMapMessageValueCopier();
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BYTES[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesCorrectSetter() {
        OrderedMapMessageValueCopier testCopier = new OrderedMapMessageValueCopier();
        int mocks = 0;

        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setByte",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              new Byte(EXPECTED_DECODED_BYTES[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_BYTES[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setShort",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              new Short(EXPECTED_DECODED_SHORTS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_SHORTS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_CHARS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setChar",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              new Character(EXPECTED_DECODED_CHARS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_CHARS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setInt",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              new Integer(EXPECTED_DECODED_INTEGERS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_INTEGERS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setLong",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              new Long(EXPECTED_DECODED_LONGS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_LONGS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setFloat",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              new Float(EXPECTED_DECODED_FLOATS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_FLOATS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setDouble",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              new Double(EXPECTED_DECODED_DOUBLES[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_DOUBLES[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setBoolean",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              new Boolean(EXPECTED_DECODED_BOOLEANS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_BOOLEANS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setBytes",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              EXPECTED_DECODED_BARRAYS[i]);
            testCopier.addToMessage("testName",
                                    OK_ENCODED_BARRAYS[i],
                                    testMessage);

        }

        for (int i = 0; i < NOT_OK_FOR_ANYTHING.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setString",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              "testName",
                                                                              NOT_OK_FOR_ANYTHING[i]);
            testCopier.addToMessage("testName",
                                    NOT_OK_FOR_ANYTHING[i],
                                    testMessage);
        }

    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedMapMessageValueCopier testCopier = new OrderedMapMessageValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        OrderedMapMessageValueCopier testCopier = new OrderedMapMessageValueCopier();
        ArrayList throwsOn = new ArrayList();
        int mocks = 0;

        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getByte",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Byte(EXPECTED_DECODED_BYTES[i]),
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_BYTES[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("getByte");

        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getShort",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Short(EXPECTED_DECODED_SHORTS[i]),
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_SHORTS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("getShort");

        for (int i = 0; i < EXPECTED_DECODED_CHARS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getChar",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Character(EXPECTED_DECODED_CHARS[i]),
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_CHARS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("getChar");

        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getInt",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Integer(EXPECTED_DECODED_INTEGERS[i]),
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_INTEGERS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("getInt");

        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getLong",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Long(EXPECTED_DECODED_LONGS[i]),
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_LONGS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("getLong");

        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getFloat",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Float(EXPECTED_DECODED_FLOATS[i]),
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_FLOATS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("getFloat");

        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getDouble",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Double(EXPECTED_DECODED_DOUBLES[i]),
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_DOUBLES[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("getDouble");

        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getBoolean",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     new Boolean(EXPECTED_DECODED_BOOLEANS[i]),
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_BOOLEANS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("getBoolean");

        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getBytes",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     EXPECTED_DECODED_BARRAYS[i],
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_BARRAYS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("getBytes");

        for (int i = 0; i < NOT_OK_FOR_ANYTHING.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getString",
                                                                                     "testMessage"
                                                                                             + mocks,
                                                                                     "testName",
                                                                                     NOT_OK_FOR_ANYTHING[i],
                                                                                     throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         NOT_OK_FOR_ANYTHING[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }

    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(this, MapMessage.class);
    }

    private MockMessageBuilder messageBuilder;

    private static final String[] OK_ENCODED_BYTES = CodecTestValues.OK_ENCODED_BYTES;

    private static final byte[] EXPECTED_DECODED_BYTES = CodecTestValues.EXPECTED_DECODED_BYTES;

    private static final String[] OK_EXPECTED_DECODED_BYTES = CodecTestValues.OK_EXPECTED_DECODED_BYTES;

    private static final String[] OK_ENCODED_BOOLEANS = CodecTestValues.OK_ENCODED_BOOLEANS;

    private static final boolean[] EXPECTED_DECODED_BOOLEANS = CodecTestValues.EXPECTED_DECODED_BOOLEANS;

    private static final String[] OK_EXPECTED_DECODED_BOOLEANS = CodecTestValues.OK_EXPECTED_DECODED_BOOLEANS;

    private static final String[] OK_ENCODED_DOUBLES = CodecTestValues.OK_ENCODED_DOUBLES;

    private static final double[] EXPECTED_DECODED_DOUBLES = CodecTestValues.EXPECTED_DECODED_DOUBLES;

    private static final String[] OK_EXPECTED_DECODED_DOUBLES = CodecTestValues.OK_EXPECTED_DECODED_DOUBLES;

    private static final String[] OK_ENCODED_FLOATS = CodecTestValues.OK_ENCODED_FLOATS;

    private static final float[] EXPECTED_DECODED_FLOATS = CodecTestValues.EXPECTED_DECODED_FLOATS;

    private static final String[] OK_EXPECTED_DECODED_FLOATS = CodecTestValues.OK_EXPECTED_DECODED_FLOATS;

    private static final String[] OK_ENCODED_INTEGERS = CodecTestValues.OK_ENCODED_INTEGERS;

    private static final int[] EXPECTED_DECODED_INTEGERS = CodecTestValues.EXPECTED_DECODED_INTEGERS;

    private static final String[] OK_EXPECTED_DECODED_INTEGERS = CodecTestValues.OK_EXPECTED_DECODED_INTEGERS;

    private static final String[] OK_ENCODED_CHARS = CodecTestValues.OK_ENCODED_CHARS;

    private static final char[] EXPECTED_DECODED_CHARS = CodecTestValues.EXPECTED_DECODED_CHARS;

    private static final String[] OK_EXPECTED_DECODED_CHARS = CodecTestValues.OK_EXPECTED_DECODED_CHARS;

    private static final String[] OK_ENCODED_LONGS = CodecTestValues.OK_ENCODED_LONGS;

    private static final long[] EXPECTED_DECODED_LONGS = CodecTestValues.EXPECTED_DECODED_LONGS;

    private static final String[] OK_EXPECTED_DECODED_LONGS = CodecTestValues.OK_EXPECTED_DECODED_LONGS;

    private static final String[] OK_ENCODED_SHORTS = CodecTestValues.OK_ENCODED_SHORTS;

    private static final short[] EXPECTED_DECODED_SHORTS = CodecTestValues.EXPECTED_DECODED_SHORTS;

    private static final String[] OK_EXPECTED_DECODED_SHORTS = CodecTestValues.OK_EXPECTED_DECODED_SHORTS;

    private static final String[] NOT_OK_FOR_ANYTHING = CodecTestValues.NOT_OK_FOR_ANYTHING;

    private static final String[] OK_ENCODED_BARRAYS = CodecTestValues.OK_ENCODED_BARRAYS;

    private static final byte[][] EXPECTED_DECODED_BARRAYS = CodecTestValues.EXPECTED_DECODED_BARRAYS;

    private static final String[] OK_EXPECTED_DECODED_BARRAYS = CodecTestValues.OK_EXPECTED_DECODED_BARRAYS;

}
