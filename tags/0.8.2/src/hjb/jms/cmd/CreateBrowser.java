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
package hjb.jms.cmd;

import javax.jms.Queue;
import javax.jms.QueueBrowser;

import hjb.jms.HJBSessionQueueBrowsers;
import hjb.misc.HJBStrings;

public class CreateBrowser extends BaseJMSCommand {

    public CreateBrowser(HJBSessionQueueBrowsers browsers,
                         int sessionIndex,
                         Queue queue,
                         String messageSelector) {
        if (null == browsers)
            throw new IllegalArgumentException(strings().needsANonNull(HJBSessionQueueBrowsers.class));
        if (null == queue)
            throw new IllegalArgumentException(strings().needsANonNull(Queue.class));
        this.browsers = browsers;
        this.messageSelector = messageSelector;
        this.sessionIndex = sessionIndex;
        this.queue = queue;
        setBrowserIndex(UNSET_BROWSER_INDEX);
    }

    public CreateBrowser(HJBSessionQueueBrowsers browsers,
                         int sessionIndex,
                         Queue queue) {
        this(browsers, sessionIndex, queue, null);
    }

    public void execute() {
        assertNotCompleted();
        try {
            createBrowserAndSaveItsIndex();
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_CREATE_COMMANDS,
                                   QueueBrowser.class.getName(),
                                   new Integer(getSessionIndex()));
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_CREATE_COMMANDS,
                                       QueueBrowser.class.getName());
        } else {
            return getFault().getMessage();
        }
    }

    public int getBrowserIndex() {
        return browserIndex;
    }

    public boolean isBrowserIndexSet() {
        return UNSET_BROWSER_INDEX == getBrowserIndex();
    }

    protected void createBrowserAndSaveItsIndex() {
        if (null == getMessageSelector()) {
            setBrowserIndex(getBrowsers().createBrowser(getSessionIndex(),
                                                        getQueue()));
        } else {
            setBrowserIndex(getBrowsers().createBrowser(getSessionIndex(),
                                                        getQueue(),
                                                        getMessageSelector()));
        }
    }

    protected void setBrowserIndex(int browserIndex) {
        this.browserIndex = browserIndex;
    }

    protected int getSessionIndex() {
        return sessionIndex;
    }

    protected HJBSessionQueueBrowsers getBrowsers() {
        return browsers;
    }

    protected Queue getQueue() {
        return queue;
    }

    protected String getMessageSelector() {
        return messageSelector;
    }

    private int sessionIndex;
    private int browserIndex;
    private String messageSelector;
    private Queue queue;
    private HJBSessionQueueBrowsers browsers;
    public static final int UNSET_BROWSER_INDEX = Integer.MIN_VALUE;
}
