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
import javax.jms.TextMessage;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.testsupport.MessageAttributeInvoker;

public class TextMessageCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new TextMessageCopier(),
                     new TextMessageCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new TextMessageCopier(),
                      new Object());
    }

    public void testCopyToHJBMessageThrowsHJBExceptionOnJMSException() {
        Mock mockJMSMessage = mock(TextMessage.class);
        mockJMSMessage.expects(once()).method("getText").will(throwException(new JMSException("thrown as a test")));
        attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);
        
        Message testJMSMessage = (TextMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBTextMessageHeaders(),
                                                   "test text");
        TextMessageCopier c = new TextMessageCopier();
        try {
            c.copyToHJBMessage(testJMSMessage, testHJBMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCopyToHJBMessageCopiesText() {
        Mock mockJMSMessage = mock(TextMessage.class);
        mockJMSMessage.expects(once()).method("getText").will(returnValue("jms Text"));
        attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);
        
        Message testJMSMessage = (TextMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBTextMessageHeaders(),
                                                   "hjb text");
        TextMessageCopier c = new TextMessageCopier();
        c.copyToHJBMessage(testJMSMessage, testHJBMessage);
        assertEquals("should be equal", "jms Text", testHJBMessage.getBody());
    }

    public void testCopyToJMSMessageThrowsHJBExceptionOnJMSException() {
        Mock mockJMSMessage = mock(TextMessage.class);
        mockJMSMessage.stubs().method("setText").with(eq("test text")).will(throwException(new JMSException("thrown as a test")));
        
        Message testJMSMessage = (TextMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBTextMessageHeaders(),
                                                   "test text");
        TextMessageCopier c = new TextMessageCopier();
        try {
            c.copyToJMSMessage(testHJBMessage, testJMSMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCopyToJMSMessage() {
        Mock mockJMSMessage = mock(TextMessage.class);
        mockJMSMessage.expects(once()).method("setText").with(eq("hjb text"));
        
        Message testJMSMessage = (TextMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBTextMessageHeaders(),
                                                   "hjb text");
        TextMessageCopier c = new TextMessageCopier();
        c.copyToJMSMessage(testHJBMessage, testJMSMessage);
    }

    protected Map createEmptyHJBTextMessageHeaders() {
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_CLASS,
                    TextMessage.class.getName());
        return headers;
    }

    protected void setUp() {
        attributeInvoker = new MessageAttributeInvoker();
    }

    private MessageAttributeInvoker attributeInvoker;
}
