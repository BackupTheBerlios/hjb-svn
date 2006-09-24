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
package hjb.jms;

import java.text.MessageFormat;
import java.util.*;

import org.apache.log4j.Logger;

import hjb.jms.info.SessionDescription;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>HJBSessionItems</code> is a base class that provides behaviour common
 * to those classes that represent items created using a {@link HJBSession}.
 * 
 * @author Tim Emiola
 */
public abstract class HJBSessionItems {

    public HJBSessionItems(HJBSession theSession) {
        if (null == theSession) {
            throw new IllegalArgumentException(strings().needsANonNull(HJBSession.class));
        }
        this.theSession = theSession;
        this.items = Collections.synchronizedList(new ArrayList());
    }

    public int getSessionIndex() {
        return getTheSession().getSessionIndex();
    }
    
    protected void handleFailure(MessageFormat formatter, Exception e) {
        logAndThrowFailure(formatter.format(new Object[] {
            new Integer(getSessionIndex())
        }), e);
    }

    protected void handleFailure(String messageKey, Exception e) {
        logAndThrowFailure(strings().getString(messageKey, new Integer(getSessionIndex())),
                           e);
    }

    protected void handleFailure(String itemDescription,
                                 String messageKey,
                                 Exception e) {
        logAndThrowFailure(strings().getString(messageKey,
                                               getSessionDescription(),
                                               itemDescription), e);
    }


    protected void logAndThrowFailure(String message, Exception e) {
        LOG.error(message, e);
        throw new HJBException(message, e);
    }

    protected List getItems() {
        return items;
    }
    
    protected int addSessionItemAndReturnItsIndex(Object value) {
        synchronized (items) {
            items.add(value);
            return items.size() - 1;
        }
    }

    protected HJBSession getTheSession() {
        return theSession;
    }

    protected SessionDescription getSessionDescription() {
        return getTheSession().getSessionDescription();
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private final HJBSession theSession;
    private final List items;

    private static final Logger LOG = Logger.getLogger(HJBSessionItems.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
