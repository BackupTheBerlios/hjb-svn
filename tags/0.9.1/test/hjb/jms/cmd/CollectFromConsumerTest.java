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

import hjb.misc.HJBException;
import hjb.msg.HJBMessage;
import hjb.msg.MessageCopierFactory;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MessageAttributeInvoker;
import hjb.testsupport.MockMessengerBuilder;
import hjb.testsupport.MockSessionBuilder;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

import org.jmock.Mock;

public class CollectFromConsumerTest extends BaseHJBTestCase {

    public void testConstructionThrowsOnNullInputs() {
        try {
            new CollectFromConsumer(null, 1, 100, 0);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructionThrowsOnZeroOrLessTimeout() throws Exception {
        try {
            Mock mockConsumer = mock(MessageConsumer.class);
            MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
            new CollectFromConsumer(messengerBuilder.createForSessionWith(testConsumer),
                                    0,
                                    0,
                                    0);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
        try {
            Mock mockConsumer = mock(MessageConsumer.class);
            MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
            new CollectFromConsumer(messengerBuilder.createForSessionWith(testConsumer),
                                    0,
                                    -1,
                                    0);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteReceivesAMessage() throws Exception {
        Mock mockTextMessage = mock(TextMessage.class);
        mockTextMessage.expects(atLeastOnce())
            .method("getText")
            .will(returnValue("boo!"));
        mockTextMessage.expects(atLeastOnce()).method("acknowledge");
        MessageAttributeInvoker attributeInvoker = new MessageAttributeInvoker();
        attributeInvoker.invokesAllAccessors(mockTextMessage);
        attributeInvoker.invokesGetPropertyNamesReturnsNothing(mockTextMessage);

        Mock mockConsumer = mock(MessageConsumer.class);
        mockConsumer.stubs()
            .method("receive")
            .will(returnValue((TextMessage) mockTextMessage.proxy()));
        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();

        CollectFromConsumer command = new CollectFromConsumer(messengerBuilder.createForSessionWith(testConsumer),
                                                              0,
                                                              100,
                                                              3);
        command.execute();
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertEquals(3, command.getMessagesReceived().length);
        assertNotNull(command.getStatusMessage());
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
            Mock mockConsumer = mock(MessageConsumer.class);
            mockConsumer.stubs()
                .method("receive")
                .will(throwException(possibleExceptions[i]));
            MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();

            CollectFromConsumer command = new CollectFromConsumer(messengerBuilder.createForSessionWith(testConsumer),
                                                                  0,
                                                                  100,
                                                                  3);
            command.execute();
            assertFalse(command.isExecutedOK());
            assertTrue(command.isComplete());
            assertNotNull(command.getFault());
            assertEquals(command.getStatusMessage(), command.getFault()
                .getMessage());
        }
    }

    protected HJBMessage createTestTextHJBMessage() {
        HashMap headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE,
                    TextMessage.class.getName());
        return new HJBMessage(headers, "boo!");
    }
    
    protected void setUp() {
        messengerBuilder = new MockMessengerBuilder();
        sessionBuilder = new MockSessionBuilder();
    }

    protected MockMessengerBuilder messengerBuilder;
    protected MockSessionBuilder sessionBuilder;
}
