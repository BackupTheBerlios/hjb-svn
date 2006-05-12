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
package hjb.msg.valuecopiers;

import java.util.ArrayList;

import javax.jms.Message;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.testsupport.MockMessageBuilder;

/**
 * <code>OrderedPropertyValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class OrderedPropertyValueCopierTest extends MockObjectTestCase {

    public OrderedPropertyValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new OrderedPropertyValueCopier(),
                     new OrderedPropertyValueCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new OrderedPropertyValueCopier(),
                      new Object());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedPropertyValueCopier testCopier = new OrderedPropertyValueCopier();
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        OrderedPropertyValueCopier testCopier = new OrderedPropertyValueCopier();
        ArrayList throwsOn = new ArrayList();
        for (int i = 0, mocks = 0; i < PROPERTY_METHODS.length; i++) {
            for (int j = 0; j < ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i].length; j++, mocks++) {
                Message testMessage = messageBuilder.throwsOnSome(PROPERTY_METHODS[i][0],
                                                                  "testMessage"
                                                                          + mocks,
                                                                  "testName",
                                                                  ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][j],
                                                                  throwsOn);
                assertTrue("should be true "
                                   + ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][j],
                           testCopier.canBeEncoded("testName", testMessage));
            }
            throwsOn.add(PROPERTY_METHODS[i][0]);
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedPropertyValueCopier testCopier = new OrderedPropertyValueCopier();
        try {
            testCopier.addToMessage("testName",
                                    ORDERED_OK_EXPECTED_DECODED_VALUES[0][0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesCorrectPropertySetter() {
        OrderedPropertyValueCopier testCopier = new OrderedPropertyValueCopier();
        int mocks = 0;
        for (int k = 0; k < PROPERTY_METHODS.length; k++) {
            for (int i = 0; i < ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k].length; i++, mocks++) {
                Message testMessage = messageBuilder.invokesNamedMethodAsExpected(PROPERTY_METHODS[k][1],
                                                                                  "testMessage"
                                                                                          + mocks,
                                                                                  "testName",
                                                                                  ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[k][i]);
                testCopier.addToMessage("testName",
                                        ORDERED_OK_ENCODED_VALUES[k][i],
                                        testMessage);
            }
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnAnyMethod();
        OrderedPropertyValueCopier testCopier = new OrderedPropertyValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        OrderedPropertyValueCopier testCopier = new OrderedPropertyValueCopier();
        ArrayList throwsOn = new ArrayList();
        for (int i = 0, mocks = 0; i < PROPERTY_METHODS.length; i++) {
            for (int j = 0; j < ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i].length; j++, mocks++) {
                Message testMessage = messageBuilder.throwsOnSome(PROPERTY_METHODS[i][0],
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
            throwsOn.add(PROPERTY_METHODS[i][0]);
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(Message.class);
    }

    private MockMessageBuilder messageBuilder;

    public static final String[][] PROPERTY_METHODS = {
            new String[] {
                    "getByteProperty", "setByteProperty"
            }, new String[] {
                    "getShortProperty", "setShortProperty"
            }, new String[] {
                    "getIntProperty", "setIntProperty"
            }, new String[] {
                    "getLongProperty", "setLongProperty"
            }, new String[] {
                    "getFloatProperty", "setFloatProperty"
            }, new String[] {
                    "getDoubleProperty", "setDoubleProperty"
            }, new String[] {
                    "getBooleanProperty", "setBooleanProperty"
            }, new String[] {
                    "getStringProperty", "setStringProperty"
            },
    };

    public static final Object[][] ORDERED_EXPECTED_DECODED_VALUES_OBJECTS = {
            CodecTestValues.EXPECTED_DECODED_BYTES_OBJECTS,
            CodecTestValues.EXPECTED_DECODED_SHORTS_OBJECTS,
            CodecTestValues.EXPECTED_DECODED_INTEGERS_OBJECTS,
            CodecTestValues.EXPECTED_DECODED_LONGS_OBJECTS,
            CodecTestValues.EXPECTED_DECODED_FLOATS_OBJECTS,
            CodecTestValues.EXPECTED_DECODED_DOUBLES_OBJECTS,
            CodecTestValues.EXPECTED_DECODED_BOOLEANS_OBJECTS,
            CodecTestValues.NOT_OK_FOR_ANYTHING,
    };

    public static final String[][] ORDERED_OK_EXPECTED_DECODED_VALUES = {
            CodecTestValues.OK_EXPECTED_DECODED_BYTES,
            CodecTestValues.OK_EXPECTED_DECODED_SHORTS,
            CodecTestValues.OK_EXPECTED_DECODED_INTEGERS,
            CodecTestValues.OK_EXPECTED_DECODED_LONGS,
            CodecTestValues.OK_EXPECTED_DECODED_FLOATS,
            CodecTestValues.OK_EXPECTED_DECODED_DOUBLES,
            CodecTestValues.OK_EXPECTED_DECODED_BOOLEANS,
            CodecTestValues.NOT_OK_FOR_ANYTHING,
    };

    public static final String[][] ORDERED_OK_ENCODED_VALUES = {
            CodecTestValues.OK_ENCODED_BYTES,
            CodecTestValues.OK_ENCODED_SHORTS,
            CodecTestValues.OK_ENCODED_INTEGERS,
            CodecTestValues.OK_ENCODED_LONGS,
            CodecTestValues.OK_ENCODED_FLOATS,
            CodecTestValues.OK_ENCODED_DOUBLES,
            CodecTestValues.OK_ENCODED_BOOLEANS,
            CodecTestValues.NOT_OK_FOR_ANYTHING,
    };

}
