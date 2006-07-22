package hjb.jms.info;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;

import org.jmock.Mock;

import hjb.misc.PathNaming;

public class ProducerDescriptionTest extends BaseHJBTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockProducer = mock(MessageProducer.class);
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        try {
            new ProducerDescription(testProducer, -1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new ProducerDescription(null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testToStringIncludesThePriority() {
        Mock mockProducer = mock(MessageProducer.class);
        mockProducer.stubs().method("getPriority").will(returnValue(-4));
        mockProducer.stubs().method("getDestination").will(returnValue(null));

        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        ProducerDescription testDescription = new ProducerDescription(testProducer,
                                                                      0);
        assertContains(testDescription.toString(), "-4");
        assertContains(testDescription.toString(), PathNaming.PRODUCER);
    }

    public void testToStringIncludesTheDestinationWhenAvailable() {
        Mock mockProducer = mock(MessageProducer.class);
        Mock mockDestination = mock(Destination.class);

        mockProducer.stubs().method("getPriority").will(returnValue(-3));
        mockProducer.stubs()
            .method("getDestination")
            .will(returnValue((Destination) mockDestination.proxy()));

        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        ProducerDescription testDescription = new ProducerDescription(testProducer,
                                                                      0);
        assertContains(testDescription.toString(), "-3");
        assertContains(testDescription.toString(), PathNaming.PRODUCER);
        assertContains(testDescription.toString(), mockDestination.toString());
    }

    public void testLongDescriptionOutputsAllValues() {
        Mock mockProducer = mock(MessageProducer.class);
        Mock mockDestination = mock(Destination.class);

        mockProducer.stubs()
            .method("getDeliveryMode")
            .will(returnValue(DeliveryMode.NON_PERSISTENT));
        mockProducer.stubs().method("getPriority").will(returnValue(-3));
        mockProducer.stubs()
            .method("getDisableMessageTimestamp")
            .will(returnValue(true));
        mockProducer.stubs()
            .method("getDisableMessageID")
            .will(returnValue(false));
        mockProducer.stubs()
            .method("getTimeToLive")
            .will(returnValue(10002000L));
        mockProducer.stubs()
            .method("getDestination")
            .will(returnValue((Destination) mockDestination.proxy()));

        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        ProducerDescription testDescription = new ProducerDescription(testProducer,
                                                                      0);
        
        String expectedOutput = testDescription.toString() + CR
                + "delivery-mode=(int 1)" + CR + "disable-message-ids=(boolean false)" + CR
                + "disable-timestamps=(boolean true)" + CR + "priority=(int -3)" + CR
                + "time-to-live=(long 10002000)";

        assertContains(testDescription.longDescription(), "-3");
        assertContains(testDescription.longDescription(), PathNaming.PRODUCER);
        assertEquals(expectedOutput, testDescription.longDescription());
        System.err.println(testDescription.longDescription());
    }

}
