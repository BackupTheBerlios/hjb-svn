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

import hjb.jms.HJBSessionProducers;
import hjb.misc.HJBStrings;

public class CreateProducer extends BaseJMSCommand {

    public CreateProducer(HJBSessionProducers producers,
                          int sessionIndex,
                          Destination destination) {
        if (null == producers)
            throw new IllegalArgumentException(strings().needsANonNull(HJBSessionProducers.class));
        if (null == destination)
            throw new IllegalArgumentException(strings().needsANonNull(Destination.class));
        this.producers = producers;
        this.sessionIndex = sessionIndex;
        this.destination = destination;
        setProducerIndex(UNSET_PRODUCER_INDEX);
    }

    public void execute() {
        assertNotCompleted();
        try {
            setProducerIndex(getProducers().createProducer(getSessionIndex(),
                                                           getDestination()));
        } catch (RuntimeException e) {
            setFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_CREATE_COMMANDS,
                                   MessageProducer.class.getName(),
                                   new Integer(getSessionIndex()));
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

    public boolean isProducerIndexSet() {
        return UNSET_PRODUCER_INDEX == getProducerIndex();
    }

    protected void setProducerIndex(int producerIndex) {
        this.producerIndex = producerIndex;
    }

    protected int getSessionIndex() {
        return sessionIndex;
    }

    protected HJBSessionProducers getProducers() {
        return producers;
    }

    protected Destination getDestination() {
        return destination;
    }

    private int sessionIndex;
    private int producerIndex;
    private Destination destination;
    private HJBSessionProducers producers;
    public static final int UNSET_PRODUCER_INDEX = Integer.MIN_VALUE;
}
