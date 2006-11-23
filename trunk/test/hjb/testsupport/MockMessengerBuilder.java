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

import hjb.jms.HJBMessenger;
import hjb.jms.HJBRoot;
import hjb.jms.HJBSession;
import hjb.misc.Clock;

import java.io.File;

import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.jmock.Mock;
import org.jmock.core.stub.ReturnStub;

public class MockMessengerBuilder {

    public HJBMessenger createForSessionWith(TopicSubscriber testSubscriber)
            throws Exception { 
        Mock mockSession = new MockSessionBuilder().createMockSession();
        mockSession.stubs()
            .method("createDurableSubscriber")
            .will(new ReturnStub(testSubscriber));

        File testRootPath = File.createTempFile("test", null).getParentFile();
        HJBRoot root = new HJBRoot(testRootPath, new Clock());
        Mock mockTopic = new Mock(Topic.class);
        Topic testTopic = (Topic) mockTopic.proxy();

        new MockHJBRuntime().make1SessionAnd1Destination(root,
                                            (Session) mockSession.proxy(),
                                            "testProvider",
                                            "testFactory",
                                            "testDestination",
                                            testTopic);
        HJBSession testSession = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0)
            .getSession(0);
        new MockSessionBuilder().create1Subscriber(testSession);
        return new HJBMessenger(testSession);
    }

    public HJBMessenger createForSessionWith(MessageConsumer testConsumer) throws Exception {
        Mock mockSession = new MockSessionBuilder().createMockSession();
        mockSession.stubs()
            .method("createConsumer")
            .will(new ReturnStub(testConsumer));
        File testRootPath = File.createTempFile("test", null).getParentFile();
        HJBRoot root = new HJBRoot(testRootPath, new Clock());
        new MockHJBRuntime().make1Session(root,
                             (Session) mockSession.proxy(),
                             "testProvider",
                             "testFactory");
        HJBSession testSession = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0)
            .getSession(0);
        new MockSessionBuilder().create1Consumer(testSession);
        return new HJBMessenger(testSession);
    }

}
