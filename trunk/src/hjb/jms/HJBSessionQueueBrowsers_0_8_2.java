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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

import hjb.misc.HJBStrings;

/**
 * <code>HJBSessionQueueBrowsers_0_8_2</code> is used to maintain the JMS
 * <code>QueueBrowsers<code> for all the <code>Session</code> created by a
 * <code>HJBConnection</code>.
 * 
 * @author Tim Emiola
 */
public class HJBSessionQueueBrowsers_0_8_2 extends HJBSessionItems {

    public HJBSessionQueueBrowsers_0_8_2(HJBConnection theConnection) {
        super(theConnection);
    }

    public int createBrowser(int sessionIndex, Queue aQueue) {
        try {
            QueueBrowser b = getSession(sessionIndex).createBrowser(aQueue);
            return addSessionItemAndReturnItsIndex(getBrowsers(),
                                                   sessionIndex,
                                                   b);
        } catch (JMSException e) {
            handleFailure(sessionIndex, HJBStrings.COULD_NOT_CREATE_BROWSER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public int createBrowser(int sessionIndex,
                             Queue aQueue,
                             String messageSelector) {
        try {
            QueueBrowser b = getSession(sessionIndex).createBrowser(aQueue,
                                                                    messageSelector);
            return addSessionItemAndReturnItsIndex(getBrowsers(),
                                                   sessionIndex,
                                                   b);
        } catch (JMSException e) {
            handleFailure(sessionIndex, HJBStrings.COULD_NOT_CREATE_BROWSER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public QueueBrowser getBrowser(int sessionIndex, int browserIndex) {
        try {
            return getBrowsers(sessionIndex)[browserIndex];
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex, "" + browserIndex, HJBStrings.BROWSER_NOT_FOUND, e);
            return null;
        }
    }

    public QueueBrowser[] getBrowsers(int sessionIndex) {
        try {
            List items = new ArrayList(getItems(getBrowsers(), sessionIndex));
            return (QueueBrowser[]) items.toArray(new QueueBrowser[0]);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(sessionIndex, HJBStrings.SESSION_NOT_FOUND, e);
            return new QueueBrowser[0];
        }
    }

    public void removeBrowsers(int sessionIndex) {
        removeSessionItems(getBrowsers(), sessionIndex);
    }

    protected Map getBrowsers() {
        return getItems();
    }
}