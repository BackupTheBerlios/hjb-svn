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

import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.msg.valuecopiers.MockMessageBuilder;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.jmock.MockObjectTestCase;

/**
 * <code>MapMessageFloatValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class MapMessageFloatValueCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new MapMessageFloatValueCopier(),
                     new MapMessageFloatValueCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new MapMessageFloatValueCopier(),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        MapMessageFloatValueCopier testCopier = new MapMessageFloatValueCopier();
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        MapMessageFloatValueCopier testCopier = new MapMessageFloatValueCopier();
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_FLOATS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        MapMessageFloatValueCopier testCopier = new MapMessageFloatValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("getFloat");
        MapMessageFloatValueCopier testCopier = new MapMessageFloatValueCopier();
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        MapMessageFloatValueCopier testCopier = new MapMessageFloatValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getFloat",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     new Float(EXPECTED_DECODED_FLOATS[i]));
            assertTrue("should be true " + EXPECTED_DECODED_FLOATS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnPossibleExceptions() {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("Thrown as a test"),
                new IllegalArgumentException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("setFloat", ex);
            MapMessageFloatValueCopier testCopier = new MapMessageFloatValueCopier();
            try {
                testCopier.addToMessage("testName",
                                        OK_EXPECTED_DECODED_FLOATS[0],
                                        testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testAddToMessageInvokesSetFloat() {
        MapMessageFloatValueCopier testCopier = new MapMessageFloatValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setFloat",
                                                                              "testMessage"
                                                                                      + i,
                                                                              "testName",
                                                                              new Float(EXPECTED_DECODED_FLOATS[i]));

            testCopier.addToMessage("testName",
                                    OK_ENCODED_FLOATS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("getFloat");
        MapMessageFloatValueCopier testCopier = new MapMessageFloatValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        MapMessageFloatValueCopier testCopier = new MapMessageFloatValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getFloat",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     new Float(EXPECTED_DECODED_FLOATS[i]));
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_FLOATS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(MapMessage.class);
    }

    private MockMessageBuilder messageBuilder;

    private static final String OK_ENCODED_FLOATS[] = CodecTestValues.OK_ENCODED_FLOATS;
    private static final float EXPECTED_DECODED_FLOATS[] = CodecTestValues.EXPECTED_DECODED_FLOATS;
    private static final String OK_EXPECTED_DECODED_FLOATS[] = CodecTestValues.OK_EXPECTED_DECODED_FLOATS;

}
