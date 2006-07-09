package hjb.jms;

import javax.jms.JMSException;
import javax.jms.Session;

import hjb.http.cmd.PathNaming;
import hjb.misc.HJBStrings;

/**
 * <code>SessionDescription</code> is used to provide textual description of
 * the JMS <code>Sessions</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class SessionDescription {

    public SessionDescription(Session theSession, int sessionIndex) {
        if (null == theSession) {
            throw new IllegalArgumentException(strings().needsANonNull(Session.class));
        }
        if (sessionIndex < 0) {
            throw new IllegalArgumentException(strings().getString(HJBStrings.INVALID_SESSION_INDEX,
                                                                   new Integer(sessionIndex)));
        }
        this.theSession = theSession;
        this.sessionIndex = sessionIndex;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected String getExtraInformation() {
        try {
            String transacted = getTheSession().getTransacted() ? strings().getString(HJBStrings.TRANSACTED)
                    : "";
            return strings().getString(HJBStrings.SIMPLE_SESSION_DESCRIPTION,
                                       transacted);
        } catch (JMSException e) {
            return "";
        }
    }

    public String toString() {
        return PathNaming.SESSION + "-" + getSessionIndex()
                + getExtraInformation();
    }

    protected Session getTheSession() {
        return theSession;
    }

    protected int getSessionIndex() {
        return sessionIndex;
    }

    private Session theSession;
    private int sessionIndex;
    private static final HJBStrings STRINGS = new HJBStrings();
}
