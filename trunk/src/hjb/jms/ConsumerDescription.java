package hjb.jms;

import javax.jms.MessageConsumer;

import hjb.http.cmd.PathNaming;
import hjb.misc.HJBStrings;

/**
 * <code>ConsumerDescription</code> is used to provide textual description of
 * JMS <code>MessageConsumers</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class ConsumerDescription {

    public ConsumerDescription(MessageConsumer theConsumer, int consumerIndex) {
        if (null == theConsumer) {
            throw new IllegalArgumentException(strings().needsANonNull(MessageConsumer.class));
        }
        if (consumerIndex < 0) {
            throw new IllegalArgumentException(strings().getString(HJBStrings.INVALID_CONSUMER_INDEX,
                                                                   new Integer(consumerIndex)));
        }
        this.theConsumer = theConsumer;
        this.consumerIndex = consumerIndex;
    }

    public String toString() {
        return PathNaming.CONSUMER + "-" + getConsumerIndex();
    }

    protected MessageConsumer getTheConsumer() {
        return theConsumer;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected int getConsumerIndex() {
        return consumerIndex;
    }

    private final MessageConsumer theConsumer;
    private final int consumerIndex;
    private static final HJBStrings STRINGS = new HJBStrings();
}
