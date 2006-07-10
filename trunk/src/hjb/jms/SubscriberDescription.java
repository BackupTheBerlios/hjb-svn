package hjb.jms;

import javax.jms.JMSException;
import javax.jms.TopicSubscriber;

import hjb.http.cmd.PathNaming;
import hjb.misc.HJBStrings;

/**
 * <code>SubscriberDescription</code> is used to provide textual description of
 * the JMS <code>TopicSubscribers</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class SubscriberDescription {

    public SubscriberDescription(TopicSubscriber theSubscriber, int subscriberIndex) {
        if (null == theSubscriber) {
            throw new IllegalArgumentException(strings().needsANonNull(TopicSubscriber.class));
        }
        if (subscriberIndex < 0) {
            throw new IllegalArgumentException(strings().getString(HJBStrings.INVALID_SUBSCRIBER_INDEX,
                                                                   new Integer(subscriberIndex)));
        }
        this.theSubscriber = theSubscriber;
        this.subscriberIndex = subscriberIndex;
    }

    public String toString() {
        return PathNaming.SUBSCRIBER + "-" + getSubscriberIndex()
                + getExtraInformation();
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

    protected TopicSubscriber getTheSubscriber() {
        return theSubscriber;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected int getSubscriberIndex() {
        return subscriberIndex;
    }

    private final TopicSubscriber theSubscriber;
    private final int subscriberIndex;
    private static final HJBStrings STRINGS = new HJBStrings();
}
