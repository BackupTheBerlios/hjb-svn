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
package hjb.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.HJBMessage;
import hjb.msg.MessageCopierFactory;
import hjb.testsupport.MockSessionBuilder;

public class SessionMessageFactoryTest extends MockObjectTestCase {

    public void testCreateMessageHJBMessageThrowsIfMessageClassIsIncorrect() {
        SessionMessageFactory f = new SessionMessageFactory(testSession);
        try {
            f.createMessage(new HJBMessage(new HashMap(), ""));
            fail("should have thrown an exception");
        } catch (HJBException e) {}
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE, "");
        try {
            f.createMessage(new HJBMessage(headers, ""));
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCreateMessageHJBMessageWorksIfMessageClassIsCorrect() {
        Class[] messageClazzes = new Class[] {
                TextMessage.class,
                BytesMessage.class,
                ObjectMessage.class,
                MapMessage.class,
                StreamMessage.class,
        };
        for (int i = 0; i < messageClazzes.length; i++) {
            String clazzName = messageClazzes[i].getName();
            String shortClazzName = clazzName.substring(clazzName.lastIndexOf('.') + 1);
            Mock mockMessage = mock(messageClazzes[i]);
            Message testMessage = (Message) mockMessage.proxy();
            mockSession = mock(Session.class, "session " + shortClazzName);
            mockSession.stubs()
                .method("create" + shortClazzName)
                .will(returnValue(testMessage));
            testSession = (Session) mockSession.proxy();

            SessionMessageFactory f = new SessionMessageFactory(testSession);
            Map headers = new HashMap();
            headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE,
                        clazzName);
            assertNotNull(f.createMessage(new HJBMessage(headers, "")));
        }
    }

    public void testCreateMessageStringWorksIfMessageClassIsCorrect() {
        Class[] messageClazzes = new Class[] {
                TextMessage.class,
                BytesMessage.class,
                ObjectMessage.class,
                MapMessage.class,
                StreamMessage.class,
        };
        for (int i = 0; i < messageClazzes.length; i++) {
            String clazzName = messageClazzes[i].getName();
            String shortClazzName = clazzName.substring(clazzName.lastIndexOf('.') + 1);
            Mock mockMessage = mock(messageClazzes[i]);
            Message testMessage = (Message) mockMessage.proxy();
            mockSession = mock(Session.class, "session " + shortClazzName);
            mockSession.stubs()
                .method("create" + shortClazzName)
                .will(returnValue(testMessage));
            testSession = (Session) mockSession.proxy();

            SessionMessageFactory f = new SessionMessageFactory(testSession);
            assertNotNull(f.createMessage(clazzName));
        }
    }

    public void testCreateMessageThrowsHJBExceptionOnIncorrectMessageClass() {
        SessionMessageFactory f = new SessionMessageFactory(testSession);
        try {
            f.createMessage("");
            fail("should have thrown an exception");
        } catch (HJBException e) {}

        try {
            f.createMessage((String) null);
            fail("should have thrown an exception");
        } catch (HJBException e) {}

        try {
            f.createMessage("clearly.not.a.jms.message.class.name");
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCreateMessageThrowsHJBExceptionOnJMSException() {
        mockSession = sessionBuilder.createMockSessionThatThrowsJMSOn("createObjectMessage");
        Session aSession = (Session) mockSession.proxy();
        SessionMessageFactory f = new SessionMessageFactory(aSession);
        try {
            f.createMessage(ObjectMessage.class.getName());
        } catch (HJBException e) {}
    }

    protected void setUp() throws Exception {
        super.setUp();
        mockSession = new Mock(Session.class);
        testSession = (Session) mockSession.proxy();
        sessionBuilder = new MockSessionBuilder();
    }

    private Mock mockSession;
    private Session testSession;
    private MockSessionBuilder sessionBuilder;
}
