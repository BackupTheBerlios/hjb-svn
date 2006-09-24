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

import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.Session;

import hjb.misc.HJBConstants;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

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
            result.put(HJBConstants.SESSION_TRANSACTED,
                       getCodec().encode(new Boolean(getTheSession().getTransacted())));
            result.put(HJBConstants.SESSION_ACKNOWLEDGEMENT_MODE,
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
