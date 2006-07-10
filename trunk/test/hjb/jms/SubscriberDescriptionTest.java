package hjb.jms;

import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.jmock.Mock;

import hjb.http.cmd.PathNaming;

public class SubscriberDescriptionTest extends BaseDescriptionTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockSubscriber = mock(TopicSubscriber.class);       
        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        try {
            new SubscriberDescription(testSubscriber, -1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new SubscriberDescription(null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }
    
    public void testToStringIncludesTheNoLocal() {
        Mock mockSubscriber = mock(TopicSubscriber.class);
        mockSubscriber.stubs().method("getNoLocal").will(returnValue(false));
        mockSubscriber.stubs().method("getTopic").will(returnValue(null));
        
        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        SubscriberDescription testDescription = new SubscriberDescription(testSubscriber, 0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.SUBSCRIBER);
    }
    
    public void testToStringIncludesTheTopicWhenAvailable() {
        Mock mockSubscriber = mock(TopicSubscriber.class);
        Mock mockTopic = mock(Topic.class);
        
        mockSubscriber.stubs().method("getNoLocal").will(returnValue(false));
        mockSubscriber.stubs().method("getTopic").will(returnValue((Topic) mockTopic.proxy()));
        
        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        SubscriberDescription testDescription = new SubscriberDescription(testSubscriber, 0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), "false");
        assertContains(testDescription.toString(), PathNaming.SUBSCRIBER);
        assertContains(testDescription.toString(), mockTopic.toString());
    }

}
