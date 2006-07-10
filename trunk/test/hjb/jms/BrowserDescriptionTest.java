package hjb.jms;

import javax.jms.Queue;
import javax.jms.QueueBrowser;

import org.jmock.Mock;

import hjb.http.cmd.PathNaming;

public class BrowserDescriptionTest extends BaseDescriptionTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockBrowser = mock(QueueBrowser.class);       
        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();
        try {
            new BrowserDescription(testBrowser, -1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new BrowserDescription(null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }
    
    public void testToStringIncludesTheNoLocal() {
        Mock mockBrowser = mock(QueueBrowser.class);
        mockBrowser.stubs().method("getQueue").will(returnValue(null));
        
        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();
        BrowserDescription testDescription = new BrowserDescription(testBrowser, 0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.BROWSER);
    }
    
    public void testToStringIncludesTheQueueWhenAvailable() {
        Mock mockBrowser = mock(QueueBrowser.class);
        Mock mockQueue = mock(Queue.class);
        
        mockBrowser.stubs().method("getQueue").will(returnValue((Queue) mockQueue.proxy()));
        
        QueueBrowser testBrowser = (QueueBrowser) mockBrowser.proxy();
        BrowserDescription testDescription = new BrowserDescription(testBrowser, 0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.BROWSER);
        assertContains(testDescription.toString(), mockQueue.toString());
    }

}
