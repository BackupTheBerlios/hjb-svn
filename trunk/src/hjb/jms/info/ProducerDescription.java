/*
 HJB (HTTP JMS Bridge) links the HTTP protocol to the JMS API.
 Copyright (C) 2006 Timothy Emiola

 HJB is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the
 Free Software Foundation; either version 2.1 of the License, or (at
 your option) any later version.

 This library is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301
 USA

 */
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
public class ProducerDescription extends JMSObjectDescription {

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
