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

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import hjb.misc.HJBStrings;

/**
 * <code>HJBSessionDurableSubscribers_0_8_2</code> is used to maintain the JMS
 * Durable Subscribers created by a <code>HJBSession</code>.
 * 
 * @author Tim Emiola
 */
public class HJBSessionDurableSubscribersNG extends HJBSessionItemsNG {

    public HJBSessionDurableSubscribersNG(HJBSession theSession) {
        super(theSession);
    }

    public int createDurableSubscriber(Topic aTopic, String name) {

        try {
            TopicSubscriber s = getTheSession().createDurableSubscriber(aTopic,
                                                                        name);
            return addSessionItemAndReturnItsIndex(s);
        } catch (JMSException e) {
            handleFailure(HJBStrings.COULD_NOT_CREATE_SUBSCRIBER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public int createDurableSubscriber(Topic aTopic,
                                       String name,
                                       String messageSelector,
                                       boolean noLocal) {
        try {
            TopicSubscriber s = getTheSession().createDurableSubscriber(aTopic,
                                                                        name,
                                                                        messageSelector,
                                                                        noLocal);
            return addSessionItemAndReturnItsIndex(s);
        } catch (JMSException e) {
            handleFailure(HJBStrings.COULD_NOT_CREATE_SUBSCRIBER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public TopicSubscriber getSubscriber(int subscriberIndex) {
        try {
            return asArray()[subscriberIndex];
        } catch (IndexOutOfBoundsException e) {
            handleFailure("" + subscriberIndex,
                          HJBStrings.SUBSCRIBER_NOT_FOUND,
                          e);
            return null;
        }
    }

    public TopicSubscriber[] asArray() {
        try {
            return (TopicSubscriber[]) getItems().toArray(new TopicSubscriber[0]);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(HJBStrings.SESSION_NOT_FOUND, e);
            return new TopicSubscriber[0];
        }
    }

}
