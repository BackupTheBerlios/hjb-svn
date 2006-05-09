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
package hjb.msg;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageEOFException;
import javax.jms.StreamMessage;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Stub;
import org.jmock.core.stub.StubSequence;

import hjb.http.cmd.HJBMessageWriter;
import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.msg.valuecopiers.MockMessageBuilder;
import hjb.msg.valuecopiers.streammessage.OrderedStreamMessageValueCopierTest;
import hjb.testsupport.MessageAttributeInvoker;

public class StreamMessageCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new StreamMessageCopier(),
                     new StreamMessageCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new StreamMessageCopier(),
                      new Object());
    }

    public void testCopyToJMSMessageThrowsHJBExceptionOnJMSException() {
        for (int i = 0; i < ORDERED_STREAM_MESSAGE_METHODS.length; i++) {
            Mock mockJMSMessage = mock(StreamMessage.class, "test" + i);
            String methodName = ORDERED_STREAM_MESSAGE_METHODS[i][1];
            mockJMSMessage.stubs()
                .method("setStringProperty")
                .with(eq("hjb_message_version"), eq("1.0"));
            mockJMSMessage.stubs()
                .method(methodName)
                .will(throwException(new JMSException("thrown as a test")));
            for (int j = 0; j < ORDERED_STREAM_MESSAGE_METHODS.length; j++) {
                if (j == i) continue;
                String okMethod = ORDERED_STREAM_MESSAGE_METHODS[j][1];
                mockJMSMessage.stubs().method(okMethod);
            }

            Message testJMSMessage = (StreamMessage) mockJMSMessage.proxy();
            HJBMessage testHJBMessage = createTestHJBStreamMessage();
            StreamMessageCopier c = new StreamMessageCopier();
            try {
                c.copyToJMSMessage(testHJBMessage, testJMSMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testCopyToJMSMessageCopiesStreamValuesOK() {
        Mock mockJMSMessage = mock(StreamMessage.class);
        mockJMSMessage.expects(once())
            .method("setStringProperty")
            .with(eq("hjb_message_version"), eq("1.0"));
        for (int i = 0; i < ORDERED_STREAM_MESSAGE_METHODS.length; i++) {
            String methodName = ORDERED_STREAM_MESSAGE_METHODS[i][1];
            Object expectedValue = ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][0];
            mockJMSMessage.expects(atLeastOnce())
                .method(methodName)
                .with(eq(expectedValue));
        }

        Message testJMSMessage = (StreamMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = createTestHJBStreamMessage();
        System.err.println(testHJBMessage);
        StreamMessageCopier c = new StreamMessageCopier();
        c.copyToJMSMessage(testHJBMessage, testJMSMessage);

        verify();
    }

    public void testCopyToHJBMessageThrowsHJBExceptionOnJMSException() {
        Mock mockJMSMessage = mock(StreamMessage.class);
        for (int i = 0; i < ORDERED_STREAM_MESSAGE_METHODS.length; i++) {
            String methodName = ORDERED_STREAM_MESSAGE_METHODS[i][0];
            mockJMSMessage.stubs()
                .method(methodName)
                .will(throwException(new JMSException("thrown as a test")));
        }
        attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);

        try {
            StreamMessage testJMSMessage = (StreamMessage) mockJMSMessage.proxy();
            StreamMessageCopier c = new StreamMessageCopier();
            c.copyToHJBMessage(testJMSMessage,
                               new HJBMessage(createEmptyHJBStreamMessageHeaders(),
                                              ""));
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCopyToHJBMessageCopiesStreamValuesOK() throws Exception {
        Mock mockJMSMessage = mock(StreamMessage.class);

        attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);
        Map expectedValues = new HashMap();
        for (int i = 0; i < ORDERED_STREAM_MESSAGE_METHODS.length; i++) {
            String methodName = ORDERED_STREAM_MESSAGE_METHODS[i][0];
            Object methodResult = ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][0];
            Stub[] invocations = new Stub[ORDERED_STREAM_MESSAGE_METHODS.length
                    + 1 - i];

            // This works because the test array's length is less
            // than the default buffer size used to copy the array
            if ("readBytes".equals(methodName)) {
                invocations = new Stub[ORDERED_STREAM_MESSAGE_METHODS.length
                        + 2 - i];
            }

            for (int j = 0; j < invocations.length; j++) {
                if (j == 0) {
                    if ("readBytes".equals(methodName)) {
                        Stub readBytesStub = messageBuilder.updateByteArrayWith((byte[]) methodResult);
                        invocations[j++] = readBytesStub;
                        // second invocation required to terminating reading of
                        // the
                        // byte array - this works because the test array's
                        // length
                        // is smaller than the default buffer size used to copy
                        // it,
                        // otherwise further invocations would be needed
                        invocations[j] = readBytesStub;
                    } else {
                        invocations[j] = returnValue(methodResult);
                    }
                } else if (j == (invocations.length - 1)) {
                    invocations[j] = throwException(new MessageEOFException("thrown as a test"));
                } else {
                    invocations[j] = throwException(new JMSException("thrown as a test"));
                }
            }
            mockJMSMessage.stubs()
                .method(methodName)
                .will(new StubSequence(invocations));
            expectedValues.put("" + i, ORDERED_OK_EXPECTED_DECODED_VALUES[i][0]);
        }

        StreamMessage testJMSMessage = (StreamMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBStreamMessageHeaders(),
                                                   "");
        StreamMessageCopier c = new StreamMessageCopier();
        c.copyToHJBMessage(testJMSMessage, testHJBMessage);
        assertExpectedValuesAreEncodedInTheMessage(testHJBMessage,
                                                   expectedValues);
    }

    protected HJBMessage createTestHJBStreamMessage() {
        Map headers = createEmptyHJBStreamMessageHeaders();
        Map encodedValues = new HashMap();
        for (int i = 0; i < ORDERED_STREAM_MESSAGE_METHODS.length; i++) {
            Object methodResult = ORDERED_OK_EXPECTED_DECODED_VALUES[i][0];
            String keyName = "" + i;
            encodedValues.put(keyName, methodResult);
        }
        return new HJBMessage(headers,
                              new HJBMessageWriter().asText(encodedValues));
    }

    protected Map createEmptyHJBStreamMessageHeaders() {
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE,
                    StreamMessage.class.getName());
        return headers;
    }

    protected void assertExpectedValuesAreEncodedInTheMessage(HJBMessage testHJBMessage,
                                                              Map expectedValues)
            throws Exception {
        String mapText = testHJBMessage.getBody();
        System.err.println(mapText);
        assertEquals("should contain the same values",
                     expectedValues,
                     new HJBMessageWriter().asMap(mapText));
    }

    protected void setUp() {
        attributeInvoker = new MessageAttributeInvoker();
        messageBuilder = new MockMessageBuilder(StreamMessage.class);
    }

    private MessageAttributeInvoker attributeInvoker;
    private MockMessageBuilder messageBuilder;

    private static final Object[][] ORDERED_EXPECTED_DECODED_VALUES_OBJECTS = CodecTestValues.ORDERED_EXPECTED_DECODED_VALUES_OBJECTS;
    private static final String[][] ORDERED_OK_EXPECTED_DECODED_VALUES = CodecTestValues.ORDERED_OK_EXPECTED_DECODED_VALUES;
    private static final String[][] ORDERED_STREAM_MESSAGE_METHODS = OrderedStreamMessageValueCopierTest.ORDERED_STREAM_MESSAGE_METHODS;
}
