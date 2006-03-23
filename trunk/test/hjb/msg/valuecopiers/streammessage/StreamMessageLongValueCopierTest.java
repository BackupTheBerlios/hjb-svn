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
 * <code>StreamMessageLongValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StreamMessageLongValueCopierTest extends MockObjectTestCase {

    public StreamMessageLongValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageLongValueCopier(valuesRead),
                     new StreamMessageLongValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageLongValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_LONGS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readLong",
                                                                                 "testMessage",
                                                                                 new Long(EXPECTED_DECODED_LONGS[0]));
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 1, valuesRead.size());
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 2, valuesRead.size());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readLong");
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readLong",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Long(EXPECTED_DECODED_LONGS[i]));
            assertTrue("should be true " + EXPECTED_DECODED_LONGS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeLong");
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_LONGS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteLong() {
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeLong",
                                                                              "testMessage"
                                                                                      + i,
                                                                              new Long(EXPECTED_DECODED_LONGS[i]));

            testCopier.addToMessage("testName",
                                    OK_ENCODED_LONGS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readLong");
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readLong",
                                                                                 "testMessage",
                                                                                 new Long(EXPECTED_DECODED_LONGS[0]));
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        valuesRead.add(new Long(EXPECTED_DECODED_LONGS[1]));
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     OK_EXPECTED_DECODED_LONGS[1],
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        StreamMessageLongValueCopier testCopier = new StreamMessageLongValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_LONGS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readLong",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Long(EXPECTED_DECODED_LONGS[i]));
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_LONGS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(this, StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

    private static final String OK_ENCODED_LONGS[] = CodecTestValues.OK_ENCODED_LONGS;
    private static final long EXPECTED_DECODED_LONGS[] = CodecTestValues.EXPECTED_DECODED_LONGS;
    private static final String OK_EXPECTED_DECODED_LONGS[] = CodecTestValues.OK_EXPECTED_DECODED_LONGS;

}
