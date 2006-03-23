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

import javax.jms.Message;
import javax.jms.StreamMessage;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.msg.valuecopiers.MockMessageBuilder;

/**
 * <code>StreamMessageCharValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StreamMessageCharValueCopierTest extends MockObjectTestCase {

    public StreamMessageCharValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageCharValueCopier(valuesRead),
                     new StreamMessageCharValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageCharValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_CHARS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readChar",
                                                                                 "testMessage",
                                                                                 new Character(EXPECTED_DECODED_CHARS[0]));
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 1, valuesRead.size());
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 2, valuesRead.size());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readChar");
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_CHARS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readChar",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Character(EXPECTED_DECODED_CHARS[i]));
            assertTrue("should be true " + EXPECTED_DECODED_CHARS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeChar");
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_CHARS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteInt() {
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_CHARS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeChar",
                                                                              "testMessage"
                                                                                      + i,
                                                                              new Character(EXPECTED_DECODED_CHARS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_CHARS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readChar");
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readChar",
                                                                                 "testMessage",
                                                                                 new Character(EXPECTED_DECODED_CHARS[0]));
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        valuesRead.add(new Character(EXPECTED_DECODED_CHARS[1]));
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     OK_EXPECTED_DECODED_CHARS[1],
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        StreamMessageCharValueCopier testCopier = new StreamMessageCharValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_CHARS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readChar",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Character(EXPECTED_DECODED_CHARS[i]));

            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_CHARS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(this, StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

    private static final String OK_ENCODED_CHARS[] = CodecTestValues.OK_ENCODED_CHARS;
    private static final char EXPECTED_DECODED_CHARS[] = CodecTestValues.EXPECTED_DECODED_CHARS;
    private static final String OK_EXPECTED_DECODED_CHARS[] = CodecTestValues.OK_EXPECTED_DECODED_CHARS;

}
