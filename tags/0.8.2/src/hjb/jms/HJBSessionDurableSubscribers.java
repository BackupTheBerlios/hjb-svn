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

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import hjb.misc.HJBStrings;

/**
 * <code>HJBSessionDurableSubscribers</code> is used to maintain the JMS
 * Durable Subscribers for all the <code>Sessions</code> created by a
 * <code>HJBConnection</code>.
 * 
 * @author Tim Emiola
 */
public class HJBSessionDurableSubscribers extends HJBSessionItems {

    public HJBSessionDurableSubscribers(HJBConnection theConnection) {
        super(theConnection);
    }

    public int createDurableSubscriber(int sessionIndex,
                                       Topic aTopic,
                                       String name) {

        try {
            TopicSubscriber s = getSession(sessionIndex).createDurableSubscriber(aTopic,
                                                                                 name);
            return addSessionItemAndReturnItsIndex(getSubscribers(),
                                                   sessionIndex,
                                                   s);
        } catch (JMSException e) {
            handleFailure(sessionIndex,
                          HJBStrings.COULD_NOT_CREATE_SUBSCRIBER,
                          e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public int createDurableSubscriber(int sessionIndex,
                                       Topic aTopic,
                                       String name,
                                       String messageSelector,
                                       boolean noLocal) {
        try {
            TopicSubscriber s = getSession(sessionIndex).createDurableSubscriber(aTopic,
                                                                                 name,
                                                                                 messageSelector,
                                                                                 noLocal);
            return addSessionItemAndReturnItsIndex(getSubscribers(),
                                                   sessionIndex,
                                                   s);
        } catch (JMSException e) {
            handleFailure(sessionIndex,
                          HJBStrings.COULD_NOT_CREATE_SUBSCRIBER,
                          e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public TopicSubscriber getSubscriber(int sessionIndex, int producerIndex) {
        try {
            return getSubscribers(sessionIndex)[producerIndex];
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex,
                          producerIndex,
                          HJBStrings.SUBSCRIBER_NOT_FOUND,
                          e);
            return null;
        }
    }

    public TopicSubscriber[] getSubscribers(int sessionIndex) {
        try {
            List items = new ArrayList(getItems(getSubscribers(), sessionIndex));
            return (TopicSubscriber[]) items.toArray(new TopicSubscriber[0]);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex, HJBStrings.SESSION_NOT_FOUND, e);
            return new TopicSubscriber[0];
        }
    }

    public void removeSubscribers(int sessionIndex) {
        removeSessionItems(getSubscribers(), sessionIndex);
    }

    protected Map getSubscribers() {
        return getItems();
    }
}
