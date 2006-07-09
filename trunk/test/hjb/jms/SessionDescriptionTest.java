package hjb.jms;

import javax.jms.Session;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class SessionDescriptionTest extends MockObjectTestCase {
    
    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockSession = mock(Session.class);       
        Session testSession = (Session) mockSession.proxy();
        try {
            new SessionDescription(testSession, -1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }

    public void testConstructorShouldThrowOnNullSessions() {
        try {
            new SessionDescription(null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {            
        }
    }

}
