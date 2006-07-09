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
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.misc.MessageProducerArguments;
import hjb.msg.HJBMessage;

import javax.jms.Destination;

public class SendHJBMessage extends MessengerCommand {

    public SendHJBMessage(HJBMessenger messenger,
                          HJBMessage messageToSend,
                          int producerIndex,
                          MessageProducerArguments producerArguments,
                          Destination destination) {
        super(messenger);
        this.messageToSend = messageToSend;
        this.producerIndex = producerIndex;
        this.producerArguments = producerArguments;
        this.destination = destination;
    }

    public void execute() {
        assertNotCompleted();
        try {
            sendTheMessage();
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_SEND_HJB_MESSAGE,
                                   new Integer(getProducerIndex()),
                                   getMessenger().getSessionDescription());
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.MESSAGE_SENT_OK);
        } else {
            return getFault().getMessage();
        }
    }

    public HJBMessage getMessageThatWasSent() {
        return messageThatWasSent;
    }

    protected void sendTheMessage() throws HJBException {
        messageThatWasSent = getMessenger().send(getMessageToSend(),
                                                 getDestination(),
                                                 getProducerArguments(),
                                                 getProducerIndex());
    }

    protected boolean isDestinationSupplied() {
        return null != getDestination();
    }

    protected Destination getDestination() {
        return destination;
    }

    protected MessageProducerArguments getProducerArguments() {
        return producerArguments;
    }

    protected boolean areProducerArgumentsPresent() {
        return null != getProducerArguments();
    }

    protected int getProducerIndex() {
        return producerIndex;
    }

    protected HJBMessage getMessageToSend() {
        return messageToSend;
    }

    private HJBMessage messageThatWasSent;
    private HJBMessage messageToSend;
    private int producerIndex;
    private MessageProducerArguments producerArguments;
    private Destination destination;
}
