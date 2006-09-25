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

import javax.jms.MessageConsumer;

import org.jmock.Mock;

import hjb.misc.HJBConstants;
import hjb.misc.PathNaming;
import hjb.testsupport.BaseHJBTestCase;

public class ConsumerDescriptionTest extends BaseHJBTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockConsumer = mock(MessageConsumer.class);
        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
        try {
            new ConsumerDescription(testConsumer,
                                    -1,
                                    defaultTestClock().getCurrentTime());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new ConsumerDescription(null,
                                    0,
                                    defaultTestClock().getCurrentTime());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testToStringIncludesTheConsumerIndex() {
        Mock mockConsumer = mock(MessageConsumer.class);
        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
        ConsumerDescription testDescription = new ConsumerDescription(testConsumer,
                                                                      0,
                                                                      defaultTestClock().getCurrentTime());
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.CONSUMER);
    }

    public void testLongDescriptionOutputsAllValues() {
        Mock mockConsumer = mock(MessageConsumer.class);

        mockConsumer.stubs()
            .method("getMessageSelector")
            .will(returnValue("testSelector"));

        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
        ConsumerDescription testDescription = new ConsumerDescription(testConsumer,
                                                                      0,
                                                                      defaultTestClock().getCurrentTime());

        String expectedOutput = testDescription.toString() + CR
                + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong() + CR
                + "message-selector=testSelector";

        assertContains(testDescription.longDescription(), "0");
        assertContains(testDescription.longDescription(), PathNaming.CONSUMER);
        assertEquals(expectedOutput, testDescription.longDescription());
        System.err.println(testDescription.longDescription());
    }

}
