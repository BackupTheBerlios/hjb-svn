package hjb.jms;

import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.Session;

import hjb.http.HJBServletConstants;
import hjb.http.cmd.PathNaming;
import hjb.misc.HJBStrings;

/**
 * <code>SessionDescription</code> is used to provide a description of JMS
 * <code>Sessions</code> in HJB status messages and logs.
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

    protected Map attributesAsAMap() {
        Map result = new TreeMap();
        try {
            result.put(HJBServletConstants.SESSION_TRANSACTED,
                       getCodec().encode(new Boolean(getTheSession().getTransacted())));
            result.put(HJBServletConstants.SESSION_ACKNOWLEDGEMENT_MODE,
                       getCodec().encode(new Integer(getTheSession().getAcknowledgeMode())));
        } catch (JMSException e) {}
        return result;
    }

    protected String getBaseName() {
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
