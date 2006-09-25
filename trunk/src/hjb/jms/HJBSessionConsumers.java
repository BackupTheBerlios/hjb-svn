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
import javax.jms.MessageConsumer;

import hjb.jms.info.ConsumerDescription;
import hjb.jms.info.JMSObjectDescription;
import hjb.misc.Clock;
import hjb.misc.HJBStrings;

/**
 * <code>HJBSessionConsumers</code> is used to maintain the JMS
 * <code>Consumers</code> created by a <code>HJBSession</code>.
 * 
 * @author Tim Emiola
 */
public class HJBSessionConsumers extends HJBSessionItems {

    public HJBSessionConsumers(HJBSession theSession, Clock aClock) {
        super(theSession, aClock);
    }

    public int createConsumer(Destination aDestination,
                              String messageSelector,
                              boolean noLocal) {
        try {
            MessageConsumer c = getTheSession().createConsumer(aDestination,
                                                               messageSelector,
                                                               noLocal);
            return addSessionItemAndReturnItsIndex(c);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(HJBStrings.SESSION_NOT_FOUND, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        } catch (JMSException e) {
            handleFailure(HJBStrings.COULD_NOT_CREATE_CONSUMER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public int createConsumer(Destination aDestination, String messageSelector) {
        try {
            MessageConsumer c = getTheSession().createConsumer(aDestination,
                                                               messageSelector);
            return addSessionItemAndReturnItsIndex(c);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(HJBStrings.SESSION_NOT_FOUND, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        } catch (JMSException e) {
            handleFailure(HJBStrings.COULD_NOT_CREATE_CONSUMER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public int createConsumer(Destination aDestination) {
        try {
            MessageConsumer c = getTheSession().createConsumer(aDestination);
            return addSessionItemAndReturnItsIndex(c);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(HJBStrings.SESSION_NOT_FOUND, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        } catch (JMSException e) {
            handleFailure(HJBStrings.COULD_NOT_CREATE_CONSUMER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public MessageConsumer getConsumer(int consumerIndex) {
        try {
            return asArray()[consumerIndex];
        } catch (IndexOutOfBoundsException e) {
            handleFailure("" + consumerIndex, HJBStrings.CONSUMER_NOT_FOUND, e);
            return null;
        }
    }

    public MessageConsumer[] asArray() {
        return (MessageConsumer[]) getItems().toArray(new MessageConsumer[0]);
    }

    public JMSObjectDescription[] getItemDescriptions() {
        final List createdItems = getItemsWithTheirCreationTime();
        JMSObjectDescription result[] = new JMSObjectDescription[createdItems.size()];
        int count = 0;
        for (Iterator i = createdItems.iterator(); i.hasNext();) {
            SessionItem anItem = (SessionItem) i.next();
            MessageConsumer aConsumer = (MessageConsumer) anItem.getObject();
            result[count] = new ConsumerDescription(aConsumer,
                                                    count,
                                                    anItem.getCreationTime());
            count++;
        }
        return result;
    }
}
