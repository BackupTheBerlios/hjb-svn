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

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;

import hjb.misc.HJBConstants;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

/**
 * <code>BrowserDescription</code> is used to provide a description of JMS
 * <code>QueueBrowsers</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class BrowserDescription extends JMSObjectDescription {

    public BrowserDescription(QueueBrowser theBrowser,
                              int browserIndex,
                              Date creationTime) {
        super(browserIndex, HJBStrings.INVALID_BROWSER_INDEX);
        if (null == theBrowser) {
            throw new IllegalArgumentException(strings().needsANonNull(QueueBrowser.class));
        }
        if (null == creationTime) {
            throw new IllegalArgumentException(strings().needsANonNull(Date.class));
        }
        this.theBrowser = theBrowser;
        this.creationTime = creationTime;
    }

    protected String getExtraInformation() {
        try {
            String browserDestination = null == getTheBrowser().getQueue() ? ""
                    : "" + getTheBrowser().getQueue();
            return strings().getString(HJBStrings.BROWSER_DESCRIPTION,
                                       browserDestination);
        } catch (JMSException e) {
            return "";
        }
    }

    protected Map attributesAsAMap() {
        Map result = new TreeMap();
        try {
            result.put(HJBConstants.MESSAGE_SELECTOR,
                       (null == getTheBrowser().getMessageSelector() ? ""
                               : getTheBrowser().getMessageSelector()));
            result.put(HJBConstants.CREATION_TIME, getEncodedCreationTime(getCreationTime()));
        } catch (JMSException e) {}
        return result;
    }

    protected String getBaseName() {
        return PathNaming.BROWSER + "-" + getIndex();
    }

    protected QueueBrowser getTheBrowser() {
        return theBrowser;
    }
    
    protected Date getCreationTime() {
        return creationTime;
    }

    private final QueueBrowser theBrowser;
    private final Date creationTime;
}
