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
        for (int i = 0, mocks = 0; i < ORDERED_MAP_MESSAGE_METHODS.length; i++) {
            for (int j = 0; j < ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i].length; j++, mocks++) {
                Message testMessage = messageBuilder.throwsOnSome(ORDERED_MAP_MESSAGE_METHODS[i][0],
                                                                  "testMessage"
                                                                          + mocks,
                                                                  "testName",
                                                                  ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][j],
                                                                  throwsOn);
                assertTrue("should be true "
                                   + ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][j],
                           testCopier.canBeEncoded("testName", testMessage));
            }
            throwsOn.add(ORDERED_MAP_MESSAGE_METHODS[i][0]);
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedMapMessageValueCopier testCopier = new OrderedMapMessageValueCopier();
        try {
            testCopier.addToMessage("testName",
                                    ORDERED_OK_ENCODED_VALUES[0][0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesCorrectSetter() {
        OrderedMapMessageValueCopier testCopier = new OrderedMapMessageValueCopier();
        for (int i = 0, mocks = 0; i < ORDERED_MAP_MESSAGE_METHODS.length; i++) {
            for (int j = 0; j < ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i].length; j++, mocks++) {
                Message testMessage = messageBuilder.invokesNamedMethodAsExpected(ORDERED_MAP_MESSAGE_METHODS[i][1],
                                                                                  "testMessage"
                                                                                          + mocks,
                                                                                  "testName",
                                                                                  ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][j]);
                testCopier.addToMessage("testName",
                                        ORDERED_OK_ENCODED_VALUES[i][j],
                                        testMessage);
            }
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
        for (int i = 0, mocks = 0; i < ORDERED_MAP_MESSAGE_METHODS.length; i++) {
            for (int j = 0; j < ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i].length; j++, mocks++) {
                Message testMessage = messageBuilder.throwsOnSome(ORDERED_MAP_MESSAGE_METHODS[i][0],
                                                                  "testMessage"
                                                                          + mocks,
                                                                  "testName",
                                                                  ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][j],
                                                                  throwsOn);
                assertEquals("retrieved property was encoded correctly",
                             ORDERED_OK_EXPECTED_DECODED_VALUES[i][j],
                             testCopier.getAsEncodedValue("testName",
                                                          testMessage));
            }
            throwsOn.add(ORDERED_MAP_MESSAGE_METHODS[i][0]);
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        messageBuilder = new MockMessageBuilder(MapMessage.class);
    }

    private MockMessageBuilder messageBuilder;

    public static final String[][] ORDERED_MAP_MESSAGE_METHODS = {
            new String[] {
                    "getByte", "setByte"
            }, new String[] {
                    "getShort", "setShort"
            }, new String[] {
                    "getChar", "setChar"
            }, new String[] {
                    "getInt", "setInt"
            }, new String[] {
                    "getLong", "setLong"
            }, new String[] {
                    "getFloat", "setFloat"
            }, new String[] {
                    "getDouble", "setDouble"
            }, new String[] {
                    "getBoolean", "setBoolean"
            }, new String[] {
                    "getBytes", "setBytes"
            }, new String[] {
                    "getString", "setString"
            },
    };

    private static final Object[][] ORDERED_EXPECTED_DECODED_VALUES_OBJECTS = CodecTestValues.ORDERED_EXPECTED_DECODED_VALUES_OBJECTS;
    private static final String[][] ORDERED_OK_EXPECTED_DECODED_VALUES = CodecTestValues.ORDERED_OK_EXPECTED_DECODED_VALUES;
    private static final String[][] ORDERED_OK_ENCODED_VALUES = CodecTestValues.ORDERED_OK_ENCODED_VALUES;

}
