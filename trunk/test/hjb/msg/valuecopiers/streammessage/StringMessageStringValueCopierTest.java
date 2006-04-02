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
import hjb.msg.valuecopiers.MockMessageBuilder;

/**
 * <code>StringMessageStringValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StringMessageStringValueCopierTest extends MockObjectTestCase {

    public StringMessageStringValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageStringValueCopier(valuesRead),
                     new StreamMessageStringValueCopier(valuesRead));
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageStringValueCopier(valuesRead),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName", "humptydumpty", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedAddsToValuesRead() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readString",
                                                                                 "testMessage",
                                                                                 "Hello");
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 1, valuesRead.size());
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("number of valuesRead was incorrect", 2, valuesRead.size());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readString");
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsFalseOnNullValues() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readString",
                                                                                 "testMessage",
                                                                                 null);
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readString",
                                                                                 "testMessage",
                                                                                 "Hello");
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("writeString");
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        try {
            testCopier.addToMessage("testName", "testValue", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesWriteString() {
        Message testMessage = messageBuilder.invokesNamedMethodAsExpected("writeString",
                                                                          "testMessage",
                                                                          "testValue");
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        testCopier.addToMessage("testName", "testValue", testMessage);
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("readString");
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsExceptionOnNull() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readString",
                                                                                 "testMessage",
                                                                                 null);
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReadsFromValuesReadFirst() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readString",
                                                                                 "testMessage",
                                                                                 "Hello");
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        valuesRead.add("GoodBye");
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
        assertEquals("Did not read from valuesRead",
                     "GoodBye",
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    public void testGetAsEncodedValueReturnsValueUnaltered() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("readString",
                                                                                 "testMessage",
                                                                                 "Hello");
        StreamMessageStringValueCopier testCopier = new StreamMessageStringValueCopier(valuesRead);
        assertEquals("property value was altered",
                     "Hello",
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(StreamMessage.class);
        valuesRead = new LinkedList();
    }

    private MockMessageBuilder messageBuilder;
    private LinkedList valuesRead;
}
