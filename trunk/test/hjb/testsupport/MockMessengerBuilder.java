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
