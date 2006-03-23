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

public class MockSessionBuilder {

    public Session createMockSession() {
        Mock mockSession = new Mock(Session.class);
        setupDefaultReturnValues(mockSession);
        return (Session) mockSession.proxy();
    }

    public Mock createMockSessionThatThrowsJMSOn(String methodName) {
        Mock result = new Mock(Session.class);
        result.stubs().method(methodName).will(new ThrowStub(new JMSException("thrown as a test")));
        return result;
    }

    public void setupDefaultReturnValues(Mock mockSession) {
        Mock mockConsumer = new Mock(MessageConsumer.class);
        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
        mockSession.stubs().method("createConsumer").will(new ReturnStub(testConsumer));

        Mock mockProducer = new Mock(MessageProducer.class);
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        mockSession.stubs().method("createProducer").will(new ReturnStub(testProducer));

        Mock mockSubscriber = new Mock(TopicSubscriber.class);
        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        mockSession.stubs().method("createDurableSubscriber").will(new ReturnStub(testSubscriber));

        Mock mockBrowser = new Mock(QueueBrowser.class);
        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();
        mockSession.stubs().method("createBrowser").will(new ReturnStub(testBrowser));
    }
}
