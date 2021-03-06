package hjb.jms.info;

import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;

import hjb.misc.HJBConstants;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

/**
 * <code>ConsumerDescription</code> is used to provide a description of JMS
 * <code>MessageConsumers</code> in HJB status messages and logs.
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

    protected Map attributesAsAMap() {
        Map result = new TreeMap();
        try {
            result.put(HJBConstants.MESSAGE_SELECTOR,
                       (null == getTheConsumer().getMessageSelector() ? ""
                               : getTheConsumer().getMessageSelector()));
        } catch (JMSException e) {}
        return result;
    }

    protected String getBaseName() {
        return PathNaming.CONSUMER + "-" + getIndex();
    }

    private final MessageConsumer theConsumer;
}
