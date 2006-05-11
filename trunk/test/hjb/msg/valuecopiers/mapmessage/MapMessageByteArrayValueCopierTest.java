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
 * <code>MapMessageByteArrayValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class MapMessageByteArrayValueCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new MapMessageByteArrayValueCopier(),
                     new MapMessageByteArrayValueCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new MapMessageByteArrayValueCopier(),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        MapMessageByteArrayValueCopier testCopier = new MapMessageByteArrayValueCopier();
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        MapMessageByteArrayValueCopier testCopier = new MapMessageByteArrayValueCopier();
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BARRAYS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        MapMessageByteArrayValueCopier testCopier = new MapMessageByteArrayValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedReturnsFalseOnPossibleExceptions() {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("Thrown as a test"),
                new NumberFormatException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("getBytes",
                                                                              ex);
            MapMessageByteArrayValueCopier testCopier = new MapMessageByteArrayValueCopier();
            assertFalse(testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        MapMessageByteArrayValueCopier testCopier = new MapMessageByteArrayValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getBytes",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     EXPECTED_DECODED_BARRAYS[i]);
            assertTrue("should be true " + EXPECTED_DECODED_BARRAYS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnExceptions() {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("Thrown as a test"),
                new NullPointerException("Thrown as test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("setBytes",
                                                                              ex);
            MapMessageByteArrayValueCopier testCopier = new MapMessageByteArrayValueCopier();
            try {
                testCopier.addToMessage("testName",
                                        OK_EXPECTED_DECODED_BARRAYS[0],
                                        testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testAddToMessageInvokesSetInt() {
        MapMessageByteArrayValueCopier testCopier = new MapMessageByteArrayValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setBytes",
                                                                              "testMessage"
                                                                                      + i,
                                                                              "testName",
                                                                              EXPECTED_DECODED_BARRAYS[i]);
            testCopier.addToMessage("testName",
                                    OK_ENCODED_BARRAYS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("getBytes");
        MapMessageByteArrayValueCopier testCopier = new MapMessageByteArrayValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        MapMessageByteArrayValueCopier testCopier = new MapMessageByteArrayValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getBytes",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     EXPECTED_DECODED_BARRAYS[i]);

            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_BARRAYS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(MapMessage.class);
    }

    private MockMessageBuilder messageBuilder;

    private static final String OK_ENCODED_BARRAYS[] = CodecTestValues.OK_ENCODED_BARRAYS;
    private static final byte EXPECTED_DECODED_BARRAYS[][] = CodecTestValues.EXPECTED_DECODED_BARRAYS;
    private static final String OK_EXPECTED_DECODED_BARRAYS[] = CodecTestValues.OK_EXPECTED_DECODED_BARRAYS;

}
