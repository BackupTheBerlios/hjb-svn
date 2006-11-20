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
package hjb.jms;

import java.util.Iterator;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;

import hjb.jms.info.JMSObjectDescription;
import hjb.jms.info.ProducerDescription;
import hjb.misc.Clock;
import hjb.misc.HJBStrings;
import hjb.misc.MessageProducerArguments;

/**
 * <code>HJBSessionProducers</code> is used to maintain the JMS
 * <code>Producers</code> created by a <code>HJBSession</code>.
 * 
 * @author Tim Emiola
 */
public class HJBSessionProducers extends HJBSessionItems {

    public HJBSessionProducers(HJBSession theSession, Clock aClock) {
        super(theSession, aClock);
    }

    public int createProducer(Destination aDestination,
                              MessageProducerArguments producerArguments) {
        try {
            MessageProducer p = getTheSession().createProducer(aDestination);
            configureProducer(p, producerArguments);
            return addSessionItemAndReturnItsIndex(p);
        } catch (JMSException e) {
            handleFailure(HJBStrings.COULD_NOT_CREATE_PRODUCER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public MessageProducer getProducer(int producerIndex) {
        try {
            return asArray()[producerIndex];
        } catch (IndexOutOfBoundsException e) {
            handleFailure("" + producerIndex, HJBStrings.PRODUCER_NOT_FOUND, e);
            return null;
        }
    }

    public MessageProducer[] asArray() {
        return (MessageProducer[]) getItems().toArray(new MessageProducer[0]);
    }

    public JMSObjectDescription[] getItemDescriptions() {
        final List createdItems = getItemsWithTheirCreationTime();
        JMSObjectDescription result[] = new JMSObjectDescription[createdItems.size()];
        int count = 0;
        for (Iterator i = createdItems.iterator(); i.hasNext();) {
            SessionItem anItem = (SessionItem) i.next();
            MessageProducer aProducer = (MessageProducer) anItem.getObject();
            result[count] = new ProducerDescription(aProducer,
                                                    count,
                                                    anItem.getCreationTime());
            count++;
        }
        return result;
    }

    protected void configureProducer(MessageProducer producer,
                                     MessageProducerArguments producerArguments)
            throws JMSException {
        producer.setDisableMessageID(producerArguments.isDisableMessageIds());
        producer.setDisableMessageTimestamp(producerArguments.isDisableTimestamps());
        if (producerArguments.isDeliveryModeSet()) {
            producer.setDeliveryMode(producerArguments.getDeliveryMode());
        }
        if (producerArguments.isPrioritySet()) {
            producer.setPriority(producerArguments.getPriority());
        }
        if (producerArguments.isTimeToLiveSet()) {
            producer.setTimeToLive(producerArguments.getTimeToLive());
        }
    }
}