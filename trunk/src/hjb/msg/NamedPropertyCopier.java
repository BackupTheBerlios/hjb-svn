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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.valuecopiers.EncodedValueCopier;
import hjb.msg.valuecopiers.OrderedPropertyValueCopier;

/**
 * <code>NamePropertyCopier</code> is used to convert headers in
 * {@link hjb.msg.HJBMessage}s to the various named properties on a
 * {@link javax.jms.Message}s and vice versa.
 * 
 * @author Tim Emiola
 */
public class NamedPropertyCopier implements MessageCopier {

    public NamedPropertyCopier() {
        this.valueCopier = new OrderedPropertyValueCopier();
        this.assistant = new AttributeCopierAssistant();
    }

    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException {
        Set headerNames = source.getHeaders().keySet();
        for (Iterator i = headerNames.iterator(); i.hasNext();) {
            String name = (String) i.next();
            if (doNotCopy(name)) continue;
            String value = source.getHeader(name);
            getValueCopier().addToMessage(name, value, target);
        }
    }

    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException {
        try {
            List propertyNames = Collections.list(source.getPropertyNames());
            for (Iterator i = propertyNames.iterator(); i.hasNext();) {
                String propertyName = (String) i.next();
                target.addHeader(propertyName,
                                 getValueCopier().getAsEncodedValue(propertyName,
                                                                    source));
            }
        } catch (JMSException e) {
            String message = strings().getString(HJBStrings.COULD_NOT_GET_MESSAGE_PROPERTIES);
            LOG.error(message, e);
            throw new HJBException(message, e);
        }
    }

    /**
     * Overrides {@link Object#equals(java.lang.Object)} to ensure all
     * <code>NamedPropertyCopier</code> instances are equivalent.
     */
    public boolean equals(Object o) {
        return (o instanceof NamedPropertyCopier);
    }

    /**
     * Overrides {@link Object#hashCode()} to ensure it is consistent with the
     * implementation of {@link #equals(Object)}.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    public String toString() {
        return this.getClass().getName();
    }

    protected boolean doNotCopy(String headerName) {
        return getAssistant().isBuiltinHeader(headerName)
                || MessageCopierFactory.HJB_JMS_CLASS.equals(headerName);
    }

    protected EncodedValueCopier getValueCopier() {
        return valueCopier;
    }

    protected AttributeCopierAssistant getAssistant() {
        return assistant;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private EncodedValueCopier valueCopier;
    private AttributeCopierAssistant assistant;

    private static final Logger LOG = Logger.getLogger(NamedPropertyCopier.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
