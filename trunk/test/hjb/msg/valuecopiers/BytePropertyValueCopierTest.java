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

import javax.jms.Message;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;

/**
 * <code>BytePropertyValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class BytePropertyValueCopierTest extends MockObjectTestCase {

    public BytePropertyValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new BytePropertyValueCopier(),
                     new BytePropertyValueCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new BytePropertyValueCopier(),
                      new Object());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("getByteProperty");
        BytePropertyValueCopier testCopier = new BytePropertyValueCopier();
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        BytePropertyValueCopier testCopier = new BytePropertyValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getByteProperty",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     new Byte(EXPECTED_DECODED_BYTES[i]));
            assertTrue("should be true " + EXPECTED_DECODED_BYTES[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("setByteProperty");
        BytePropertyValueCopier testCopier = new BytePropertyValueCopier();
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BYTES[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesSetByteProperty() {
        BytePropertyValueCopier testCopier = new BytePropertyValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setByteProperty",
                                                                              "testMessage"
                                                                                      + i,
                                                                              "testName",
                                                                              new Byte(EXPECTED_DECODED_BYTES[i]));

            testCopier.addToMessage("testName",
                                    OK_ENCODED_BYTES[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("getByteProperty");
        BytePropertyValueCopier testCopier = new BytePropertyValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        BytePropertyValueCopier testCopier = new BytePropertyValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_BYTES.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getByteProperty",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     new Byte(EXPECTED_DECODED_BYTES[i]));
            assertEquals("retrieved property was not encoded correctly",
                         OK_EXPECTED_DECODED_BYTES[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(this, Message.class);
    }

    private MockMessageBuilder messageBuilder;

    private static final String OK_ENCODED_BYTES[] = CodecTestValues.OK_EXPECTED_DECODED_BYTES;
    private static final byte EXPECTED_DECODED_BYTES[] = CodecTestValues.EXPECTED_DECODED_BYTES;
    private static final String OK_EXPECTED_DECODED_BYTES[] = CodecTestValues.OK_EXPECTED_DECODED_BYTES;
}
