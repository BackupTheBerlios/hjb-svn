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

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.TopicSubscriber;

import hjb.misc.HJBConstants;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

/**
 * <code>SubscriberDescription</code> is used to a provide description of JMS
 * <code>TopicSubscribers</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class SubscriberDescription extends JMSObjectDescription {

    public SubscriberDescription(TopicSubscriber theSubscriber,
                                 int subscriberIndex,
                                 Date creationTime) {
        super(subscriberIndex, HJBStrings.INVALID_SUBSCRIBER_INDEX);
        if (null == theSubscriber) {
            throw new IllegalArgumentException(strings().needsANonNull(TopicSubscriber.class));
        }
        if (null == creationTime) {
            throw new IllegalArgumentException(strings().needsANonNull(Date.class));
        }
        this.creationTime = creationTime;
        this.theSubscriber = theSubscriber;
    }

    protected Map attributesAsAMap() {
        Map result = new TreeMap();
        try {
            result.put(HJBConstants.SUBSCRIBER_NAME,
                       getTheSubscriber().getTopic().getTopicName());
            result.put(HJBConstants.CONSUMER_NOLOCAL,
                       getCodec().encode(new Boolean(getTheSubscriber().getNoLocal())));
            result.put(HJBConstants.MESSAGE_SELECTOR,
                       (null == getTheSubscriber().getMessageSelector() ? ""
                               : getTheSubscriber().getMessageSelector()));
            result.put(HJBConstants.CREATION_TIME,
                       getEncodedCreationTime(getCreationTime()));
        } catch (JMSException e) {}
        return result;
    }

    protected String getExtraInformation() {
        try {
            String subscriberDestination = null == getTheSubscriber().getTopic() ? ""
                    : "" + getTheSubscriber().getTopic();
            return strings().getString(HJBStrings.SUBSCRIBER_DESCRIPTION,
                                       subscriberDestination,
                                       new Boolean(getTheSubscriber().getNoLocal()));
        } catch (JMSException e) {
            return "";
        }
    }

    protected String getBaseName() {
        return PathNaming.SUBSCRIBER + "-" + getIndex();
    }

    protected TopicSubscriber getTheSubscriber() {
        return theSubscriber;
    }

    protected Date getCreationTime() {
        return creationTime;
    }

    private final Date creationTime;
    private final TopicSubscriber theSubscriber;
}
