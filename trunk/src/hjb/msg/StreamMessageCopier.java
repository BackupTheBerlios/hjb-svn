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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import javax.jms.Message;
import javax.jms.StreamMessage;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.valuecopiers.streammessage.OrderedStreamMessageValueCopier;

/**
 * <code>StreamMessageCopier</code> is the <code>MessageCopier</code> for
 * <code>StreamMessages</code>.
 * <p />
 * The message body is copied between the JMS and HJB representation by applying
 * the simple codecs in <code>hjb.msg.codec</code> to each field read/written
 * from the <code>StreamMessage</code> and representing each value in the text
 * of the <code>HJBMessage</code> as
 * 
 * <pre>
 *      encodedFieldValue 'CR'
 * </pre>
 * 
 * TODO write a test case for this class
 * 
 * @author Tim Emiola
 */
public class StreamMessageCopier extends PayloadMessageCopier {

    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException {
        super.copyToJMSMessage(source, target);
        copyPayLoadToJMSMessage(source, (StreamMessage) target);
    }

    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException {
        super.copyToHJBMessage(source, target);
        copyPayLoadToHJBMessage((StreamMessage) source, target);
    }

    protected void copyPayLoadToJMSMessage(HJBMessage source,
                                           StreamMessage target)
            throws HJBException {
        OrderedStreamMessageValueCopier copier = new OrderedStreamMessageValueCopier(new LinkedList());
        List valuesInMessage = asList(source.getBody());
        for (Iterator i = valuesInMessage.iterator(); i.hasNext();) {
            String encodedValue = (String) i.next();
            copier.addToMessage(null, encodedValue, target);
        }
    }

    protected void copyPayLoadToHJBMessage(StreamMessage source,
                                           HJBMessage target)
            throws HJBException {
        OrderedStreamMessageValueCopier copier = new OrderedStreamMessageValueCopier(new LinkedList());
        List encodedValues = new ArrayList();
        while (true) {
            try {
                encodedValues.add(copier.getAsEncodedValue(null, source));
            } catch (IllegalStateException e) {
                break;
            }
        }
        target.setEntityBody(asText(encodedValues));
    }

    protected String asText(List encodedValues) {
        StringWriter result = new StringWriter();
        PrintWriter pw = new PrintWriter(result);
        for (Iterator i = encodedValues.iterator(); i.hasNext();) {
            String name = (String) i.next();
            pw.print(name);
            pw.println();
        }
        return result.toString();
    }

    protected List asList(String encodedText) {
        if (null == encodedText || 0 == encodedText.length())
            return new ArrayList();
        String[] lines = encodedText.split("\\n");
        return Arrays.asList(lines);
    }

    protected boolean acceptJMSMessage(Message jmsMessage) throws HJBException {
        return jmsMessage instanceof StreamMessage;
    }

    protected boolean acceptHJBMessage(HJBMessage aMessage) throws HJBException {
        return StreamMessage.class.getName().equals(aMessage.getHeader(MessageCopierFactory.HJB_JMS_CLASS));
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private static final HJBStrings STRINGS = new HJBStrings();
}
