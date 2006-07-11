package hjb.jms;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.MessageProducer;

import hjb.http.HJBServletConstants;
import hjb.http.cmd.HJBMessageWriter;
import hjb.http.cmd.PathNaming;
import hjb.misc.HJBStrings;
import hjb.msg.codec.OrderedTypedValueCodec;

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
        this.codec = new OrderedTypedValueCodec();
        this.theProducer = theProducer;
        this.producerIndex = producerIndex;
    }

    public String toString() {
        return PathNaming.PRODUCER + "-" + getProducerIndex() + " "
                + getExtraInformation();
    }

    public String longDescription() {
        StringWriter sw = new StringWriter();
        writeLongDescription(sw);
        return sw.toString();
    }

    public void writeLongDescription(Writer aWriter) {
        PrintWriter pw = new PrintWriter(aWriter);
        pw.print(this);
        pw.println();
        new HJBMessageWriter().writeAsText(attributesAsAMap(), pw);
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

    protected MessageProducer getTheProducer() {
        return theProducer;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected int getProducerIndex() {
        return producerIndex;
    }

    protected OrderedTypedValueCodec getCodec() {
        return codec;
    }

    private final OrderedTypedValueCodec codec;
    private final MessageProducer theProducer;
    private final int producerIndex;
    private static final HJBStrings STRINGS = new HJBStrings();
}
