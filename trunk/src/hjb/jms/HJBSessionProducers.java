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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;

import hjb.misc.HJBStrings;

/**
 * <code>HJBSessionProducers</code> is used to create and maintain JMS
 * Producers using the {@link javax.jms.Session} created by a
 * {@link HJBConnection}.
 * 
 * @author Tim Emiola
 */
public class HJBSessionProducers extends HJBSessionItems {

    public HJBSessionProducers(HJBConnection theConnection) {
        super(theConnection);
    }

    public int createProducer(int sessionIndex, Destination aDestination) {
        try {
            MessageProducer p = getSession(sessionIndex).createProducer(aDestination);
            return addSessionItemAndReturnItsIndex(getProducers(),
                                                   sessionIndex,
                                                   p);
        } catch (JMSException e) {
            handleFailure(sessionIndex, HJBStrings.COULD_NOT_CREATE_PRODUCER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public MessageProducer getProducer(int sessionIndex, int producerIndex) {
        try {
            return getProducers(sessionIndex)[producerIndex];
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex,
                          producerIndex,
                          HJBStrings.PRODUCER_NOT_FOUND,
                          e);
            return null;
        }
    }

    public MessageProducer[] getProducers(int sessionIndex) {
        try {
            List items = new ArrayList(getItems(getProducers(), sessionIndex));
            return (MessageProducer[]) items.toArray(new MessageProducer[0]);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex, HJBStrings.SESSION_NOT_FOUND, e);
            return new MessageProducer[0];
        }
    }

    public void removeProducers(int sessionIndex) {
        removeSessionItems(getProducers(), sessionIndex);
    }

    protected Map getProducers() {
        return getItems();
    }
}