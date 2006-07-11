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
public class ConsumerDescription extends BaseJMSObjectDescription {

    public ConsumerDescription(MessageConsumer theConsumer, int consumerIndex) {
        super(consumerIndex, HJBStrings.INVALID_CONSUMER_INDEX);
        if (null == theConsumer) {
            throw new IllegalArgumentException(strings().needsANonNull(MessageConsumer.class));
        }
        this.theConsumer = theConsumer;
    }

    protected MessageConsumer getTheConsumer() {
        return theConsumer;
    }
    
    protected String getPathname() {
        return PathNaming.CONSUMER + "-" + getIndex();
    }

    private final MessageConsumer theConsumer;
}
