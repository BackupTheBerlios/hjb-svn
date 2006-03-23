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

import java.util.*;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.OrderedTypedValueCodec;

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
        Mock mockTextMessage = mock(TextMessage.class);
        mockTextMessage.expects(once()).method("getText").will(throwException(new JMSException("thrown as a test")));
        expectsJmsGetAttributes(mockTextMessage);
        Message testMessage = (TextMessage) mockTextMessage.proxy();

        HJBMessage testMimeMessage = new HJBMessage(createEmptyTextHJBMessage(),
                                                    "test text");
        TextMessageCopier c = new TextMessageCopier();
        try {
            c.copyToHJBMessage(testMessage, testMimeMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCopyToHJBMessageCopiesText() {
        Mock mockTextMessage = mock(TextMessage.class);
        mockTextMessage.expects(once()).method("getText").will(returnValue("jms Text"));
        expectsJmsGetAttributes(mockTextMessage);
        Message testMessage = (TextMessage) mockTextMessage.proxy();

        HJBMessage testMimeMessage = new HJBMessage(createEmptyTextHJBMessage(),
                                                    "hjb text");
        TextMessageCopier c = new TextMessageCopier();
        c.copyToHJBMessage(testMessage, testMimeMessage);
        assertEquals("should be equal", "jms Text", testMimeMessage.getBody());
    }

    public void testCopyToJMSMessageThrowsHJBExceptionOnJMSException() {
        Mock mockTextMessage = mock(TextMessage.class);
        mockTextMessage.expects(once()).method("setText").with(eq("test text")).will(throwException(new JMSException("thrown as a test")));
        Message testMessage = (TextMessage) mockTextMessage.proxy();

        HJBMessage testMimeMessage = new HJBMessage(createEmptyTextHJBMessage(),
                                                    "test text");
        TextMessageCopier c = new TextMessageCopier();
        try {
            c.copyToJMSMessage(testMimeMessage, testMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCopyToJMSMessage() {
        Mock mockTextMessage = mock(TextMessage.class);
        mockTextMessage.expects(once()).method("setText").with(eq("hjb text"));
        Message testMessage = (TextMessage) mockTextMessage.proxy();

        HJBMessage testMimeMessage = new HJBMessage(createEmptyTextHJBMessage(),
                                                    "hjb text");
        TextMessageCopier c = new TextMessageCopier();
        c.copyToJMSMessage(testMimeMessage, testMessage);
    }

    protected Map createEmptyTextHJBMessage() {
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_CLASS,
                    TextMessage.class.getName());
        return headers;
    }

    protected void expectsJmsGetAttributes(Mock aMock) {
        OrderedTypedValueCodec codec = new OrderedTypedValueCodec();
        for (int i = 0; i < NON_DESTINATION_ATTRIBUTES.length; i++) {
            aMock.expects(atLeastOnce()).method(NON_DESTINATION_ATTRIBUTES[i][1]).will(returnValue(codec.decode(NON_DESTINATION_ATTRIBUTES[i][3])));
        }
        Mock destinationMock = mock(Destination.class);
        Destination testDestination = (Destination) destinationMock.proxy();
        for (int i = 0; i < DESTINATION_ATTRIBUTES.length; i++) {
            aMock.expects(atLeastOnce()).method(DESTINATION_ATTRIBUTES[i][1]).will(returnValue(testDestination));
        }
        aMock.expects(atLeastOnce()).method("getPropertyNames").will(returnValue(Collections.enumeration(Collections.EMPTY_LIST)));
    }

    public static final String[][] NON_DESTINATION_ATTRIBUTES = AttributeCopierAssistantTest.NON_DESTINATION_ATTRIBUTES;
    public static final String[][] DESTINATION_ATTRIBUTES = AttributeCopierAssistantTest.DESTINATION_ATTRIBUTES;
}
