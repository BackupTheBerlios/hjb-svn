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

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;

import org.jmock.Mock;

import hjb.misc.HJBConstants;
import hjb.misc.PathNaming;
import hjb.testsupport.BaseHJBTestCase;

public class ProducerDescriptionTest extends BaseHJBTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockProducer = mock(MessageProducer.class);
        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        try {
            new ProducerDescription(testProducer,
                                    -1,
                                    defaultTestClock().getCurrentTime());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new ProducerDescription(null,
                                    0,
                                    defaultTestClock().getCurrentTime());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testToStringIncludesThePriority() {
        Mock mockProducer = mock(MessageProducer.class);
        mockProducer.stubs().method("getPriority").will(returnValue(-4));
        mockProducer.stubs().method("getDestination").will(returnValue(null));

        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        ProducerDescription testDescription = new ProducerDescription(testProducer,
                                                                      0,
                                                                      defaultTestClock().getCurrentTime());
        assertContains(testDescription.toString(), "-4");
        assertContains(testDescription.toString(), PathNaming.PRODUCER);
    }

    public void testToStringIncludesTheDestinationWhenAvailable() {
        Mock mockProducer = mock(MessageProducer.class);
        Mock mockDestination = mock(Destination.class);

        mockProducer.stubs().method("getPriority").will(returnValue(-3));
        mockProducer.stubs()
            .method("getDestination")
            .will(returnValue((Destination) mockDestination.proxy()));

        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        ProducerDescription testDescription = new ProducerDescription(testProducer,
                                                                      0,
                                                                      defaultTestClock().getCurrentTime());
        assertContains(testDescription.toString(), "-3");
        assertContains(testDescription.toString(), PathNaming.PRODUCER);
        assertContains(testDescription.toString(), mockDestination.toString());
    }

    public void testLongDescriptionOutputsAllValues() {
        Mock mockProducer = mock(MessageProducer.class);
        Mock mockDestination = mock(Destination.class);

        mockProducer.stubs()
            .method("getDeliveryMode")
            .will(returnValue(DeliveryMode.NON_PERSISTENT));
        mockProducer.stubs().method("getPriority").will(returnValue(-3));
        mockProducer.stubs()
            .method("getDisableMessageTimestamp")
            .will(returnValue(true));
        mockProducer.stubs()
            .method("getDisableMessageID")
            .will(returnValue(false));
        mockProducer.stubs()
            .method("getTimeToLive")
            .will(returnValue(10002000L));
        mockProducer.stubs()
            .method("getDestination")
            .will(returnValue((Destination) mockDestination.proxy()));

        MessageProducer testProducer = (MessageProducer) mockProducer.proxy();
        ProducerDescription testDescription = new ProducerDescription(testProducer,
                                                                      0,
                                                                      defaultTestClock().getCurrentTime());

        String expectedOutput = testDescription.toString() + CR
                + '\t' + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong()
                + ", delivery-mode=(int 1)"
                + ", disable-message-ids=(boolean false)"
                + ", disable-timestamps=(boolean true)"
                + ", priority=(int -3), time-to-live=(long 10002000)";

        assertContains(testDescription.longDescription(), "-3");
        assertContains(testDescription.longDescription(), PathNaming.PRODUCER);
        assertEquals(expectedOutput, testDescription.longDescription());
        System.err.println(testDescription.longDescription());
    }

}
