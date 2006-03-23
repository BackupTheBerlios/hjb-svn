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

import javax.jms.Session;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>HJBSessionItems</code> is a base class that provides behaviour common
 * to those classes that represent items created using the
 * {@link javax.jms.Session} created by a {@link HJBConnection}.
 * 
 * @author Tim Emiola
 */
public abstract class HJBSessionItems {

    public HJBSessionItems(HJBConnection theConnection) {
        if (null == theConnection)
            throw new IllegalArgumentException(strings().needsANonNull(HJBConnection.class.getName()));
        this.theConnection = theConnection;
        this.items = Collections.synchronizedMap(new HashMap());
    }

    protected boolean sessionExists(int sessionIndex) {
        return null != getSession(sessionIndex);
    }

    protected HJBConnection getTheConnection() {
        return theConnection;
    }

    protected void handleFailure(int index, MessageFormat formatter, Exception e) {
        logAndThrowFailure(formatter.format(new Object[] {
            new Integer(index)
        }), e);
    }

    protected void handleFailure(int index, String messageKey, Exception e) {
        logAndThrowFailure(strings().getString(messageKey, new Integer(index)),
                           e);
    }

    protected void handleFailure(int sessionIndex,
                                 int itemIndex,
                                 String messageKey,
                                 Exception e) {
        logAndThrowFailure(strings().getString(messageKey,
                                               new Integer(itemIndex)), e);
    }

    protected void logAndThrowFailure(String message, Exception e) {
        LOG.error(message, e);
        throw new HJBException(message, e);
    }

    protected List getItems(Map aMap, int index) {
        synchronized (aMap) {
            return getItems(aMap, index, false);
        }
    }

    protected List getItems(Map aMap, int index, boolean createItems) {
        synchronized (aMap) {
            List sessionItems = (List) aMap.get(new Integer(index));
            if (null == sessionItems && !sessionExists(index) && !createItems) {
                String message = strings().getString(HJBStrings.SESSION_NOT_FOUND,
                                                     new Integer(index));
                throw new IndexOutOfBoundsException(message);
            }
            if (null == sessionItems) {
                sessionItems = new ArrayList();
                aMap.put(new Integer(index), sessionItems);
            }
            return sessionItems;
        }
    }

    protected int addSessionItemAndReturnItsIndex(Map aMap,
                                                  int sessionIndex,
                                                  Object value) {
        synchronized (aMap) {
            List sessionItems = getItems(aMap, sessionIndex, true);
            sessionItems.add(value);
            return sessionItems.size() - 1;
        }
    }

    protected Object getSessionItem(Map aMap, int sessionIndex, int valueIndex) {
        synchronized (aMap) {
            return getItems(aMap, sessionIndex).get(valueIndex);
        }
    }

    protected Object removeSessionItems(Map aMap, int sessionIndex) {
        synchronized (aMap) {
            return aMap.remove(new Integer(sessionIndex));
        }
    }

    protected Map getItems() {
        return items;
    }

    protected Session getSession(int sessionIndex) {
        try {
            return getTheConnection().getSession(sessionIndex);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex, HJBStrings.SESSION_NOT_FOUND, e);
            return null;
        }
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private HJBConnection theConnection;

    private Map items;

    private static final Logger LOG = Logger.getLogger(HJBSessionItems.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
