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

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.apache.log4j.Logger;

import hjb.http.cmd.HJBMessageWriter;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.valuecopiers.mapmessage.OrderedMapMessageValueCopier;

/**
 * <code>MapMessageCopier</code>is the <code>MessageCopier</code> for
 * <code>MapMessages</code>.
 * <p />
 * The message body is copied between the JMS and HJB representation by applying
 * the simple codecs in <code>hjb.msg.codec</code> to each field of the
 * <code>MapMessage</code> and writing each value as text in the MapMessage as
 * 
 * <pre>
 *      fieldName=encodedfieldValue 'CR'
 * </pre>
 * 
 * where CR is the platform specific carriage return.
 * 
 * @author Tim Emiola
 */
public class MapMessageCopier extends PayloadMessageCopier {

    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException {
        super.copyToJMSMessage(source, target);
        copyPayLoadToJMSMessage(source, (MapMessage) target);
    }

    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException {
        super.copyToHJBMessage(source, target);
        copyPayLoadToHJBMessage((MapMessage) source, target);
    }

    protected void copyPayLoadToJMSMessage(HJBMessage source, MapMessage target)
            throws HJBException {
        Map encodedValues = new HJBMessageWriter().asMap(source.getBody());
        OrderedMapMessageValueCopier copier = new OrderedMapMessageValueCopier();
        for (Iterator i = encodedValues.keySet().iterator(); i.hasNext();) {
            String name = (String) i.next();
            copier.addToMessage(name, (String) encodedValues.get(name), target);
        }
    }

    protected void copyPayLoadToHJBMessage(MapMessage source, HJBMessage target)
            throws HJBException {
        try {
            OrderedMapMessageValueCopier copier = new OrderedMapMessageValueCopier();
            ArrayList mapNames = Collections.list(source.getMapNames());
            Map encodedValues = new HashMap();
            for (Iterator i = mapNames.iterator(); i.hasNext();) {
                String name = (String) i.next();
                String value = copier.getAsEncodedValue(name, source);
                encodedValues.put(name, value);
            }
            target.setEntityBody(new HJBMessageWriter().asText(encodedValues));
        } catch (JMSException e) {
            String errorMessage = strings().getString(HJBStrings.COULD_NOT_GET_MAP_FROM_TEXT);
            LOG.error(errorMessage, e);
            throw new HJBException(errorMessage, e);
        }
    }
    
    protected void handleBadMapData(int i, String line) throws HJBException {
        String message = strings().getString(HJBStrings.INCORRECT_FORMAT_IN_MAP_DATA,
                                             new Integer(i),
                                             line);
        LOG.error(message);
        throw new HJBException(message);
    }

    public boolean equals(Object o) {
        return (o instanceof MapMessageCopier);
    }

    protected boolean acceptJMSMessage(Message jmsMessage) throws HJBException {
        return jmsMessage instanceof MapMessage;
    }

    protected boolean acceptHJBMessage(HJBMessage aMessage) throws HJBException {
        return MapMessage.class.getName().equals(aMessage.getHeader(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE));
    }

    private static final Logger LOG = Logger.getLogger(MapMessageCopier.class);
}