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
 * <code>StreamMessageShortValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StreamMessageShortValueCopierTest extends MockObjectTestCase {

    public StreamMessageShortValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageShortValueCopier(valuesRead),
                     new StreamMessageShortValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageShortValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_SHORTS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readShort",
                                                                                 "testMessage",
                                                                                 new Short(EXPECTED_DECODED_SHORTS[0]));
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 1, valuesRead.size());
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 2, valuesRead.size());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readShort");
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readShort",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Short(EXPECTED_DECODED_SHORTS[i]));
            assertTrue("should be true " + EXPECTED_DECODED_SHORTS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeShort");
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_SHORTS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteShort() {
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeShort",
                                                                              "testMessage"
                                                                                      + i,
                                                                              new Short(EXPECTED_DECODED_SHORTS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_SHORTS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readShort");
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readShort",
                                                                                 "testMessage",
                                                                                 new Short(EXPECTED_DECODED_SHORTS[0]));
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        valuesRead.add(new Short(EXPECTED_DECODED_SHORTS[1]));
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     OK_EXPECTED_DECODED_SHORTS[1],
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        StreamMessageShortValueCopier testCopier = new StreamMessageShortValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readShort",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Short(EXPECTED_DECODED_SHORTS[i]));
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_SHORTS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(this, StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

    private static final String OK_ENCODED_SHORTS[] = CodecTestValues.OK_ENCODED_SHORTS;
    private static final short EXPECTED_DECODED_SHORTS[] = CodecTestValues.EXPECTED_DECODED_SHORTS;
    private static final String OK_EXPECTED_DECODED_SHORTS[] = CodecTestValues.OK_EXPECTED_DECODED_SHORTS;

}
