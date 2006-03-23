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

import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import hjb.jms.HJBSessionDurableSubscribers;
import hjb.misc.HJBStrings;

public class CreateSubscriber extends BaseJMSCommand {

    public CreateSubscriber(HJBSessionDurableSubscribers subscribers,
                            int sessionIndex,
                            Topic topic,
                            String name,
                            String messageSelector,
                            boolean noLocal) {
        if (null == subscribers)
            throw new IllegalArgumentException(strings().needsANonNull(HJBSessionDurableSubscribers.class));
        if (null == topic)
            throw new IllegalArgumentException(strings().needsANonNull(Topic.class));
        this.sessionIndex = sessionIndex;
        this.topic = topic;
        this.name = name;
        this.messageSelector = messageSelector;
        this.noLocal = noLocal;
        this.subscribers = subscribers;
    }

    public CreateSubscriber(HJBSessionDurableSubscribers subscribers,
                            int sessionIndex,
                            Topic topic,
                            String name) {
        this(subscribers, sessionIndex, topic, name, null, false);
    }

    public void execute() {
        assertNotCompleted();
        try {
            createDurableSubscriberAndSaveItsIndex();
        } catch (RuntimeException e) {
            setFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_CREATE_COMMANDS,
                                   TopicSubscriber.class.getName(),
                                   new Integer(getSessionIndex()));
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_CREATE_COMMANDS,
                                       TopicSubscriber.class.getName());
        } else {
            return getFault().getMessage();
        }
    }

    public int getSubscriberIndex() {
        return subscriberIndex;
    }

    protected void createDurableSubscriberAndSaveItsIndex() {
        if (null == getMessageSelector() && !noLocal) {
            setDurableSubscriberIndex(getDurableSubscribers().createDurableSubscriber(getSessionIndex(),
                                                                                      getTopic(),
                                                                                      getName()));
        } else {
            setDurableSubscriberIndex(getDurableSubscribers().createDurableSubscriber(getSessionIndex(),
                                                                                      getTopic(),
                                                                                      getName(),
                                                                                      getMessageSelector(),
                                                                                      isNoLocal()));
        }
    }

    protected void setDurableSubscriberIndex(int subscriberIndex) {
        this.subscriberIndex = subscriberIndex;
    }

    protected int getSessionIndex() {
        return sessionIndex;
    }

    protected HJBSessionDurableSubscribers getDurableSubscribers() {
        return subscribers;
    }

    protected Topic getTopic() {
        return topic;
    }

    protected String getMessageSelector() {
        return messageSelector;
    }

    protected String getName() {
        return name;
    }

    protected boolean isNoLocal() {
        return noLocal;
    }

    private int sessionIndex;
    private int subscriberIndex;
    private Topic topic;
    private String messageSelector;
    private String name;
    private boolean noLocal;
    private HJBSessionDurableSubscribers subscribers;
    public static final int UNSET_SUBSCRIBER_INDEX = Integer.MIN_VALUE;
}
