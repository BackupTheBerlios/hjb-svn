package hjb.jms;

import javax.jms.JMSException;
import javax.jms.MessageProducer;

import hjb.http.cmd.PathNaming;
import hjb.misc.HJBStrings;

/**
 * <code>ProducerDescription</code> is used to provide textual description of
 * the JMS <code>Producers</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class ProducerDescription {

    public ProducerDescription(MessageProducer theProducer, int producerIndex) {
        if (null == theProducer) {
            throw new IllegalArgumentException(strings().needsANonNull(MessageProducer.class));
        }
        if (producerIndex < 0) {
            throw new IllegalArgumentException(strings().getString(HJBStrings.INVALID_PRODUCER_INDEX,
                                                                   new Integer(producerIndex)));
        }
        this.theProducer = theProducer;
        this.producerIndex = producerIndex;
    }

    public String toString() {
        return PathNaming.PRODUCER + "-" + getProducerIndex()
                + getExtraInformation();
    }

    protected String getExtraInformation() {
        try {
            String producerDestination = (null == getTheProducer().getDestination()) ? strings().getString(HJBStrings.ANY_PRODUCER)
                    : "" + getTheProducer().getDestination();
            return strings().getString(HJBStrings.PRODUCER_DESCRIPTION,
                                       producerDestination,
                                       new Integer(getTheProducer().getPriority()));
        } catch (JMSException e) {
            return "";
        }
    }

    protected MessageProducer getTheProducer() {
        return theProducer;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected int getProducerIndex() {
        return producerIndex;
    }

    private final MessageProducer theProducer;
    private final int producerIndex;
    private static final HJBStrings STRINGS = new HJBStrings();
}
