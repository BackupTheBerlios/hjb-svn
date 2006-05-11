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

import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.msg.valuecopiers.MockMessageBuilder;

import java.util.LinkedList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageEOFException;
import javax.jms.StreamMessage;

import org.jmock.MockObjectTestCase;

/**
 * <code>StreamMessageByteValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StreamMessageByteValueCopierTest extends MockObjectTestCase {

    public StreamMessageByteValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageByteValueCopier(valuesRead),
                     new StreamMessageByteValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageByteValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BYTES[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readByte",
                                                                                 "testMessage",
                                                                                 new Byte(EXPECTED_DECODED_BYTES[0]));
        StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
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
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readByte",
                                                                              ex);
            StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
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
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readByte",
                                                                              ex);
            StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
            assertFalse(testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readByte",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Byte(EXPECTED_DECODED_BYTES[i]));
            assertTrue("should be true " + EXPECTED_DECODED_BYTES[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeByte");
        StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BYTES[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteByte() {
        StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeByte",
                                                                              "testMessage"
                                                                                      + i,
                                                                              new Byte(EXPECTED_DECODED_BYTES[i]));

            testCopier.addToMessage("testName",
                                    OK_ENCODED_BYTES[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readByte",
                                                                                 "testMessage",
                                                                                 new Byte(EXPECTED_DECODED_BYTES[0]));
        StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
        valuesRead.add(new Byte(EXPECTED_DECODED_BYTES[1]));
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     OK_EXPECTED_DECODED_BYTES[1],
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueThrowsIllegalStateExceptionOnMessageEOFException() {
        Exception[] possibleExceptions = new Exception[] {
            new MessageEOFException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readByte",
                                                                              ex);
            StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
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
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readByte",
                                                                              ex);
            StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
            try {
                testCopier.getAsEncodedValue("testName", testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        StreamMessageByteValueCopier testCopier = new StreamMessageByteValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readByte",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Byte(EXPECTED_DECODED_BYTES[i]));
            assertEquals("retrieved property was not encoded correctly",
                         OK_EXPECTED_DECODED_BYTES[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

    private static final String OK_ENCODED_BYTES[] = CodecTestValues.OK_ENCODED_BYTES;
    private static final byte EXPECTED_DECODED_BYTES[] = CodecTestValues.EXPECTED_DECODED_BYTES;
    private static final String OK_EXPECTED_DECODED_BYTES[] = CodecTestValues.OK_EXPECTED_DECODED_BYTES;
}
