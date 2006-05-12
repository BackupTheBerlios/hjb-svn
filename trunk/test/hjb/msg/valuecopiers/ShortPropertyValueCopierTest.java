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
import hjb.msg.codec.CodecTestValues;
import hjb.testsupport.MockMessageBuilder;

/**
 * <code>ShortValuePropertyValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class ShortPropertyValueCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new ShortPropertyValueCopier(),
                     new ShortPropertyValueCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new ShortPropertyValueCopier(),
                      new Object());
    }

    public void testCanBeEncodedReturnsFalseOnPossibleExceptions() {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("Thrown as a test"),
                new NumberFormatException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("getShortProperty",
                                                                              ex);
            ShortPropertyValueCopier testCopier = new ShortPropertyValueCopier();
            assertFalse(testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        ShortPropertyValueCopier testCopier = new ShortPropertyValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getShortProperty",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     new Short(EXPECTED_DECODED_SHORTS[i]));
            assertTrue("should be true " + EXPECTED_DECODED_SHORTS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnPossibleExceptions() {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("Thrown as a test"),
                new IllegalArgumentException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("setShortProperty",
                                                                              ex);
            ShortPropertyValueCopier testCopier = new ShortPropertyValueCopier();
            try {
                testCopier.addToMessage("testName",
                                        OK_EXPECTED_DECODED_SHORTS[0],
                                        testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testAddToMessageInvokesSetShortProperty() {
        ShortPropertyValueCopier testCopier = new ShortPropertyValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setShortProperty",
                                                                              "testMessage"
                                                                                      + i,
                                                                              "testName",
                                                                              new Short(EXPECTED_DECODED_SHORTS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_SHORTS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsHJBExceptionOnPossibleException() {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("Thrown as a test"),
                new NumberFormatException("Thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Exception ex = possibleExceptions[i];
            Message testMessage = messageBuilder.throwsExceptionOnMethodNamed("getShortProperty",
                                                                              ex);
            ShortPropertyValueCopier testCopier = new ShortPropertyValueCopier();
            try {
                testCopier.getAsEncodedValue("testName", testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        ShortPropertyValueCopier testCopier = new ShortPropertyValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_SHORTS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getShortProperty",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     new Short(EXPECTED_DECODED_SHORTS[i]));
            assertEquals("retrieved property was encoded correctly",
                         OK_EXPECTED_DECODED_SHORTS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(Message.class);
    }

    private MockMessageBuilder messageBuilder;

    private static final String OK_ENCODED_SHORTS[] = CodecTestValues.OK_EXPECTED_DECODED_SHORTS;
    private static final short EXPECTED_DECODED_SHORTS[] = CodecTestValues.EXPECTED_DECODED_SHORTS;
    private static final String OK_EXPECTED_DECODED_SHORTS[] = CodecTestValues.OK_EXPECTED_DECODED_SHORTS;
}
