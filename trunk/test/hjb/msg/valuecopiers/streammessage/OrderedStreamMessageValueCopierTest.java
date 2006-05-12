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
        for (int k = 0, mocks = 0; k < ORDERED_STREAM_MESSAGE_METHODS.length; k++) {
            for (int i = 0; i < ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k].length; i++, mocks++) {
                Message testMessage = null;
                if (!"readBytes".equals(ORDERED_STREAM_MESSAGE_METHODS[k][0])) {
                    testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs(ORDERED_STREAM_MESSAGE_METHODS[k][0],
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k][i],
                                                                                               throwsOn);
                } else {
                    testMessage = messageBuilder.updatesMessageUsingByteArray(ORDERED_STREAM_MESSAGE_METHODS[k][0],
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              (byte[]) ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k][i],
                                                                              throwsOn);
                }
                assertTrue("should be true " + ORDERED_OK_ENCODED_VALUES[k][i],
                           testCopier.canBeEncoded("testName", testMessage));

            }
            throwsOn.add(ORDERED_STREAM_MESSAGE_METHODS[k][0]);
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedStreamMessageValueCopier testCopier = new OrderedStreamMessageValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    ORDERED_OK_EXPECTED_DECODED_VALUES[0][0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesCorrectSetter() {
        OrderedStreamMessageValueCopier testCopier = new OrderedStreamMessageValueCopier(valuesRead);
        for (int k = 0, mocks = 0; k < ORDERED_STREAM_MESSAGE_METHODS.length; k++) {
            for (int i = 0; i < ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k].length; i++, mocks++) {
                Message testMessage = messageBuilder.invokesNamedMethodAsExpected(ORDERED_STREAM_MESSAGE_METHODS[k][1],
                                                                                  "testMessage"
                                                                                          + mocks,
                                                                                  ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k][i]);
                testCopier.addToMessage("testName",
                                        ORDERED_OK_ENCODED_VALUES[k][i],
                                        testMessage);
            }
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
        for (int k = 0, mocks = 0; k < ORDERED_STREAM_MESSAGE_METHODS.length; k++) {
            for (int i = 0; i < ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k].length; i++, mocks++) {
                Message testMessage = null;
                if (!"readBytes".equals(ORDERED_STREAM_MESSAGE_METHODS[k][0])) {
                    testMessage = messageBuilder.returnsExpectedValueFromNamedMethodWithNoArgs(ORDERED_STREAM_MESSAGE_METHODS[k][0],
                                                                                               "testMessage"
                                                                                                       + mocks,
                                                                                               ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k][i],
                                                                                               throwsOn);
                } else {
                    testMessage = messageBuilder.updatesMessageUsingByteArray(ORDERED_STREAM_MESSAGE_METHODS[k][0],
                                                                              "testMessage"
                                                                                      + mocks,
                                                                              (byte[]) ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k][i],
                                                                              throwsOn);
                }
                assertEquals("retrieved property was encoded correctly",
                             ORDERED_OK_EXPECTED_DECODED_VALUES[k][i],
                             testCopier.getAsEncodedValue("testName",
                                                          testMessage));
            }
            throwsOn.add(ORDERED_STREAM_MESSAGE_METHODS[k][0]);
        }

    }

    protected void setUp() throws Exception {
        super.setUp();
        messageBuilder = new MockMessageBuilder(StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

    public static final String[][] ORDERED_STREAM_MESSAGE_METHODS = {
            new String[] {
                    "readByte", "writeByte"
            }, new String[] {
                    "readShort", "writeShort"
            }, new String[] {
                    "readChar", "writeChar"
            }, new String[] {
                    "readInt", "writeInt"
            }, new String[] {
                    "readLong", "writeLong"
            }, new String[] {
                    "readFloat", "writeFloat"
            }, new String[] {
                    "readDouble", "writeDouble"
            }, new String[] {
                    "readBoolean", "writeBoolean"
            }, new String[] {
                    "readBytes", "writeBytes"
            }, new String[] {
                    "readString", "writeString"
            },
    };

    private static final Object[][] ORDERED_EXPECTED_DECODED_VALUES_OBJECTS = CodecTestValues.ORDERED_EXPECTED_DECODED_VALUES_OBJECTS;
    private static final String[][] ORDERED_OK_EXPECTED_DECODED_VALUES = CodecTestValues.ORDERED_OK_EXPECTED_DECODED_VALUES;
    private static final String[][] ORDERED_OK_ENCODED_VALUES = CodecTestValues.ORDERED_OK_ENCODED_VALUES;
}
