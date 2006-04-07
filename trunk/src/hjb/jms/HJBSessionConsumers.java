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
import javax.jms.MessageConsumer;

import hjb.misc.HJBStrings;

/**
 * <code>HJBSessionConsumers</code> is used to maintain the JMS
 * <code>Consumers</code> for all <code>Sessions</code> created by a
 * <code>HJBConnection</code>.
 * 
 * @author Tim Emiola
 */
public class HJBSessionConsumers extends HJBSessionItems {

    public HJBSessionConsumers(HJBConnection theConnection) {
        super(theConnection);
    }

    public int createConsumer(int sessionIndex,
                              Destination aDestination,
                              String messageSelector,
                              boolean noLocal) {
        try {
            MessageConsumer c = getSession(sessionIndex).createConsumer(aDestination,
                                                                        messageSelector,
                                                                        noLocal);
            return addSessionItemAndReturnItsIndex(getConsumers(),
                                                   sessionIndex,
                                                   c);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex, HJBStrings.SESSION_NOT_FOUND, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        } catch (JMSException e) {
            handleFailure(sessionIndex, HJBStrings.COULD_NOT_CREATE_CONSUMER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public int createConsumer(int sessionIndex,
                              Destination aDestination,
                              String messageSelector) {
        try {
            MessageConsumer c = getSession(sessionIndex).createConsumer(aDestination,
                                                                        messageSelector);
            return addSessionItemAndReturnItsIndex(getConsumers(),
                                                   sessionIndex,
                                                   c);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex, HJBStrings.SESSION_NOT_FOUND, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        } catch (JMSException e) {
            handleFailure(sessionIndex, HJBStrings.COULD_NOT_CREATE_CONSUMER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public int createConsumer(int sessionIndex, Destination aDestination) {
        try {
            MessageConsumer c = getSession(sessionIndex).createConsumer(aDestination);
            return addSessionItemAndReturnItsIndex(getConsumers(),
                                                   sessionIndex,
                                                   c);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex, HJBStrings.SESSION_NOT_FOUND, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        } catch (JMSException e) {
            handleFailure(sessionIndex, HJBStrings.COULD_NOT_CREATE_CONSUMER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public MessageConsumer getConsumer(int sessionIndex, int consumerIndex) {
        try {
            return getConsumers(sessionIndex)[consumerIndex];
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex,
                          consumerIndex,
                          HJBStrings.CONSUMER_NOT_FOUND,
                          e);
            return null;
        }
    }

    public MessageConsumer[] getConsumers(int sessionIndex) {
        try {
            List items = new ArrayList(getItems(getConsumers(), sessionIndex));
            return (MessageConsumer[]) items.toArray(new MessageConsumer[0]);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex, HJBStrings.SESSION_NOT_FOUND, e);
            return new MessageConsumer[0];
        }
    }

    public void removeConsumers(int sessionIndex) {
        removeSessionItems(getConsumers(), sessionIndex);
    }

    protected Map getConsumers() {
        return getItems();
    }
}
