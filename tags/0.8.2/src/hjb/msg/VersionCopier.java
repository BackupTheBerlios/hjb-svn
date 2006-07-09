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

import javax.jms.Message;

import hjb.misc.HJBException;
import hjb.msg.valuecopiers.EncodedValueCopier;
import hjb.msg.valuecopiers.StringPropertyValueCopier;

/**
 * <code>VersionCopier</code> is used to add the current HJB message version
 * to HJB messages.
 * 
 * @author Tim Emiola
 */
public class VersionCopier implements MessageCopier {

    public VersionCopier() {
        this.valueCopier = new StringPropertyValueCopier();
    }

    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException {
        getValueCopier().addToMessage(MessageCopierFactory.HJB_MESSAGE_VERSION,
                                      MessageCopierFactory.HJB_MESSAGE_VERSION_1_0,
                                      target);
    }

    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException {
        target.addHeader(MessageCopierFactory.HJB_MESSAGE_VERSION,
                         MessageCopierFactory.HJB_MESSAGE_VERSION_1_0);
    }

    /**
     * Overrides {@link Object#equals(java.lang.Object)} to ensure all
     * <code>VersionCopier</code> instances are equivalent.
     */
    public boolean equals(Object o) {
        return (o instanceof VersionCopier);
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

    protected EncodedValueCopier getValueCopier() {
        return valueCopier;
    }

    private EncodedValueCopier valueCopier;
}
