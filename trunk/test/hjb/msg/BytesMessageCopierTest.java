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

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.StreamMessage;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.misc.YetAnotherBase64Test;
import hjb.msg.codec.ByteArrayCodec;
import hjb.msg.valuecopiers.MockMessageBuilder;
import hjb.testsupport.MessageAttributeInvoker;

public class BytesMessageCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new BytesMessageCopier(),
                     new BytesMessageCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new BytesMessageCopier(),
                      new Object());
    }

    public void testCopyToJMSMessageThrowsHJBExceptionOnJMSException() {
        Mock mockJMSMessage = mock(BytesMessage.class);
        mockJMSMessage.stubs()
            .method("setStringProperty")
            .with(eq("hjb.core.message-version"), eq("1.0"));
        mockJMSMessage.stubs()
            .method("writeBytes")
            .will(throwException(new JMSException("thrown as a test")));

        Message testJMSMessage = (BytesMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = createTestHJBBytesMessage();
        BytesMessageCopier c = new BytesMessageCopier();
        try {
            c.copyToJMSMessage(testHJBMessage, testJMSMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCopyToJMSMessageCopiesByteArrayOK() {
        Mock mockJMSMessage = mock(BytesMessage.class);
        mockJMSMessage.expects(once())
            .method("setStringProperty")
            .with(eq("hjb.core.message-version"), eq("1.0"));
        mockJMSMessage.expects(once())
            .method("writeBytes")
            .with(eq(YetAnotherBase64Test.DECODED_SOURCE.getBytes()));

        Message testJMSMessage = (BytesMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = createTestHJBBytesMessage();
        BytesMessageCopier c = new BytesMessageCopier();
        c.copyToJMSMessage(testHJBMessage, testJMSMessage);

        verify();
    }

    public void testCopyToHJBMessageThrowsHJBExceptionOnJMSException() {
        Mock mockJMSMessage = mock(BytesMessage.class);
        mockJMSMessage.expects(once())
            .method("readBytes")
            .will(throwException(new JMSException("thrown as a test")));
        attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);

        Message testJMSMessage = (BytesMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBBytesMessageHeaders(),
                                                   "");
        BytesMessageCopier c = new BytesMessageCopier();
        try {
            c.copyToHJBMessage(testJMSMessage, testHJBMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCopyToHJBMessageCopiesByteArrayCorrectly() {
        Mock mockJMSMessage = mock(BytesMessage.class);
        attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);
        byte[] testBytes = YetAnotherBase64Test.DECODED_SOURCE.getBytes();
        mockJMSMessage.stubs()
            .method("readBytes")
            .will(messageBuilder.updateByteArrayWith(testBytes));

        Message testJMSMessage = (BytesMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBBytesMessageHeaders(),
                                                   "");
        BytesMessageCopier c = new BytesMessageCopier();
        c.copyToHJBMessage(testJMSMessage, testHJBMessage);
        System.err.println(testHJBMessage);
        assertEquals(new ByteArrayCodec().encode(testBytes),
                     testHJBMessage.getBody());
    }

    protected HJBMessage createTestHJBBytesMessage() {
        return new HJBMessage(createEmptyHJBBytesMessageHeaders(),
                              new ByteArrayCodec().encode(YetAnotherBase64Test.DECODED_SOURCE.getBytes()));
    }

    protected Map createEmptyHJBBytesMessageHeaders() {
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_CLASS,
                    BytesMessage.class.getName());
        return headers;
    }

    protected void setUp() {
        attributeInvoker = new MessageAttributeInvoker();
        messageBuilder = new MockMessageBuilder(StreamMessage.class);
    }

    private MessageAttributeInvoker attributeInvoker;
    private MockMessageBuilder messageBuilder;
}
