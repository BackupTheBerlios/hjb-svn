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

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.OrderedTypedValueCodec;
import hjb.testsupport.MessageAttributeInvoker;

public class AttributeCopierTest extends MockObjectTestCase {

    public AttributeCopierTest() {

        attributeInvoker = new MessageAttributeInvoker();
    }

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new AttributeCopier(),
                     new AttributeCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new AttributeCopier(),
                      new Object());
    }

    public void testCopyToJMSMessageDoesNothingWhenHeadersAreAbsent() {
        Mock mockMessage = mock(Message.class);
        for (int i = 0; i < ALL_ATTRIBUTES.length; i++) {
            mockMessage.expects(never()).method(ALL_ATTRIBUTES[i][1]);
        }
        for (int i = 0; i < ALL_ATTRIBUTES.length; i++) {
            mockMessage.expects(never()).method(ALL_ATTRIBUTES[i][2]);
        }

        Message testMessage = (Message) mockMessage.proxy();
        HJBMessage mimeMessage = new HJBMessage(new HashMap(), "test");
        AttributeCopier copier = new AttributeCopier();
        copier.copyToJMSMessage(mimeMessage, testMessage);
    }

    public void testCopyToJMSMessageThrowsHJBExceptionOnJMSException() {
        AttributeCopier copier = new AttributeCopier();
        OrderedTypedValueCodec orderedCodec = new OrderedTypedValueCodec();

        for (int i = 0; i < ALL_ATTRIBUTES.length; i++) {
            if (null == ALL_ATTRIBUTES[i][3]) continue;
            Mock mockMessage = mock(Message.class);
            Map testHeaders = new HashMap();
            testHeaders.put(ALL_ATTRIBUTES[i][0], ALL_ATTRIBUTES[i][3]);
            HJBMessage mimeMessage = new HJBMessage(testHeaders, "test");
            mockMessage.expects(once())
                .method(ALL_ATTRIBUTES[i][2])
                .with(eq(orderedCodec.decode(ALL_ATTRIBUTES[i][3])))
                .will(throwException(new JMSException("thrown as a test")));
            Message testMessage = (Message) mockMessage.proxy();
            try {
                copier.copyToJMSMessage(mimeMessage, testMessage);
            } catch (HJBException e) {}
        }
    }

    public void testCopyToJMSMessageCopiesCorrectValuesSuccessfully() {
        OrderedTypedValueCodec orderedCodec = new OrderedTypedValueCodec();

        for (int i = 0; i < ALL_ATTRIBUTES.length; i++) {
            Mock mockMessage = mock(Message.class);
            Map testHeaders = new HashMap();
            testHeaders.put(ALL_ATTRIBUTES[i][0], ALL_ATTRIBUTES[i][3]);
            HJBMessage mimeMessage = new HJBMessage(testHeaders, "test");
            mockMessage.expects(once())
                .method(ALL_ATTRIBUTES[i][2])
                .with(eq(orderedCodec.decode(ALL_ATTRIBUTES[i][3])));

            Message testMessage = (Message) mockMessage.proxy();
            AttributeCopier copier = new AttributeCopier();
            copier.copyToJMSMessage(mimeMessage, testMessage);
        }

    }

    public void testCopyToJMSMessageDoesNotCopyDestinationPropertyHeaders() {

        for (int i = 0; i < DESTINATION_ATTRIBUTES.length; i++) {
            Mock mockMessage = mock(Message.class);
            Map testHeaders = new HashMap();
            testHeaders.put(DESTINATION_ATTRIBUTES[i][0], "");
            HJBMessage mimeMessage = new HJBMessage(testHeaders, "test");
            mockMessage.expects(never()).method(DESTINATION_ATTRIBUTES[i][2]);

            Message testMessage = (Message) mockMessage.proxy();
            AttributeCopier copier = new AttributeCopier();
            copier.copyToJMSMessage(mimeMessage, testMessage);
        }
    }

    public void testCopyToHJBMessageDetectsTypesCorrectly() {
        for (int n = 0; n < ALL_ATTRIBUTES.length; n++) {
            Mock mockMessage = mock(Message.class, "message" + n);

            attributeInvoker.invokesAccessorsInOrderStoppingAfterN(mockMessage,
                                                                   n);
            mockMessage.expects(once())
                .method(ALL_ATTRIBUTES[n][1])
                .will(throwException(new JMSException("thrown as a test")));
            Message testMessage = (Message) mockMessage.proxy();
            try {
                HJBMessage mimeMessage = new HJBMessage(new HashMap(), "test");
                AttributeCopier copier = new AttributeCopier();
                copier.copyToHJBMessage(testMessage, mimeMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testCopyToHJBMessageCopiesCorrectValues() {
        AttributeCopier copier = new AttributeCopier();
        Mock mockMessage = mock(Message.class, "message");
        attributeInvoker.invokesAllAccessors(mockMessage);

        Message testMessage = (Message) mockMessage.proxy();
        HJBMessage mimeMessage = new HJBMessage(new HashMap(), "test");
        copier.copyToHJBMessage(testMessage, mimeMessage);
        assertEquals("should be the same",
                     attributeInvoker.getOKEncodedPropertyValues(),
                     mimeMessage.getHeaders());
    }

    public static final String[][] ALL_ATTRIBUTES = AttributeCopierAssistantTest.NON_DESTINATION_ATTRIBUTES;
    public static final String[][] DESTINATION_ATTRIBUTES = AttributeCopierAssistantTest.DESTINATION_ATTRIBUTES;

    private MessageAttributeInvoker attributeInvoker;
}
