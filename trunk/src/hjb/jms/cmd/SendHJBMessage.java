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

public class SendHJBMessage extends MessengerCommand {

    public SendHJBMessage(HJBMessenger messenger,
                          HJBMessage messageToSend,
                          int producerIndex) {
        super(messenger);
        this.messageToSend = messageToSend;
        this.producerIndex = producerIndex;
    }

    public void execute() {
        assertNotCompleted();
        try {
            getMessenger().send(getMessageToSend(), getProducerIndex());
        } catch (RuntimeException e) {
            setFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_SEND_HJB_MESSAGE,
                                   new Integer(getProducerIndex()),
                                   new Integer(getMessenger().getSessionIndex()));
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.MESSAGE_SENT_OK);
        } else {
            return getFault().getMessage();
        }
    }

    protected int getProducerIndex() {
        return producerIndex;
    }

    protected HJBMessage getMessageToSend() {
        return messageToSend;
    }

    private HJBMessage messageToSend;
    private int producerIndex;
}
