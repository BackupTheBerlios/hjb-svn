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
 * <code>BooleanPropertyValueCopierTest</code>
 * 
 * @author Tim Emiola
 */
public class BooleanPropertyValueCopierTest extends MockObjectTestCase {

    public BooleanPropertyValueCopierTest() {

    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new BooleanPropertyValueCopier(),
                     new BooleanPropertyValueCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new BooleanPropertyValueCopier(),
                      new Object());
    }

    public void testCanBeEncodedReturnsFalseOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("getBooleanProperty");
        BooleanPropertyValueCopier testCopier = new BooleanPropertyValueCopier();
        assertFalse(testCopier.canBeEncoded("testName", testMessage));
    }

    public void testCanBeEncodedReturnsTrueForCorrectValues() {
        BooleanPropertyValueCopier testCopier = new BooleanPropertyValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getBooleanProperty",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     new Boolean(EXPECTED_DECODED_BOOLEANS[i]));
            assertTrue("should be true " + EXPECTED_DECODED_BOOLEANS[i],
                       testCopier.canBeEncoded("testName", testMessage));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("setBooleanProperty");
        BooleanPropertyValueCopier testCopier = new BooleanPropertyValueCopier();
        try {
            testCopier.addToMessage("testName",
                                    OK_EXPECTED_DECODED_BOOLEANS[0],
                                    testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testAddToMessageInvokesSetBooleanProperty() {
        BooleanPropertyValueCopier testCopier = new BooleanPropertyValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++) {
            Message testMessage = messageBuilder.invokesNamedMethodAsExpected("setBooleanProperty",
                                                                              "testMessage"
                                                                                      + i,
                                                                              "testName",
                                                                              new Boolean(EXPECTED_DECODED_BOOLEANS[i]));
            testCopier.addToMessage("testName",
                                    OK_ENCODED_BOOLEANS[i],
                                    testMessage);
        }
    }

    public void testGetAsEncodedValueThrowsExceptionOnJMSException() {
        Message testMessage = messageBuilder.throwsJMSMessageOnMethodNamed("getBooleanProperty");
        BooleanPropertyValueCopier testCopier = new BooleanPropertyValueCopier();
        try {
            testCopier.getAsEncodedValue("testName", testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetAsEncodedValueReturnsValuesCorrectlyEncoded() {
        BooleanPropertyValueCopier testCopier = new BooleanPropertyValueCopier();
        for (int i = 0; i < EXPECTED_DECODED_BOOLEANS.length; i++) {
            Message testMessage = messageBuilder.returnsExpectedValueFromNamedMethod("getBooleanProperty",
                                                                                     "testMessage"
                                                                                             + i,
                                                                                     "testName",
                                                                                     new Boolean(EXPECTED_DECODED_BOOLEANS[i]));
            assertEquals("retrieved property was not encoded correctly",
                         OK_EXPECTED_DECODED_BOOLEANS[i],
                         testCopier.getAsEncodedValue("testName", testMessage));
        }
    }

    protected void setUp() throws Exception {
        messageBuilder = new MockMessageBuilder(Message.class);
    }

    private MockMessageBuilder messageBuilder;

    private static final String OK_ENCODED_BOOLEANS[] = CodecTestValues.OK_EXPECTED_DECODED_BOOLEANS;
    private static final boolean EXPECTED_DECODED_BOOLEANS[] = CodecTestValues.EXPECTED_DECODED_BOOLEANS;
    private static final String OK_EXPECTED_DECODED_BOOLEANS[] = CodecTestValues.OK_EXPECTED_DECODED_BOOLEANS;
}
