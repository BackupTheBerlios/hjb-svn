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
package hjb.misc;

import javax.jms.DeliveryMode;

/**
 * <code>MessageProducerArguments</code> groups the set of values that are
 * optionally used to configure a <code>MessageProducer</code>.
 * 
 * @author Tim Emiola
 */
public class MessageProducerArguments {

    public MessageProducerArguments() {
        this(false,
             false,
             null,
             null,
             null);
    }

    public MessageProducerArguments(boolean disableTimestamps,
                                    boolean disableMessageIds,
                                    Long timeToLive,
                                    Integer deliveryMode,
                                    Integer priority) {
        this.disableTimestamps = disableTimestamps;
        this.disableMessageIds = disableMessageIds;
        this.timeToLive = timeToLive;
        this.deliveryMode = deliveryMode;
        this.priority = priority;
    }

    public int getDeliveryMode() {
        if (!isDeliveryModeSet()) {
            throw new IllegalStateException(strings().getString(HJBStrings.NO_VALUE_PROVIDED,
                                                                "delivery mode"));
        }
        return deliveryMode.intValue();
    }

    public boolean isDeliveryModeSet() {
        return null != deliveryMode;
    }

    public boolean isDisableMessageIds() {
        return disableMessageIds;
    }

    public boolean isDisableTimestamps() {
        return disableTimestamps;
    }

    public int getPriority() {
        if (!isPrioritySet()) {
            throw new IllegalStateException(strings().getString(HJBStrings.NO_VALUE_PROVIDED,
                                                                "priority"));
        }
        return priority.intValue();
    }

    public boolean isPrioritySet() {
        return null != priority;
    }

    public long getTimeToLive() {
        if (!isTimeToLiveSet()) {
            throw new IllegalStateException(strings().getString(HJBStrings.NO_VALUE_PROVIDED,
                                                                "time to live"));
        }
        return timeToLive.longValue();
    }

    public boolean isTimeToLiveSet() {
        return null != timeToLive;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private boolean disableTimestamps;
    private boolean disableMessageIds;
    private Long timeToLive;
    private Integer deliveryMode;
    private Integer priority;
    private static final HJBStrings STRINGS = new HJBStrings();
}