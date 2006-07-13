package hjb.jms.info;

import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.MessageProducer;

import hjb.misc.HJBConstants;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

/**
 * <code>ProducerDescription</code> is used to provide a description of JMS
 * <code>Producers</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class ProducerDescription extends BaseJMSObjectDescription {

    public ProducerDescription(MessageProducer theProducer, int producerIndex) {
        super(producerIndex, HJBStrings.INVALID_PRODUCER_INDEX);
        if (null == theProducer) {
            throw new IllegalArgumentException(strings().needsANonNull(MessageProducer.class));
        }
        this.theProducer = theProducer;
    }

    protected Map attributesAsAMap() {
        Map result = new TreeMap();
        try {
            result.put(HJBConstants.DISABLE_MESSAGE_IDS,
                       getCodec().encode(new Boolean(getTheProducer().getDisableMessageID())));
            result.put(HJBConstants.DISABLE_TIMESTAMPS,
                       getCodec().encode(new Boolean(getTheProducer().getDisableMessageTimestamp())));
            result.put(HJBConstants.TIME_TO_LIVE,
                       getCodec().encode(new Long(getTheProducer().getTimeToLive())));
            result.put(HJBConstants.PRIORITY,
                       getCodec().encode((new Integer(getTheProducer().getPriority()))));
            result.put(HJBConstants.DELIVERY_MODE,
                       getCodec().encode((new Integer(getTheProducer().getDeliveryMode()))));
        } catch (JMSException e) {}
        return result;
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

    protected String getBaseName() {
        return PathNaming.PRODUCER + "-" + getIndex();
    }

    protected MessageProducer getTheProducer() {
        return theProducer;
    }

    private final MessageProducer theProducer;
}
