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

import java.util.Arrays;
import java.util.LinkedList;

import javax.jms.Message;
import javax.jms.StreamMessage;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.msg.valuecopiers.MockMessageBuilder;

/**
 * <code>StreamMessageByteArrayValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StreamMessageByteArrayValueCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageByteArrayValueCopier(valuesRead),
                     new StreamMessageByteArrayValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageByteArrayValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BARRAYS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testLargeByteArraysAreHandleCorrectly() {
        byte[] reallyBig = new byte[65536 * 8];
        Arrays.fill(reallyBig, (byte) 20);
        Message testMessage = messageBuilder.updatesMessageUsingByteArray("readBytes",
                                                                              "testMessage",
                                                                              reallyBig);
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect",
                     65536 * 8,
                     ((byte[]) valuesRead.get(0)).length);
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.updatesMessageUsingByteArray("readBytes",
                                                                              "testMessage",
                                                                              EXPECTED_DECODED_BARRAYS[0]);
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        assertEquals("number of valuesRead was incorrect", 0, valuesRead.size());
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 1, valuesRead.size());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readBytes");
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++) {
            Message testMessage = messageBuilder.updatesMessageUsingByteArray("readBytes",
                                                                                  "testMessage"
                                                                                          + i,
                                                                                  EXPECTED_DECODED_BARRAYS[i]);

            assertTrue("should be true " + EXPECTED_DECODED_BARRAYS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeBytes");
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BARRAYS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteMethod() {
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeBytes",
                                                                              "testMessage"
                                                                                      + i,
                                                                              EXPECTED_DECODED_BARRAYS[i]);
            testCopier.addToMessage("testName",
                                    OK_ENCODED_BARRAYS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readBytes");
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.updatesMessageUsingByteArray("readBytes",
                                                                              "testMessage",
                                                                              EXPECTED_DECODED_BARRAYS[0]);
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        valuesRead.add(EXPECTED_DECODED_BARRAYS[1]);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     OK_EXPECTED_DECODED_BARRAYS[1],
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        StreamMessageByteArrayValueCopier testCopier = new StreamMessageByteArrayValueCopier(valuesRead);
        for (int i = 0; i < EXPECTED_DECODED_BARRAYS.length; i++) {
            Message testMessage = messageBuilder.updatesMessageUsingByteArray("readBytes",
                                                                                  "testMessage"
                                                                                          + i,
                                                                                  EXPECTED_DECODED_BARRAYS[i]);
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_BARRAYS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(this, StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;

    private static final String OK_ENCODED_BARRAYS[] = CodecTestValues.OK_ENCODED_BARRAYS;
    private static final byte EXPECTED_DECODED_BARRAYS[][] = CodecTestValues.EXPECTED_DECODED_BARRAYS;
    private static final String OK_EXPECTED_DECODED_BARRAYS[] = CodecTestValues.OK_EXPECTED_DECODED_BARRAYS;

}
