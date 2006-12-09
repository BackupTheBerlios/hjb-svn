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

import javax.jms.Message;

/**
 * <code>MessageCopier</code> defines the methods used to convert between
 * <code>HJBMessage</code> and <code>JMS</code> Messages.
 * 
 * @author Tim Emiola
 */
public interface MessageCopier {

    /**
     * Copies values from an HJB Message into a JMS Message.
     * 
     * @param source
     *            a <code>HJBMessage</code>
     * @param target
     *            a {@link Message}
     * @throws HJBException
     *             if a problem occurs during conversion
     */
    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException;

    /**
     * Copies values from a JMS Message into an HJB Message.
     * 
     * @param source
     *            a {@link javax.jms.Message}
     * @param target
     *            a {@link HJBMessage}
     * @throws HJBException
     *             if a problem occurs during conversion
     */
    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException;

}