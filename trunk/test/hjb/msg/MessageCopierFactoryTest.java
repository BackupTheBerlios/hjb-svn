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
package hjb.msg;

import java.util.*;

import javax.jms.Message;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;

public class MessageCopierFactoryTest extends MockObjectTestCase {

    public void testMessageCopierFactory() {
        MessageCopierFactory f = new MessageCopierFactory();
        assertNotNull("created OK", f);
        assertEquals("Loaded distributed factory keys OK",
                     5,
                     f.getMessageCopierNames().size());
    }

    public void testGetCopierForMessageWorksForNormalJMSMessageImplementations()
            throws Exception {
        for (Iterator i = KNOWN_JMS_MESSAGE_CLASSES.iterator(); i.hasNext();) {
            Mock mockMessage = new Mock(Class.forName((String) i.next()));
            Message m = (Message) mockMessage.proxy();
            assertNotNull("copier was not found",
                          new MessageCopierFactory().getCopierFor(m));
        }
    }

    public void testGetCopierForHJBMessageThrowsWhenJMSClassFieldIsMissing() {
        HJBMessage m = new HJBMessage(new HashMap(), "test");
        try {
            new MessageCopierFactory().getCopierFor(m);
            fail("Should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetCopierForHJBMessageThrowsWhenSpecifiedJMSClassIsBogus() {
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE, "foo.bar.baz");
        HJBMessage m = new HJBMessage(headers, "test");
        try {
            new MessageCopierFactory().getCopierFor(m);
            fail("Should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testGetCopierForHJBMessageIsOKForCorrectHJBMessages() {
        for (Iterator i = KNOWN_JMS_MESSAGE_CLASSES.iterator(); i.hasNext();) {
            Map headers = new HashMap();
            headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE, i.next());
            HJBMessage m = new HJBMessage(headers, "test");
            assertNotNull("copier was not found",
                          new MessageCopierFactory().getCopierFor(m));
        }
    }

    protected static List KNOWN_JMS_MESSAGE_CLASSES = new ArrayList();
    static {
        KNOWN_JMS_MESSAGE_CLASSES.add("javax.jms.ObjectMessage");
        KNOWN_JMS_MESSAGE_CLASSES.add("javax.jms.TextMessage");
        KNOWN_JMS_MESSAGE_CLASSES.add("javax.jms.MapMessage");
        KNOWN_JMS_MESSAGE_CLASSES.add("javax.jms.BytesMessage");
        KNOWN_JMS_MESSAGE_CLASSES.add("javax.jms.StreamMessage");
    }

}
