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

import hjb.jms.HJBSessionQueueBrowsersNG;
import hjb.misc.HJBStrings;

public class CreateBrowser extends BaseJMSCommand {

    public CreateBrowser(HJBSessionQueueBrowsersNG browsers,
                         Queue queue,
                         String messageSelector) {
        if (null == browsers)
            throw new IllegalArgumentException(strings().needsANonNull(HJBSessionQueueBrowsersNG.class));
        if (null == queue)
            throw new IllegalArgumentException(strings().needsANonNull(Queue.class));
        this.browsers = browsers;
        this.messageSelector = messageSelector;
        this.queue = queue;
        setBrowserIndex(UNSET_BROWSER_INDEX);
    }

    public CreateBrowser(HJBSessionQueueBrowsersNG browsers, Queue queue) {
        this(browsers, queue, null);
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
                                   getBrowserIndexAsText());
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

    public String getBrowserIndexAsText() {
        if (isBrowserIndexSet()) {
            return "" + getBrowserIndex();
        } else {
            return strings().getString(HJBStrings.NOT_APPLICAPLE);
        }
    }

    public boolean isBrowserIndexSet() {
        return UNSET_BROWSER_INDEX != getBrowserIndex();
    }

    protected void createBrowserAndSaveItsIndex() {
        if (null == getMessageSelector()) {
            setBrowserIndex(getBrowsers().createBrowser(getQueue()));
        } else {
            setBrowserIndex(getBrowsers().createBrowser(getQueue(),
                                                        getMessageSelector()));
        }
    }

    protected void setBrowserIndex(int browserIndex) {
        this.browserIndex = browserIndex;
    }

    protected HJBSessionQueueBrowsersNG getBrowsers() {
        return browsers;
    }

    protected Queue getQueue() {
        return queue;
    }

    protected String getMessageSelector() {
        return messageSelector;
    }

    private int browserIndex;
    private final String messageSelector;
    private final Queue queue;
    private final HJBSessionQueueBrowsersNG browsers;
    public static final int UNSET_BROWSER_INDEX = Integer.MIN_VALUE;
}
