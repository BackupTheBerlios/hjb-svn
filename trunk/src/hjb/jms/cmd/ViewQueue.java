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

import hjb.jms.HJBMessenger;
import hjb.misc.HJBStrings;
import hjb.msg.HJBMessage;

public class ViewQueue extends MessengerCommand {

    public ViewQueue(HJBMessenger messenger, int browserIndex) {
        super(messenger);
        this.browserIndex = browserIndex;
    }

    public void execute() {
        assertNotCompleted();
        try {
            messagesOnQueue = getMessenger().viewQueue(getBrowserIndex());
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_VIEW_QUEUE,
                                   new Integer(getBrowserIndex()),
                                   new Integer(getMessenger().getSessionIndex()));
    }

    public HJBMessage[] getMessagesOnQueue() {
        return messagesOnQueue;
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.STRINGS_RECEIVED_OK);
        } else {
            return getFault().getMessage();
        }
    }

    protected int getBrowserIndex() {
        return browserIndex;
    }

    private HJBMessage messagesOnQueue[];
    private int browserIndex;
}
