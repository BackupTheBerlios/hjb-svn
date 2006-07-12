package hjb.jms;

import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.MessageProducer;

import hjb.http.HJBServletConstants;
import hjb.http.cmd.PathNaming;
import hjb.misc.HJBStrings;

/**
 * <code>ProducerDescription</code> is used to provide textual description of
 * the JMS <code>Producers</code> in HJB status messages and logs.
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
            result.put(HJBServletConstants.DISABLE_MESSAGE_IDS,
                       getCodec().encode(new Boolean(getTheProducer().getDisableMessageID())));
            result.put(HJBServletConstants.DISABLE_TIMESTAMPS,
                       getCodec().encode(new Boolean(getTheProducer().getDisableMessageTimestamp())));
            result.put(HJBServletConstants.TIME_TO_LIVE,
                       getCodec().encode(new Long(getTheProducer().getTimeToLive())));
            result.put(HJBServletConstants.PRIORITY,
                       getCodec().encode((new Integer(getTheProducer().getPriority()))));
            result.put(HJBServletConstants.DELIVERY_MODE,
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
