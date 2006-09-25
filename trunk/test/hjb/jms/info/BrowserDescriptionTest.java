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

import javax.jms.Queue;
import javax.jms.QueueBrowser;

import org.jmock.Mock;

import hjb.misc.HJBConstants;
import hjb.misc.PathNaming;
import hjb.testsupport.BaseHJBTestCase;

public class BrowserDescriptionTest extends BaseHJBTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockBrowser = mock(QueueBrowser.class);
        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();
        try {
            new BrowserDescription(testBrowser,
                                   -1,
                                   defaultTestClock().getCurrentTime());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new BrowserDescription(null, 0, defaultTestClock().getCurrentTime());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testToStringIncludesTheNoLocal() {
        Mock mockBrowser = mock(QueueBrowser.class);
        mockBrowser.stubs().method("getQueue").will(returnValue(null));

        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();
        BrowserDescription testDescription = new BrowserDescription(testBrowser,
                                                                    0,
                                                                    defaultTestClock().getCurrentTime());
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.BROWSER);
    }

    public void testToStringIncludesTheQueueWhenAvailable() {
        Mock mockBrowser = mock(QueueBrowser.class);
        Mock mockQueue = mock(Queue.class);

        mockBrowser.stubs()
            .method("getQueue")
            .will(returnValue((Queue) mockQueue.proxy()));

        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();
        BrowserDescription testDescription = new BrowserDescription(testBrowser,
                                                                    0,
                                                                    defaultTestClock().getCurrentTime());
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.BROWSER);
        assertContains(testDescription.toString(), mockQueue.toString());
    }

    public void testLongDescriptionOutputsAllValues() {
        Mock mockBrowser = mock(QueueBrowser.class);
        Mock mockQueue = mock(Queue.class);

        mockBrowser.stubs()
            .method("getQueue")
            .will(returnValue((Queue) mockQueue.proxy()));
        mockBrowser.stubs()
            .method("getMessageSelector")
            .will(returnValue("testSelector"));

        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();
        BrowserDescription testDescription = new BrowserDescription(testBrowser,
                                                                    0,
                                                                    defaultTestClock().getCurrentTime());

        String expectedOutput = testDescription.toString() + CR
                + HJBConstants.CREATION_TIME + "="
                + defaultClockTimeAsHJBEncodedLong() + CR
                + "message-selector=testSelector";

        assertContains(testDescription.longDescription(), "0");
        assertContains(testDescription.longDescription(), PathNaming.BROWSER);
        assertEquals(expectedOutput, testDescription.longDescription());
        System.err.println(testDescription.longDescription());
    }

}
