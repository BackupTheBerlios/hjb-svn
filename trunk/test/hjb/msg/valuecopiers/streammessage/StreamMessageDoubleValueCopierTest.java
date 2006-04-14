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
 * <code>StreamMessageDoubleValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StreamMessageDoubleValueCopierTest extends MockObjectTestCase {

    public StreamMessageDoubleValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageDoubleValueCopier(valuesRead),
                     new StreamMessageDoubleValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageDoubleValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_DOUBLES[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readDouble",
                                                                                 "testMessage",
                                                                                 new Double(EXPECTED_DECODED_DOUBLES[0]));
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 1, valuesRead.size());
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 2, valuesRead.size());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readDouble");
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readDouble",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Double(EXPECTED_DECODED_DOUBLES[i]));
            assertTrue("should be true " + EXPECTED_DECODED_DOUBLES[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeDouble");
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_DOUBLES[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteDouble() {
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeDouble",
                                                                              "testMessage"
                                                                                      + i,
                                                                              new Double(EXPECTED_DECODED_DOUBLES[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_DOUBLES[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readDouble");
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readDouble",
                                                                                 "testMessage",
                                                                                 new Double(EXPECTED_DECODED_DOUBLES[0]));
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        valuesRead.add(new Double(EXPECTED_DECODED_DOUBLES[1]));
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     OK_EXPECTED_DECODED_DOUBLES[1],
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        StreamMessageDoubleValueCopier testCopier = new StreamMessageDoubleValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_DOUBLES.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readDouble",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     new Double(EXPECTED_DECODED_DOUBLES[i]));
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_DOUBLES[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

    private static final String OK_ENCODED_DOUBLES[] = CodecTestValues.OK_ENCODED_DOUBLES;
    private static final double EXPECTED_DECODED_DOUBLES[] = CodecTestValues.EXPECTED_DECODED_DOUBLES;
    private static final String OK_EXPECTED_DECODED_DOUBLES[] = CodecTestValues.OK_EXPECTED_DECODED_DOUBLES;

}
