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
 * <code>StreamMessageIntValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StreamMessageIntValueCopierTest extends MockObjectTestCase {

    public StreamMessageIntValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageIntValueCopier(valuesRead),
                     new StreamMessageIntValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageIntValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_INTEGERS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readInt",
                                                                                 "testMessage",
                                                                                 new Integer(EXPECTED_DECODED_INTEGERS[0]));
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 1, valuesRead.size());
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 2, valuesRead.size());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readInt");
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readInt",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Integer(EXPECTED_DECODED_INTEGERS[i]));
            assertTrue("should be true " + EXPECTED_DECODED_INTEGERS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeInt");
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_INTEGERS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteInt() {
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeInt",
                                                                              "testMessage"
                                                                                      + i,
                                                                              new Integer(EXPECTED_DECODED_INTEGERS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_INTEGERS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readInt");
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readInt",
                                                                                 "testMessage",
                                                                                 new Integer(EXPECTED_DECODED_INTEGERS[0]));
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        valuesRead.add(new Integer(EXPECTED_DECODED_INTEGERS[1]));
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     OK_EXPECTED_DECODED_INTEGERS[1],
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        StreamMessageIntValueCopier testCopier = new StreamMessageIntValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_INTEGERS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readInt",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Integer(EXPECTED_DECODED_INTEGERS[i]));

            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_INTEGERS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(this, StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

    private static final String OK_ENCODED_INTEGERS[] = CodecTestValues.OK_ENCODED_INTEGERS;
    private static final int EXPECTED_DECODED_INTEGERS[] = CodecTestValues.EXPECTED_DECODED_INTEGERS;
    private static final String OK_EXPECTED_DECODED_INTEGERS[] = CodecTestValues.OK_EXPECTED_DECODED_INTEGERS;

}
