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
package hjb.jms.info;

import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.jmock.Mock;

import hjb.misc.PathNaming;
import hjb.testsupport.BaseHJBTestCase;

public class SubscriberDescriptionTest extends BaseHJBTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockSubscriber = mock(TopicSubscriber.class);
        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        try {
            new SubscriberDescription(testSubscriber, -1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new SubscriberDescription(null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testToStringIncludesTheNoLocal() {
        Mock mockSubscriber = mock(TopicSubscriber.class);
        mockSubscriber.stubs().method("getNoLocal").will(returnValue(false));
        mockSubscriber.stubs().method("getTopic").will(returnValue(null));

        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        SubscriberDescription testDescription = new SubscriberDescription(testSubscriber,
                                                                          0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.SUBSCRIBER);
    }

    public void testToStringIncludesTheTopicWhenAvailable() {
        Mock mockSubscriber = mock(TopicSubscriber.class);
        Mock mockTopic = mock(Topic.class);

        mockSubscriber.stubs().method("getNoLocal").will(returnValue(false));
        mockSubscriber.stubs()
            .method("getTopic")
            .will(returnValue((Topic) mockTopic.proxy()));

        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        SubscriberDescription testDescription = new SubscriberDescription(testSubscriber,
                                                                          0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), "false");
        assertContains(testDescription.toString(), PathNaming.SUBSCRIBER);
        assertContains(testDescription.toString(), mockTopic.toString());
    }

    public void testLongDescriptionOutputsAllValues() {
        Mock mockSubscriber = mock(TopicSubscriber.class);
        Mock mockTopic = mock(Topic.class);

        mockTopic.stubs().method("getTopicName").will(returnValue("testTopic"));
        mockSubscriber.stubs()
            .method("getTopic")
            .will(returnValue((Topic) mockTopic.proxy()));
        mockSubscriber.stubs()
            .method("getMessageSelector")
            .will(returnValue("testSelector"));
        mockSubscriber.stubs().method("getNoLocal").will(returnValue(false));

        TopicSubscriber testSubscriber = (TopicSubscriber) mockSubscriber.proxy();
        SubscriberDescription testDescription = new SubscriberDescription(testSubscriber,
                                                                          0);

        String expectedOutput = testDescription.toString() + CR
                + "message-selector=testSelector" + CR
                + "no-local=(boolean false)" + CR + "subscriber-name=testTopic";

        assertContains(testDescription.longDescription(), "0");
        assertContains(testDescription.longDescription(), PathNaming.SUBSCRIBER);
        assertEquals(expectedOutput, testDescription.longDescription());
        System.err.println(testDescription.longDescription());
    }

}
