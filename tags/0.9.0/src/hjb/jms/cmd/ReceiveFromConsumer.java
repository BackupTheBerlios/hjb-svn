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

public class ReceiveFromConsumer extends MessengerCommand {

    public ReceiveFromConsumer(HJBMessenger messenger, int consumerIndex) {
        this(messenger, consumerIndex, NO_TIMEOUT_SET);
    }

    public ReceiveFromConsumer(HJBMessenger messenger,
                               int consumerIndex,
                               long timeout) {
        super(messenger);
        this.consumerIndex = consumerIndex;
        this.timeout = timeout;
    }

    public void execute() {
        assertNotCompleted();
        try {
            receiveMessage();
        } catch (HJBNotFoundException e) {
            recordFaultAsDebug(e);
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }
    
    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_RECEIVE_FROM_CONSUMER,
                                   getMessenger().getConsumerDescription(getConsumerIndex()),
                                   getMessenger().getSessionDescription());
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.MESSAGE_RECEIVED_OK);
        } else {
            return getFault().getMessage();
        }
    }

    public HJBMessage getMessageReceived() {
        return messageReceived;
    }

    protected void setMessageReceived(HJBMessage messageReceived) {
        this.messageReceived = messageReceived;
    }

    protected void receiveMessage() {
        if (NO_TIMEOUT_SET == getTimeout()) {
            setMessageReceived(getMessenger().receiveFromConsumer(getConsumerIndex()));
        } else if (NO_WAIT == getTimeout()) {
            setMessageReceived(getMessenger().receiveFromConsumerNoWait(getConsumerIndex()));
        } else {
            setMessageReceived(getMessenger().receiveFromConsumer(getConsumerIndex(),
                                                                  getTimeout()));
        }
    }

    protected int getConsumerIndex() {
        return consumerIndex;
    }

    protected long getTimeout() {
        return timeout;
    }

    private int consumerIndex;
    private long timeout;

    public static final long NO_TIMEOUT_SET = Long.MAX_VALUE;
    public static final long NO_WAIT = Long.MIN_VALUE;
    private HJBMessage messageReceived;
}
