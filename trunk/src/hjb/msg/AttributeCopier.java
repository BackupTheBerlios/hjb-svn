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

import java.util.Iterator;

import javax.jms.Message;

/**
 * <code>AttributeCopier</code> is used to convert headers in
 * {@link hjb.msg.HJBMessage}s to the various attribute values on
 * {@link javax.jms.Message}s and vice versa.
 * 
 * @author Tim Emiola
 */
public class AttributeCopier implements MessageCopier {

    public AttributeCopier() {
        this.assistant = new AttributeCopierAssistant();
    }

    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException {
        for (Iterator i = source.getHeaders().keySet().iterator(); i.hasNext();) {
            String name = (String) i.next();
            getAssistant().addToMessage(name, source.getHeader(name), target);
        }
    }

    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException {
        String[] headerNames = AttributeCopierAssistant.BUILT_IN_HEADERS;
        for (int i = 0; i < headerNames.length; i++) {
            String value = getAssistant().getEncodedValueFromMessage(headerNames[i],
                                                                     source);
            if (null == value) continue;
            target.addHeader(headerNames[i], value);
        }
    }

    public boolean equals(Object o) {
        return (o instanceof AttributeCopier);
    }

    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    public String toString() {
        return this.getClass().getName();
    }

    protected AttributeCopierAssistant getAssistant() {
        return assistant;
    }

    private AttributeCopierAssistant assistant;
}
