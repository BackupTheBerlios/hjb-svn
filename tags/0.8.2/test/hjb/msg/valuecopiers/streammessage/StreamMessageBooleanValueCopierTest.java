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
 * <code>StreamMessageBooleanValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StreamMessageBooleanValueCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageBooleanValueCopier(valuesRead),
                     new StreamMessageBooleanValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageBooleanValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BOOLEANS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readBoolean",
                                                                                 "testMessage",
                                                                                 new Boolean(EXPECTED_DECODED_BOOLEANS[0]));
        StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
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
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readBoolean",
                                                                              ex);
            StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
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
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readBoolean",
                                                                              ex);
            StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
            assertFalse(testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readBoolean",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Boolean(EXPECTED_DECODED_BOOLEANS[i]));
            assertTrue("should be true " + EXPECTED_DECODED_BOOLEANS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeBoolean");
        StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BOOLEANS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteBoolean() {
        StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeBoolean",
                                                                              "testMessage"
                                                                                      + i,
                                                                              new Boolean(EXPECTED_DECODED_BOOLEANS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_BOOLEANS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsIllegalStateExceptionOnMessageEOFException() {
        Exception[] possibleExceptions = new Exception[] {
            new MessageEOFException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readBoolean",
                                                                              ex);
            StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
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
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("readBoolean",
                                                                              ex);
            StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
            try {
                testCopier.getAsEncodedValue("testName", testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readBoolean",
                                                                                 "testMessage",
                                                                                 new Boolean(EXPECTED_DECODED_BOOLEANS[0]));
        StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
        valuesRead.add(new Boolean(EXPECTED_DECODED_BOOLEANS[1]));
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     OK_EXPECTED_DECODED_BOOLEANS[1],
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        StreamMessageBooleanValueCopier testCopier = new StreamMessageBooleanValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readBoolean",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Boolean(EXPECTED_DECODED_BOOLEANS[i]));
            assertEquals("retrieved property was not encoded correctly",
                         OK_EXPECTED_DECODED_BOOLEANS[i],
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

    private static final String OK_ENCODED_BOOLEANS[] = CodecTestValues.OK_ENCODED_BOOLEANS;
    private static final boolean EXPECTED_DECODED_BOOLEANS[] = CodecTestValues.EXPECTED_DECODED_BOOLEANS;
    private static final String OK_EXPECTED_DECODED_BOOLEANS[] = CodecTestValues.OK_EXPECTED_DECODED_BOOLEANS;
}
