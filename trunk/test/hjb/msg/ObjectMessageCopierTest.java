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

import hjb.misc.HJBException;
import hjb.msg.codec.ByteArrayCodec;
import hjb.testsupport.MessageAttributeInvoker;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class ObjectMessageCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new ObjectMessageCopier(),
                     new ObjectMessageCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new ObjectMessageCopier(),
                      new Object());
    }

    public void testCopyToJMSMessageThrowsHJBExceptionOnPossibleExceptions()
            throws Exception {
        Exception[] possibleExceptions = {
            new JMSException("thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Mock mockJMSMessage = mock(ObjectMessage.class);
            mockJMSMessage.stubs()
                .method("setStringProperty")
                .with(eq("hjb_message_version"), eq("1.0"));
            mockJMSMessage.stubs()
                .method("setObject")
                .will(throwException(possibleExceptions[i]));

            Message testJMSMessage = (ObjectMessage) mockJMSMessage.proxy();
            HJBMessage testHJBMessage = createTestHJBObjectMessage();
            ObjectMessageCopier c = new ObjectMessageCopier();
            try {
                c.copyToJMSMessage(testHJBMessage, testJMSMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testCopyToJMSMessageCopiesObjectOK() throws Exception {
        Mock mockJMSMessage = mock(ObjectMessage.class);
        mockJMSMessage.stubs()
            .method("setStringProperty")
            .with(eq("hjb_message_version"), eq("1.0"));

        mockJMSMessage.expects(once())
            .method("setObject")
            .with(eq(getTestSerializable()));

        Message testJMSMessage = (ObjectMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = createTestHJBObjectMessage();
        ObjectMessageCopier c = new ObjectMessageCopier();
        c.copyToJMSMessage(testHJBMessage, testJMSMessage);

        verify();
    }

    public void testCopyToHJBMessageThrowsHJBExceptionOnPossibleExceptions() {
        Exception[] possibleExceptions = {
            new JMSException("thrown as a test"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Mock mockJMSMessage = mock(ObjectMessage.class);
            mockJMSMessage.expects(once())
                .method("getObject")
                .will(throwException(possibleExceptions[i]));
            attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);

            Message testJMSMessage = (ObjectMessage) mockJMSMessage.proxy();
            HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBObjectMessageHeaders(),
                                                       "");
            ObjectMessageCopier c = new ObjectMessageCopier();
            try {
                c.copyToHJBMessage(testJMSMessage, testHJBMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testCopyToHJBMessageCopiesObjectCorrectly() throws Exception {
        Mock mockJMSMessage = mock(ObjectMessage.class);
        attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);
        mockJMSMessage.stubs()
            .method("getObject")
            .will(returnValue(getTestSerializable()));

        Message testJMSMessage = (ObjectMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBObjectMessageHeaders(),
                                                   "");
        ObjectMessageCopier c = new ObjectMessageCopier();
        c.copyToHJBMessage(testJMSMessage, testHJBMessage);
        System.err.println(testHJBMessage);
        assertEquals(createTestHJBObjectMessage().getBody(),
                     testHJBMessage.getBody());
    }

    protected HJBMessage createTestHJBObjectMessage() throws Exception {
        Date testObject = getTestSerializable();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(out);
        objectOut.writeObject(testObject);
        return new HJBMessage(createEmptyHJBObjectMessageHeaders(),
                              new ByteArrayCodec().encode(out.toByteArray()));
    }

    protected Date getTestSerializable() {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(2005, 6, 6, 11, 30, 0);
        Date testObject = c.getTime();
        return testObject;
    }

    protected Map createEmptyHJBObjectMessageHeaders() {
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE,
                    ObjectMessage.class.getName());
        return headers;
    }

    protected void setUp() {
        attributeInvoker = new MessageAttributeInvoker();
    }

    private MessageAttributeInvoker attributeInvoker;
}
