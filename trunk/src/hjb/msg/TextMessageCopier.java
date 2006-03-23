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

import hjb.misc.HJBException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * <code>TextMessageCopier</code> is the <code>MessageCopier</code> for
 * <code>TextMessages</code>.
 * 
 * TODO write a test case for this class
 * 
 * @author Tim Emiola
 */
public class TextMessageCopier extends PayloadMessageCopier {

    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException {
        super.copyToJMSMessage(source, target);
        copyPayLoadToJMSMessage(source, (TextMessage) target);
    }

    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException {
        super.copyToHJBMessage(source, target);
        copyPayLoadToHJBMessage((TextMessage) source, target);
    }

    protected void copyPayLoadToJMSMessage(HJBMessage source, TextMessage target) {
        try {
            target.setText(source.getBody());
        } catch (JMSException e) {
            handleMessageCopyFailure(target, e);
        }
    }

    public void copyPayLoadToHJBMessage(TextMessage source, HJBMessage target)
            throws HJBException {
        try {
            target.setEntityBody(source.getText());
        } catch (JMSException e) {
            handleMessageCopyFailure(source, e);
        }
    }

    protected boolean acceptJMSMessage(Message jmsMessage) throws HJBException {
        return jmsMessage instanceof TextMessage;
    }

    protected boolean acceptHJBMessage(HJBMessage aMessage) throws HJBException {
        return TextMessage.class.getName().equals(aMessage.getHeader(MessageCopierFactory.HJB_JMS_CLASS));
    }
}
