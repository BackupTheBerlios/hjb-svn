/**
 * 
 */
package hjb.misc;

/**
 * <code>MessageProducerArguments</code> groups the set of values that are
 * optionally used to configure a <code>MessageProducer</code>.
 * 
 * @author Tim Emiola
 */
public class MessageProducerArguments {

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