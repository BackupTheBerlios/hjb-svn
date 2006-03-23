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
package hjb.msg.valuecopiers.streammessage;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.jms.Message;
import javax.jms.StreamMessage;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.msg.valuecopiers.MockMessageBuilder;

/**
 * <code>OrderedStreamMessageValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class OrderedStreamMessageValueCopierTest extends MockObjectTestCase {

    public OrderedStreamMessageValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new OrderedStreamMessageValueCopier(valuesRead),
                     new OrderedStreamMessageValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new OrderedStreamMessageValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedStreamMessageValueCopier testCopier = new OrderedStreamMessageValueCopier(valuesRead);
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        OrderedStreamMessageValueCopier testCopier = new OrderedStreamMessageValueCopier(valuesRead);
        ArrayList throwsOn = new ArrayList();
        int mocks = 0;

        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readByte",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Byte(EXPECTED_DECODED_BYTES[i]),
                                                                                               throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_BYTES[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("readByte");

        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readShort",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Short(EXPECTED_DECODED_SHORTS[i]),
                                                                                               throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_SHORTS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("readShort");

        for (int i = 0; i < EXPECTED_DECODED_CHARS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readChar",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Character(EXPECTED_DECODED_CHARS[i]),
                                                                                               throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_CHARS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("readChar");

        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readInt",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Integer(EXPECTED_DECODED_INTEGERS[i]),
                                                                                               throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_INTEGERS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("readInt");

        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readLong",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Long(EXPECTED_DECODED_LONGS[i]),
                                                                                               throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_LONGS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("readLong");

        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readFloat",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Float(EXPECTED_DECODED_FLOATS[i]),
                                                                                               throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_FLOATS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("readFloat");

        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readDouble",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Double(EXPECTED_DECODED_DOUBLES[i]),
                                                                                               throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_DOUBLES[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("readDouble");

        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readBoolean",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Boolean(EXPECTED_DECODED_BOOLEANS[i]),
                                                                                               throwsOn);
            assertTrue("should be true " + EXPECTED_DECODED_BOOLEANS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
        throwsOn.add("readBoolean");

        for (int i = 0; i < NOT_OK_FOR_ANYTHING.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readString",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               NOT_OK_FOR_ANYTHING[i],
                                                                                               throwsOn);
            assertTrue("should be true " + NOT_OK_FOR_ANYTHING[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedStreamMessageValueCopier testCopier = new OrderedStreamMessageValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BYTES[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesCorrectSetter() {
        OrderedStreamMessageValueCopier testCopier = new OrderedStreamMessageValueCopier(valuesRead);
        int mocks = 0;

        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeByte",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              new Byte(EXPECTED_DECODED_BYTES[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_BYTES[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeShort",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              new Short(EXPECTED_DECODED_SHORTS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_SHORTS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_CHARS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeChar",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              new Character(EXPECTED_DECODED_CHARS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_CHARS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeInt",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              new Integer(EXPECTED_DECODED_INTEGERS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_INTEGERS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeLong",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              new Long(EXPECTED_DECODED_LONGS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_LONGS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeFloat",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              new Float(EXPECTED_DECODED_FLOATS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_FLOATS[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeDouble",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              new Double(EXPECTED_DECODED_DOUBLES[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_DOUBLES[i],
                                    testMessage);
        }

        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeBoolean",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              new Boolean(EXPECTED_DECODED_BOOLEANS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_BOOLEANS[i],
                                    testMessage);
        }

        for (int i = 0; i < NOT_OK_FOR_ANYTHING.length; i++, mocks++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeString",
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              NOT_OK_FOR_ANYTHING[i]);
            testCopier.addToMessage("testName",
                                    NOT_OK_FOR_ANYTHING[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedStreamMessageValueCopier testCopier = new OrderedStreamMessageValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        OrderedStreamMessageValueCopier testCopier = new OrderedStreamMessageValueCopier(valuesRead);
        ArrayList throwsOn = new ArrayList();
        int mocks = 0;

        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readByte",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Byte(EXPECTED_DECODED_BYTES[i]),
                                                                                               throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_BYTES[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("readByte");

        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readShort",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Short(EXPECTED_DECODED_SHORTS[i]),
                                                                                               throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_SHORTS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("readShort");

        for (int i = 0; i < EXPECTED_DECODED_CHARS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readChar",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Character(EXPECTED_DECODED_CHARS[i]),
                                                                                               throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_CHARS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("readChar");

        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readInt",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Integer(EXPECTED_DECODED_INTEGERS[i]),
                                                                                               throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_INTEGERS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("readInt");

        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readLong",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Long(EXPECTED_DECODED_LONGS[i]),
                                                                                               throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_LONGS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("readLong");

        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readFloat",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Float(EXPECTED_DECODED_FLOATS[i]),
                                                                                               throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_FLOATS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("readFloat");

        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readDouble",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Double(EXPECTED_DECODED_DOUBLES[i]),
                                                                                               throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_DOUBLES[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("readDouble");

        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readBoolean",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               new Boolean(EXPECTED_DECODED_BOOLEANS[i]),
                                                                                               throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_BOOLEANS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
        throwsOn.add("readBoolean");

        for (int i = 0; i < NOT_OK_FOR_ANYTHING.length; i++, mocks++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs("readString",
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               NOT_OK_FOR_ANYTHING[i],
                                                                                               throwsOn);
            assertEquals("retrieved property was encoded correctly",
                         NOT_OK_FOR_ANYTHING[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(this, StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

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

}
