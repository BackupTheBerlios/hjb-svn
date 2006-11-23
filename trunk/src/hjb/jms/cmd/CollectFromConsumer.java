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
import hjb.misc.HJBNotFoundException;
import hjb.misc.HJBStrings;
import hjb.msg.HJBMessage;

public class CollectFromConsumer extends MessengerCommand {

    public CollectFromConsumer(HJBMessenger messenger,
                               int consumerIndex,
                               long timeout,
                               int numberToCollect) {
        super(messenger);
        if (timeout <= 0) {
            throw new IllegalArgumentException(strings().getString(HJBStrings.TIMEOUT_MUST_BE_ABOVE_ZERO,
                                                                   new Long(timeout)));
        }
        this.consumerIndex = consumerIndex;
        this.timeout = timeout;
        this.numberToCollect = numberToCollect;
    }

    public void execute() {
        assertNotCompleted();
        try {
            collectMessages();
        } catch (HJBNotFoundException e) {
            recordFaultAsDebug(e);
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_COLLECT_FROM_CONSUMER,
                                   getMessenger().getConsumerDescription(getConsumerIndex()),
                                   getMessenger().getSessionDescription(),
                                   new Integer(getMessagesReceived().length));
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.MESSAGES_RECEIVED_OK);
        } else {
            return getFault().getMessage();
        }
    }

    public HJBMessage[] getMessagesReceived() {
        return messagesReceived;
    }

    protected void collectMessages() {
        setMessagesReceived(getMessenger().collectFromConsumer(getConsumerIndex(),
                                                               getTimeout(),
                                                               getNumberToCollect()));
    }

    protected void setMessagesReceived(HJBMessage[] messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    protected int getConsumerIndex() {
        return consumerIndex;
    }

    protected long getTimeout() {
        return timeout;
    }

    protected int getNumberToCollect() {
        return numberToCollect;
    }

    private int consumerIndex;
    private int numberToCollect;
    private long timeout;

    public static final long NO_TIMEOUT_SET = Long.MAX_VALUE;
    public static final long NO_WAIT = Long.MIN_VALUE;
    private HJBMessage messagesReceived[];
}
