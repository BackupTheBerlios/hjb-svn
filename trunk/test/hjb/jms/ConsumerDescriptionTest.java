package hjb.jms;

import javax.jms.MessageConsumer;

import org.jmock.Mock;

import hjb.http.cmd.PathNaming;

public class ConsumerDescriptionTest extends BaseDescriptionTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockConsumer = mock(MessageConsumer.class);       
        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
        try {
            new ConsumerDescription(testConsumer, -1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new ConsumerDescription(null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }
    
    public void testToStringIncludesTheConsumerIndex() {
        Mock mockConsumer = mock(MessageConsumer.class);
        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
        ConsumerDescription testDescription = new ConsumerDescription(testConsumer, 0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.CONSUMER);
    }
}
