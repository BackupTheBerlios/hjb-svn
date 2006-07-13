package hjb.jms.info;

import javax.jms.Session;

import org.jmock.Mock;

import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

public class SessionDescriptionTest extends BaseDescriptionTestCase {

    public void testConstructorShouldThrowOnNegativeIndices() {
        Mock mockSession = mock(Session.class);
        Session testSession = (Session) mockSession.proxy();
        try {
            new SessionDescription(testSession, -1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testConstructorShouldThrowOnNullInputs() {
        try {
            new SessionDescription(null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testToStringHasNoXtraInfoForNonTransactedSessions() {
        Mock mockSession = mock(Session.class);
        mockSession.stubs().method("getTransacted").will(returnValue(false));

        Session testSession = (Session) mockSession.proxy();
        SessionDescription testDescription = new SessionDescription(testSession,
                                                                    0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.SESSION);
        assertDoesNotContain(testDescription.toString(),
                             strings().getString(HJBStrings.TRANSACTED));
    }

    public void testToStringHasXtraInfoForTransactedSessions() {
        Mock mockSession = mock(Session.class);
        mockSession.stubs().method("getTransacted").will(returnValue(true));

        Session testSession = (Session) mockSession.proxy();
        SessionDescription testDescription = new SessionDescription(testSession,
                                                                    0);
        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.SESSION);
        assertContains(testDescription.toString(),
                       strings().getString(HJBStrings.TRANSACTED));
    }

    public void testLongDescriptionIncludeSessionAttributes() {
        Mock mockSession = mock(Session.class);
        mockSession.stubs().method("getTransacted").will(returnValue(true));
        mockSession.stubs()
            .method("getAcknowledgeMode")
            .will(returnValue(Session.AUTO_ACKNOWLEDGE));

        SessionDescription testDescription = new SessionDescription((Session) mockSession.proxy(),
                                                                    0);
        String expectedOutput = testDescription.toString() + CR
                + "acknowledgement-mode=(int 1)" + CR
                + "transacted=(boolean true)";
        System.err.println(testDescription.longDescription());

        assertContains(testDescription.toString(), "0");
        assertContains(testDescription.toString(), PathNaming.SESSION);
        assertContains(testDescription.toString(),
                       strings().getString(HJBStrings.TRANSACTED));
        assertEquals(expectedOutput, testDescription.longDescription());
    }

}