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

import javax.jms.Destination;
import javax.jms.MessageConsumer;

import org.apache.log4j.Logger;

import hjb.jms.HJBSessionConsumers;
import hjb.misc.HJBStrings;

public class CreateConsumer extends BaseJMSCommand {

    public CreateConsumer(HJBSessionConsumers consumers,
                          Destination destination,
                          String messageSelector,
                          boolean noLocal) {
        if (null == consumers) {
            throw new IllegalArgumentException(strings().needsANonNull(HJBSessionConsumers.class));
        }
        if (null == destination) {
            throw new IllegalArgumentException(strings().needsANonNull(Destination.class));
        }
        this.destination = destination;
        this.messageSelector = messageSelector;
        this.noLocal = noLocal;
        this.consumers = consumers;
    }

    public CreateConsumer(HJBSessionConsumers consumers,
                          Destination destination,
                          String messageSelector) {
        this(consumers, destination, messageSelector, false);
    }

    public CreateConsumer(HJBSessionConsumers consumers,
                          Destination destination) {
        this(consumers, destination, null);
    }

    public void execute() {
        assertNotCompleted();
        try {
            createConsumerAndSaveItsIndex();
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_CREATE_COMMANDS,
                                   MessageConsumer.class.getName(),
                                   getConsumerIndexAsText());
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_CREATE_COMMANDS,
                                       MessageConsumer.class.getName());
        } else {
            return getFault().getMessage();
        }
    }

    public int getConsumerIndex() {
        return consumerIndex;
    }

    public String getConsumerIndexAsText() {
        if (isConsumerIndexSet()) {
            return "" + getConsumerIndex();
        } else {
            return strings().getString(HJBStrings.NOT_APPLICAPLE);
        }
    }

    public boolean isConsumerIndexSet() {
        return UNSET_CONSUMER_INDEX != getConsumerIndex();
    }

    protected void createConsumerAndSaveItsIndex() {
        if (null == getMessageSelector() && !noLocal) {
            setConsumerIndex(getConsumers().createConsumer(getDestination()));
        } else if (null != getMessageSelector() && !noLocal) {

            setConsumerIndex(getConsumers().createConsumer(getDestination(),
                                                           getMessageSelector()));
        } else {
            setConsumerIndex(getConsumers().createConsumer(getDestination(),
                                                           getMessageSelector(),
                                                           isNoLocal()));
        }
    }

    protected void setConsumerIndex(int consumerIndex) {
        this.consumerIndex = consumerIndex;
    }

    protected Destination getDestination() {
        return destination;
    }

    protected String getMessageSelector() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Using selector: " + messageSelector);
        }
        return messageSelector;
    }

    protected boolean isNoLocal() {
        return noLocal;
    }

    protected HJBSessionConsumers getConsumers() {
        return consumers;
    }

    private int consumerIndex;
    private final Destination destination;
    private final String messageSelector;
    private final boolean noLocal;
    private final HJBSessionConsumers consumers;
    private static final Logger LOG = Logger.getLogger(CreateConsumer.class);
    public static final int UNSET_CONSUMER_INDEX = Integer.MIN_VALUE;
}
