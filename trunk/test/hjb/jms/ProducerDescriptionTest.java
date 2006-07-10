package hjb.jms;

import javax.jms.Destination;
import javax.jms.MessageProducer;

import org.jmock.Mock;

import hjb.http.cmd.PathNaming;

public class ProducerDescriptionTest extends BaseDescriptionTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockProducer = mock(MessageProducer.class);       
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        try {
            new ProducerDescription(testProducer, -1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new ProducerDescription(null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }
    
    public void testToStringIncludesThePriority() {
        Mock mockProducer = mock(MessageProducer.class);
        mockProducer.stubs().method("getPriority").will(returnValue(-4));
        mockProducer.stubs().method("getDestination").will(returnValue(null));
        
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        ProducerDescription testDescription = new ProducerDescription(testProducer, 0);
        assertContains(testDescription.toString(), "-4");
        assertContains(testDescription.toString(), PathNaming.PRODUCER);
    }
    
    public void testToStringIncludesTheDestinationWhenAvailable() {
        Mock mockProducer = mock(MessageProducer.class);
        Mock mockDestination = mock(Destination.class);
        
        mockProducer.stubs().method("getPriority").will(returnValue(-3));
        mockProducer.stubs().method("getDestination").will(returnValue((Destination) mockDestination.proxy()));
        
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        ProducerDescription testDescription = new ProducerDescription(testProducer, 0);
        assertContains(testDescription.toString(), "-3");
        assertContains(testDescription.toString(), PathNaming.PRODUCER);
        assertContains(testDescription.toString(), mockDestination.toString());
    }


}
