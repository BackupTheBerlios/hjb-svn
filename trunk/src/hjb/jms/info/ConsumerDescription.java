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
