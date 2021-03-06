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

import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

import hjb.jms.info.BrowserDescription;
import hjb.jms.info.JMSObjectDescription;
import hjb.misc.Clock;
import hjb.misc.HJBStrings;

/**
 * <code>HJBSessionQueueBrowsers</code> is used to maintain the JMS
 * <code>QueueBrowsers<code> created by a
 * <code>HJBSession</code>.
 * 
 * @author Tim Emiola
 */
public class HJBSessionQueueBrowsers extends HJBSessionItems {

    public HJBSessionQueueBrowsers(HJBSession theSession, Clock aClock) {
        super(theSession, aClock);
    }

    public int createBrowser(Queue aQueue) {
        try {
            QueueBrowser b = getTheSession().createBrowser(aQueue);
            return addSessionItemAndReturnItsIndex(b);
        } catch (JMSException e) {
            handleFailure(HJBStrings.COULD_NOT_CREATE_BROWSER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public int createBrowser(Queue aQueue, String messageSelector) {
        try {
            QueueBrowser b = getTheSession().createBrowser(aQueue,
                                                           messageSelector);
            return addSessionItemAndReturnItsIndex(b);
        } catch (JMSException e) {
            handleFailure(HJBStrings.COULD_NOT_CREATE_BROWSER, e);
            return HJBStrings.INTEGER_NOT_REACHED;
        }
    }

    public QueueBrowser getBrowser(int browserIndex) {
        try {
            return asArray()[browserIndex];
        } catch (IndexOutOfBoundsException e) {
            handleFailure("" + browserIndex, HJBStrings.BROWSER_NOT_FOUND, e);
            return null;
        }
    }

    public QueueBrowser[] asArray() {
        try {
            return (QueueBrowser[]) getItems().toArray(new QueueBrowser[0]);
        } catch (IndexOutOfBoundsException e) {
            handleFailure(HJBStrings.SESSION_NOT_FOUND, e);
            return new QueueBrowser[0];
        }
    }

    public JMSObjectDescription[] getItemDescriptions() {
        final List createdItems = getItemsWithTheirCreationTime();
        JMSObjectDescription result[] = new JMSObjectDescription[createdItems.size()];
        int count = 0;
        for (Iterator i = createdItems.iterator(); i.hasNext();) {
            SessionItem anItem = (SessionItem) i.next();
            QueueBrowser aBrowser = (QueueBrowser) anItem.getObject();
            result[count] = new BrowserDescription(aBrowser,
                                                   count,
                                                   anItem.getCreationTime());
            count++;
        }
        return result;
    }
}