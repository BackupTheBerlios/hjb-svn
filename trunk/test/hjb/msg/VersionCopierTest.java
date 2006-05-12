package hjb.msg;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;

public class VersionCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new VersionCopier(),
                     new VersionCopier());
        assertEquals("Hashcodes were not equal",
                     new VersionCopier().hashCode(),
                     new VersionCopier().hashCode());
        assertEquals("ToStrings were not equal",
                     new VersionCopier().toString(),
                     new VersionCopier().toString());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new VersionCopier(),
                      new Object());
    }

    public void testCopyToHJBMessageAddsHeadersWithoutUsingJMSMessage() {
        Mock mockJMSMessage = mock(TextMessage.class);

        Message testJMSMessage = (TextMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBTextMessageHeaders(),
                                                   "hjb text");
        VersionCopier c = new VersionCopier();
        c.copyToHJBMessage(testJMSMessage, testHJBMessage);
        assertNotNull(testHJBMessage.getHeader(MessageCopierFactory.HJB_MESSAGE_VERSION));
    }

    public void testCopyToJMSMessageThrowsHJBExceptionOnJMSException() {
        Mock mockJMSMessage = mock(TextMessage.class);
        mockJMSMessage.stubs()
            .method("setStringProperty")
            .with(eq(MessageCopierFactory.HJB_MESSAGE_VERSION), eq("1.0"))
            .will(throwException(new JMSException("thrown as a test")));

        Message testJMSMessage = (TextMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBTextMessageHeaders(),
                                                   "test text");
        VersionCopier c = new VersionCopier();
        try {
            c.copyToJMSMessage(testHJBMessage, testJMSMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCopyToJMSMessage() {
        Mock mockJMSMessage = mock(TextMessage.class);
        mockJMSMessage.expects(once())
            .method("setStringProperty")
            .with(eq(MessageCopierFactory.HJB_MESSAGE_VERSION), eq("1.0"));

        Message testJMSMessage = (TextMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBTextMessageHeaders(),
                                                   "hjb text");
        VersionCopier c = new VersionCopier();
        c.copyToJMSMessage(testHJBMessage, testJMSMessage);
    }

    protected Map createEmptyHJBTextMessageHeaders() {
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE,
                    TextMessage.class.getName());
        return headers;
    }

}
