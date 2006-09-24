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
import javax.jms.MessageProducer;

import hjb.jms.HJBSessionProducersNG;
import hjb.misc.HJBStrings;
import hjb.misc.MessageProducerArguments;

public class CreateProducer extends BaseJMSCommand {

    public CreateProducer(HJBSessionProducersNG producers,
                          Destination destination,
                          MessageProducerArguments producerArguments) {
        if (null == producers) {
            throw new IllegalArgumentException(strings().needsANonNull(HJBSessionProducersNG.class));
        }
        if (null == destination) {
            throw new IllegalArgumentException(strings().needsANonNull(Destination.class));
        }
        if (null == producerArguments) {
            throw new IllegalArgumentException(strings().needsANonNull(MessageProducerArguments.class));
        }
        this.producerArguments = producerArguments;
        this.destination = destination;
        this.producers = producers;
        setProducerIndex(UNSET_PRODUCER_INDEX);
    }

    public void execute() {
        assertNotCompleted();
        try {
            setProducerIndex(getProducers().createProducer(getDestination(),
                                                           getProducerArguments()));
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_CREATE_COMMANDS,
                                   MessageProducer.class.getName(),
                                   getProducerIndexAsText());
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_CREATE_COMMANDS,
                                       MessageProducer.class.getName());
        } else {
            return getFault().getMessage();
        }
    }

    public int getProducerIndex() {
        return producerIndex;
    }

    public String getProducerIndexAsText() {
        if (isProducerIndexSet()) {
            return "" + getProducerIndex();
        } else {
            return strings().getString(HJBStrings.NOT_APPLICAPLE);
        }
    }

    public boolean isProducerIndexSet() {
        return UNSET_PRODUCER_INDEX != producerIndex;
    }

    protected void setProducerIndex(int producerIndex) {
        this.producerIndex = producerIndex;
    }

    protected MessageProducerArguments getProducerArguments() {
        return producerArguments;
    }

    protected HJBSessionProducersNG getProducers() {
        return producers;
    }

    protected Destination getDestination() {
        return destination;
    }

    private int producerIndex;
    private final MessageProducerArguments producerArguments;
    private final Destination destination;
    private final HJBSessionProducersNG producers;
    public static final int UNSET_PRODUCER_INDEX = Integer.MIN_VALUE;
}
