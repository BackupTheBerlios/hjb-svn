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

import java.util.LinkedList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageEOFException;
import javax.jms.StreamMessage;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.testsupport.MockMessageBuilder;

/**
 * <code>StreamMessageFloatValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StreamMessageFloatValueCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageFloatValueCopier(valuesRead),
                     new StreamMessageFloatValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageFloatValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
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
        StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readFloat",
                                                                                 "testMessage",
                                                                                 new Float(EXPECTED_DECODED_FLOATS[0]));
        StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 1, valuesRead.size());
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 2, valuesRead.size());
    }

    public void testCanBeEncodedThrowsIllegalStateExceptionOnMessageEOFException() {
        Exception[] possibleExceptions = new Exception[] {
            new MessageEOFException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readFloat",
                                                                              ex);
            StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
            try {
                testCopier.canBeEncoded(null, testMessage);
                fail("should have thrown an exception");
            } catch (IllegalStateException e) {}
        }
    }

    public void testCanBeEncodedReturnsFalseOnPossibleExceptions() {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("Thrown as a test"),
                new NumberFormatException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readFloat",
                                                                              ex);
            StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
            assertFalse(testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readFloat",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Float(EXPECTED_DECODED_FLOATS[i]));
            assertTrue("should be true " + EXPECTED_DECODED_FLOATS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeFloat");
        StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_FLOATS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteFloat() {
        StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeFloat",
                                                                              "testMessage"
                                                                                      + i,
                                                                              new Float(EXPECTED_DECODED_FLOATS[i]));

            testCopier.addToMessage("testName",
                                    OK_ENCODED_FLOATS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsIllegalStateExceptionOnMessageEOFException() {
        Exception[] possibleExceptions = new Exception[] {
            new MessageEOFException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readFloat",
                                                                              ex);
            StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
            try {
                testCopier.getAsEncodedValue("testName", testMessage);
                fail("should have thrown an exception");
            } catch (IllegalStateException e) {}
        }
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnPossibleException() {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("Thrown as a test"),
                new NumberFormatException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readFloat",
                                                                              ex);
            StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
            try {
                testCopier.getAsEncodedValue("testName", testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readFloat",
                                                                                 "testMessage",
                                                                                 new Float(EXPECTED_DECODED_FLOATS[0]));
        StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
        valuesRead.add(new Float(EXPECTED_DECODED_FLOATS[1]));
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     OK_EXPECTED_DECODED_FLOATS[1],
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        StreamMessageFloatValueCopier testCopier = new StreamMessageFloatValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_FLOATS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readFloat",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Float(EXPECTED_DECODED_FLOATS[i]));
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_FLOATS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        messageBuilder = new MockMessageBuilder(StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

    private static final String OK_ENCODED_FLOATS[] = CodecTestValues.OK_ENCODED_FLOATS;
    private static final float EXPECTED_DECODED_FLOATS[] = CodecTestValues.EXPECTED_DECODED_FLOATS;
    private static final String OK_EXPECTED_DECODED_FLOATS[] = CodecTestValues.OK_EXPECTED_DECODED_FLOATS;

}
