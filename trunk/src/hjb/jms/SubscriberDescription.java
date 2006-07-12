package hjb.jms;

import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.TopicSubscriber;

import hjb.http.HJBServletConstants;
import hjb.http.cmd.PathNaming;
import hjb.misc.HJBStrings;

/**
 * <code>SubscriberDescription</code> is used to provide textual description
 * of the JMS <code>TopicSubscribers</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class SubscriberDescription extends BaseJMSObjectDescription {

    public SubscriberDescription(TopicSubscriber theSubscriber,
                                 int subscriberIndex) {
        super(subscriberIndex, HJBStrings.INVALID_SUBSCRIBER_INDEX);
        if (null == theSubscriber) {
            throw new IllegalArgumentException(strings().needsANonNull(TopicSubscriber.class));
        }
        this.theSubscriber = theSubscriber;
    }

    protected Map attributesAsAMap() {
        Map result = new TreeMap();
        try {
            result.put(HJBServletConstants.SUBSCRIBER_NAME,
                       getTheSubscriber().getTopic().getTopicName());
            result.put(HJBServletConstants.CONSUMER_NOLOCAL,
                       getCodec().encode(new Boolean(getTheSubscriber().getNoLocal())));
            result.put(HJBServletConstants.MESSAGE_SELECTOR,
                       (null == getTheSubscriber().getMessageSelector() ? ""
                               : getTheSubscriber().getMessageSelector()));
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

    protected String getPathName() {
        return PathNaming.SUBSCRIBER + "-" + getIndex();
    }

    protected TopicSubscriber getTheSubscriber() {
        return theSubscriber;
    }

    private final TopicSubscriber theSubscriber;
}
