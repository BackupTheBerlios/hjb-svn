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

    public String toString() {
        return PathNaming.SESSION + "-" + getSessionIndex()
                + getExtraInformation();
    }

    protected String getExtraInformation() {
        try {
            return !getTheSession().getTransacted() ? ""
                    : strings().getString(HJBStrings.SESSION_DESCRIPTION,
                                          strings().getString(HJBStrings.TRANSACTED));
        } catch (JMSException e) {
            return "";
        }
    }

    protected Session getTheSession() {
        return theSession;
    }

    protected int getSessionIndex() {
        return sessionIndex;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private Session theSession;
    private int sessionIndex;
    private static final HJBStrings STRINGS = new HJBStrings();
}
