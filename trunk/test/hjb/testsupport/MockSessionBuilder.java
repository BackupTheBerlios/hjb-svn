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
package hjb.testsupport;

import javax.jms.*;

import org.jmock.Mock;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;

import hjb.misc.MessageProducerArguments;

public class MockSessionBuilder {

    public Mock createMockSession() {
        Mock mockSession = new Mock(Session.class);
        mockSession.stubs()
            .method("getTransacted")
            .will(new ReturnStub(new Boolean(false)));
        setupDefaultReturnValues(mockSession);
        return mockSession;
    }

    public MessageProducerArguments defaultProducerArguments() {
        return new MessageProducerArguments(false, false, null, null, null);
    }

    public Mock createMockSessionThatThrowsJMSOn(String methodName) {
        Mock result = new Mock(Session.class);
        result.stubs()
            .method(methodName)
            .will(new ThrowStub(new JMSException("thrown as a test")));
        return result;
    }

    public void setupDefaultReturnValues(Mock mockSession) {
        setupDefaultConsumer(mockSession);
        setupDefaultProducer(mockSession);
        setupDefaultSubscriber(mockSession);
        setupDefaultBrowser(mockSession);
    }

    protected void setupDefaultBrowser(Mock mockSession) {
        Mock mockBrowser = new Mock(QueueBrowser.class);
        Mock mockQueue = new Mock(Queue.class);
        mockBrowser.stubs()
            .method("getQueue")
            .will(new ReturnStub((Queue) mockQueue.proxy()));
        mockBrowser.stubs()
            .method("getMessageSelector")
            .will(new ReturnStub("testSelector"));
        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();
        mockSession.stubs()
            .method("createBrowser")
            .will(new ReturnStub(testBrowser));
    }

    protected void setupDefaultConsumer(Mock mockSession) {
        Mock mockConsumer = new Mock(MessageConsumer.class);
        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
        mockSession.stubs()
            .method("createConsumer")
            .will(new ReturnStub(testConsumer));
        mockConsumer.stubs()
            .method("getMessageSelector")
            .will(new ReturnStub("testSelector"));

    }

    protected void setupDefaultProducer(Mock mockSession) {
        Mock mockProducer = new Mock(MessageProducer.class);
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        mockSession.stubs()
            .method("createProducer")
            .will(new ReturnStub(testProducer));
        mockProducer.stubs().method("setDisableMessageTimestamp");
        mockProducer.stubs().method("setDisableMessageID");
        mockProducer.stubs()
            .method("getPriority")
            .will(new ReturnStub(new Integer(-4)));
        mockProducer.stubs()
            .method("getTimeToLive")
            .will(new ReturnStub(new Long(1)));
        mockProducer.stubs()
            .method("getDeliveryMode")
            .will(new ReturnStub(new Integer(DeliveryMode.NON_PERSISTENT)));
        mockProducer.stubs()
            .method("getDestination")
            .will(new ReturnStub(null));
        mockProducer.stubs()
            .method("getDisableMessageID")
            .will(new ReturnStub(new Boolean(false)));
        mockProducer.stubs()
            .method("getDisableMessageTimestamp")
            .will(new ReturnStub(new Boolean(false)));
    }

    protected void setupDefaultSubscriber(Mock mockSession) {
        Mock mockTopic = new Mock(Topic.class);
        mockTopic.stubs()
            .method("getTopicName")
            .will(new ReturnStub("testTopic"));
        Mock mockSubscriber = new Mock(TopicSubscriber.class);
        mockSubscriber.stubs()
            .method("getTopic")
            .will(new ReturnStub((Topic) mockTopic.proxy()));
        mockSubscriber.stubs()
            .method("getMessageSelector")
            .will(new ReturnStub("testSelector"));
        mockSubscriber.stubs()
            .method("getNoLocal")
            .will(new ReturnStub(new Boolean(false)));

        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        mockSession.stubs()
            .method("createDurableSubscriber")
            .will(new ReturnStub(testSubscriber));
    }
}
