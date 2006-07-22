package hjb.jms.info;

import javax.jms.MessageConsumer;

import org.jmock.Mock;

import hjb.misc.PathNaming;
import hjb.testsupport.BaseHJBTestCase;

public class ConsumerDescriptionTest extends BaseHJBTestCase {

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
    
    public void testLongDescriptionOutputsAllValues() {
        Mock mockConsumer = mock(MessageConsumer.class);
        
        mockConsumer.stubs()
            .method("getMessageSelector")
            .will(returnValue("testSelector"));

        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
        ConsumerDescription testDescription = new ConsumerDescription(testConsumer,
                                                                      0);
        
        String expectedOutput = testDescription.toString() + CR
                + "message-selector=testSelector";

        assertContains(testDescription.longDescription(), "0");
        assertContains(testDescription.longDescription(), PathNaming.CONSUMER);
        assertEquals(expectedOutput, testDescription.longDescription());
        System.err.println(testDescription.longDescription());
    }

}
