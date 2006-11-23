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
package hjb.jms.cmd;

import hjb.http.cmd.HJBMessageWriter;
import hjb.jms.HJBMessenger;
import hjb.misc.HJBException;
import hjb.msg.HJBMessage;
import hjb.msg.MessageCopierFactory;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MessageAttributeInvoker;
import hjb.testsupport.MockMessengerBuilder;
import hjb.testsupport.MockSessionBuilder;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.TopicSubscriber;

import org.jmock.Mock;

public class ReceiveFromSubscriberTest extends BaseHJBTestCase {

    public void testCommitSessionThrowsOnNullInputs() {
        try {
            new ReceiveFromSubscriber(null, 1);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }


    public void testExecuteReceivesAMessage() throws Exception {
        Mock mockTextMessage = mock(TextMessage.class);
        mockTextMessage.expects(once())
            .method("getText")
            .will(returnValue("boo!"));
        mockTextMessage.expects(once()).method("acknowledge");
        MessageAttributeInvoker attributeInvoker = new MessageAttributeInvoker();
        attributeInvoker.invokesAllAccessors(mockTextMessage);
        attributeInvoker.invokesGetPropertyNamesReturnsNothing(mockTextMessage);
        Message testMessage = (TextMessage) mockTextMessage.proxy();

        Mock mockSubscriber = mock(TopicSubscriber.class);
        mockSubscriber.stubs().method("getTopic").will(returnValue(null));
        mockSubscriber.stubs().method("getNoLocal").will(returnValue(false));
        mockSubscriber.expects(once())
            .method("receive")
            .will(returnValue(testMessage));
        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        
        HJBMessenger m = messengerBuilder.createForSessionWith(testSubscriber);
        ReceiveFromSubscriber command = new ReceiveFromSubscriber(m, 0);
        command.execute();
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
        assertNotNull(command.getMessageReceived());
        System.err.println(new HJBMessageWriter().asText(command.getMessageReceived()));
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testExecuteReportsAFaultOnPossibleExceptions() throws Exception {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("thrown as a test"),
                new RuntimeException("fire in the server room"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            Mock mockSubscriber = mock(TopicSubscriber.class);
            mockSubscriber.expects(once())
                .method("receive")
                .will(throwException(possibleExceptions[i]));
            TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();

            HJBMessenger m = messengerBuilder.createForSessionWith(testSubscriber);
            ReceiveFromSubscriber command = new ReceiveFromSubscriber(m, 0);
            command.execute();
            assertFalse(command.isExecutedOK());
            assertTrue(command.isComplete());
            assertNotNull(command.getFault());
            assertEquals(command.getStatusMessage(), command.getFault()
                .getMessage());
        }
    }

    protected void setUp() throws Exception {
        messengerBuilder = new MockMessengerBuilder();
        sessionBuilder = new MockSessionBuilder();
    }
    
    protected HJBMessage createTestTextHJBMessage() {
        HashMap headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE,
                    TextMessage.class.getName());
        return new HJBMessage(headers, "boo!");
    }

    protected MockSessionBuilder sessionBuilder;
    protected MockMessengerBuilder messengerBuilder;
}
