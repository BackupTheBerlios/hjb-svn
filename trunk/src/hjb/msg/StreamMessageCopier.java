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
import javax.jms.StreamMessage;

import hjb.http.cmd.HJBMessageWriter;
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
 *       'pos'=encodedFieldValue 'CR'
 * </pre>
 * 
 * where 'CR' is the platform specific line terminator, and 'pos' is the
 * zero-based index of position of the value
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
            copier.addToMessage(strings().getString(HJBStrings.NOT_APPLICAPLE),
                                encodedValue,
                                target);
        }
    }

    protected void copyPayLoadToHJBMessage(StreamMessage source,
                                           HJBMessage target)
            throws HJBException {
        OrderedStreamMessageValueCopier copier = new OrderedStreamMessageValueCopier(new LinkedList());
        List encodedValues = new ArrayList();
        while (true) {
            try {
                encodedValues.add(copier.getAsEncodedValue(strings().getString(HJBStrings.NOT_APPLICAPLE),
                                                           source));
            } catch (IllegalStateException e) {
                break;
            }
        }
        System.err.println("Encoded values: " + encodedValues);
        target.setEntityBody(new HJBMessageWriter().asText(asMap(encodedValues)));
    }

    protected Map asMap(List encodedValues) {
        Map result = new TreeMap();
        int count = 0;
        for (Iterator i = encodedValues.iterator(); i.hasNext(); count++) {
            result.put("" + count, i.next());
        }
        return result;
    }

    protected List asList(String encodedText) {
        Map asMap = new HJBMessageWriter().asMap(encodedText);
        Map orderedMap = new TreeMap();
        for (Iterator i = asMap.keySet().iterator(); i.hasNext();) {
            String indexAsText = (String) i.next();
            Integer index = Integer.valueOf(indexAsText);
            orderedMap.put(index, asMap.get(indexAsText));
        }
        return new ArrayList(orderedMap.values());
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
