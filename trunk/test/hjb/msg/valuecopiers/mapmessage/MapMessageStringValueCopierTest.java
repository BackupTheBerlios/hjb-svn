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
package hjb.msg.valuecopiers.mapmessage;

import javax.jms.MapMessage;
import javax.jms.Message;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.valuecopiers.MockMessageBuilder;

/**
 * <code>StringMessageStringValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class MapMessageStringValueCopierTest extends MockObjectTestCase {

    public MapMessageStringValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new MapMessageStringValueCopier(),
                     new MapMessageStringValueCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new MapMessageStringValueCopier(),
                      new Object());
    }

    public void testCanBeEncodedThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        try {
            testCopier.canBeEncoded("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        try {
            testCopier.addToMessage("testName", "foobarbaz", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnIncorrectMessageType() {
        messageBuilder = new MockMessageBuilder(this, Message.class);
        Message testMessage = messageBuilder.nothingExpected();
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("getString");
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsFalseOnNullValues() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getString",
                                                                                 "testMessage",
                                                                                 "testName",
                                                                                 null);
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getString",
                                                                                 "testMessage",
                                                                                 "testName",
                                                                                 "Hello");
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("setString");
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        try {
            testCopier.addToMessage("testName", "testValue", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesSetString() {
        Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setString",
                                                                          "testMessage",
                                                                          "testName",
                                                                          "testValue");
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        testCopier.addToMessage("testName", "testValue", testMessage);
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("getString");
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueThrowsExceptionOnNull() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getString",
                                                                                 "testMessage",
                                                                                 "testName",
                                                                                 null);
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReturnsValueUnaltered() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getString",
                                                                                 "testMessage",
                                                                                 "testName",
                                                                                 "Hello");
        MapMessageStringValueCopier testCopier = new MapMessageStringValueCopier();
        assertEquals("property value was altered",
                     "Hello",
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(this, MapMessage.class);
    }

    private MockMessageBuilder messageBuilder;
}
