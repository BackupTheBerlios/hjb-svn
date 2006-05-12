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
package hjb.msg.valuecopiers;

import javax.jms.JMSException;
import javax.jms.Message;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;

/**
 * <code>StringPropertyValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class StringPropertyValueCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StringPropertyValueCopier(),
                     new StringPropertyValueCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StringPropertyValueCopier(),
                      new Object());
    }

    public void testCanBeEncodedReturnsFalseOnPossibleExceptions() {
        Exception[] possibleExceptions = new Exception[] {
            new JMSException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("getStringProperty",
                                                                              ex);
            StringPropertyValueCopier testCopier = new StringPropertyValueCopier();
            assertFalse(testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testCanBeEncodedReturnsFalseOnNullValues() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getStringProperty",
                                                                                 "testMessage",
                                                                                 "testName",
                                                                                 null);
        StringPropertyValueCopier testCopier = new StringPropertyValueCopier();
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getStringProperty",
                                                                                 "testMessage",
                                                                                 "testName",
                                                                                 "Hello");
        StringPropertyValueCopier testCopier = new StringPropertyValueCopier();
        assertTrue(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testAddToMessageThrowsHJBExceptionOnPossibleExceptions() {
        Exception[] possibleExceptions = new Exception[] {
            new JMSException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("setStringProperty",
                                                                              ex);
            StringPropertyValueCopier testCopier = new StringPropertyValueCopier();
            try {
                testCopier.addToMessage("testName", "testValue", testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testAddToMessageInvokesSetStringProperty() {
        Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setStringProperty",
                                                                          "testMessage",
                                                                          "testName",
                                                                          "testValue");
        StringPropertyValueCopier testCopier = new StringPropertyValueCopier();
        testCopier.addToMessage("testName", "testValue", testMessage);
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnPossibleException() {
        Exception[] possibleExceptions = new Exception[] {
            new JMSException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("getStringProperty",
                                                                              ex);
            StringPropertyValueCopier testCopier = new StringPropertyValueCopier();
            try {
                testCopier.getAsEncodedValue("testName", testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnNull() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getStringProperty",
                                                                                 "testMessage",
                                                                                 "testName",
                                                                                 null);
        StringPropertyValueCopier testCopier = new StringPropertyValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReturnsValueUnaltered() {
        Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getStringProperty",
                                                                                 "testMessage",
                                                                                 "testName",
                                                                                 "Hello");
        StringPropertyValueCopier testCopier = new StringPropertyValueCopier();
        assertEquals("property value was altered",
                     "Hello",
                     testCopier.getAsEncodedValue("testName", testMessage));
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(Message.class);
    }

    private MockMessageBuilder messageBuilder;
}
