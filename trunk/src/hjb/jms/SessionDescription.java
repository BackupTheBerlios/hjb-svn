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
public class SessionDescription extends BaseJMSObjectDescription {

    public SessionDescription(Session theSession, int sessionIndex) {
        super(sessionIndex, HJBStrings.INVALID_SESSION_INDEX);
        if (null == theSession) {
            throw new IllegalArgumentException(strings().needsANonNull(Session.class));
        }
        this.theSession = theSession;
    }
    
    protected String getPathName() {
        return PathNaming.SESSION + "-" + getIndex();
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

    private Session theSession;
}
