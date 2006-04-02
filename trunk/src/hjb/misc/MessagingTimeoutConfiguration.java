package hjb.misc;

/**
 * <code>MessagingTimeoutConfiguration</code> is used to specify the maximum
 * and default timeouts for waiting to receive JMS messages in HJB.
 * 
 * <p />
 * It specifies
 * <ul>
 * 
 * <li>a maximum timeout, which is used to limit HJB request-specified
 * timeouts, because HJB does not allow requests to wait indefinitely.</li>
 * 
 * <li>a minimum timeout, used to stop a timeout of 0 being assigned (this
 * corresponds to waiting indefinitely). The configured minimum timeout also
 * serves as the default timeout value - it is used if no timeout value is
 * specified.</li>
 * 
 * </ul>
 * 
 * @author Tim Emiola
 */
public class MessagingTimeoutConfiguration {

    /**
     * Returns the default message timeout.
     * 
     * It defaults to {@link #MINIMUM_TIMEOUT}, but if
     * {@link #MINIMUM_TIMEOUT_PROPERTY} is set and its value is valid, it uses
     * that.
     * 
     * @return the default message timeout.
     */
    public int getMinimumMessageTimeout() {
        return Integer.getInteger(MINIMUM_TIMEOUT_PROPERTY, MINIMUM_TIMEOUT).intValue();
    }

    /**
     * Returns the maximum message timeout.
     * 
     * It defaults to {@link #DEFAULT_MAXIMUM_TIMEOUT}, but if
     * {@link #MAXIMUM_TIMEOUT_PROPERTY} is set and its value is valid, it uses
     * that.
     * 
     * @return the maximum message timeout.
     */
    public int getMaximumMessageTimeout() {
        return Integer.getInteger(MAXIMUM_TIMEOUT_PROPERTY,
                                  DEFAULT_MAXIMUM_TIMEOUT).intValue();
    }

    /**
     * System property name that can be used to change the maximum timeout on
     * waiting to receive a JMS Message throughout HJB (in seconds).
     * 
     * <p />
     * The value of this constant is 'hjb.runtime.maximum-receive-timeout'.
     */
    public static final String MAXIMUM_TIMEOUT_PROPERTY = "hjb.runtime.maximum-receive-timeout";

    /**
     * System property name that can be used to change for the timeout on
     * waiting to receive a JMS message throughout HJB (in seconds).
     * <p />
     * The value of this constant is 'hjb.runtime.minimum-receive-timeout'.
     */
    public static final String MINIMUM_TIMEOUT_PROPERTY = "hjb.runtime.minimum-receive-timeout";

    /**
     * Constant that holds the default value for the maximum timeout on waiting
     * to receive a JMS Message throughout HJB (in seconds). This value is the
     * ceiling for timeout's specified in 'receive from consumer/subscriber'
     * requests.
     * <p />
     * The value of this constant is 300 (i.e, 5 minutes).
     */
    public static final int DEFAULT_MAXIMUM_TIMEOUT = 300;

    /**
     * Constant that holds the default value for the timeout on waiting to
     * receive a JMS message throughout HJB (in seconds). This value is used
     * when no timeout is specified in 'receive from consumer/subscriber'
     * requests, as HJB does not allow indefinite waiting.
     * <p />
     * The value of this constant is 60 (i.e, 1 minutes).
     */
    public static final int MINIMUM_TIMEOUT = 60;
}
