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
package hjb.msg.valuecopiers;

import javax.jms.Message;

import hjb.misc.HJBException;
import hjb.msg.codec.TypedValueCodec;

/**
 * <code>EncodedValueCopier</code> defines the methods used to read and write
 * encoded values to or from JMS {@link javax.jms.Message}s
 * 
 * @author Tim Emiola
 */
public interface EncodedValueCopier {

    /**
     * Adds a property or message field named <code>name</code> encoded as
     * <code>value</code> to a JMS <code>Message</code>.
     * 
     * @param name
     *            a name of property or field in <code>message</code>
     * @param encodedValue
     *            a value contained in the <code>name</code> field
     * @param message
     *            a <code>Message</code> to be updated with the field's
     *            decoded value
     * 
     * @throws HJBException
     *             if <code>name</code> is an illegal JMS field, or if
     *             <code>value</code> cannot be correctly decoded by this
     *             <code>TypePropertyConverter</code>.
     */
    public void addToMessage(String name, String encodedValue, Message message)
            throws HJBException;

    /**
     * Examines <code>message</code> to see if it has property named
     * <code>name</code> corresponding to the type this
     * <code>TypePropertyConverter</code> is responsible for handling.
     * 
     * @param name
     *            a possible name of a property in the <code>message</code>
     * @param message
     * @return <code>true</code> if the <code>name<code> property exists in
     * <code>message</code> and can be returned encoded as the type 
     * corresponding to this <code>TypedValueCodec</code>, <code>false</code>
     * otherwise.
     * @throws HJBException if a problem occurs while examining <code>message</code>
     */
    public boolean canBeEncoded(String name, Message message)
            throws HJBException;

    /**
     * Examines <code>message</code> and retrieves the property named
     * <code>name</code> corresponding to the type this
     * <code>TypePropertyConverter</code> handles.
     * 
     * @param name
     *            a possible name of a property in the <code>message</code>
     * @param message
     * @return <code>null</code> if the <code>name</code> property does not
     *         exist in this message, otherwise, the HJB encoded value of the
     *         property.
     * @throws HJBException
     *             if a problem occurs while examining <code>message</code>,
     *             or if the property can not be encoded as the type
     *             corresponding to this <code>TypedValueCodec</code>.
     */
    public String getAsEncodedValue(String name, Message message)
            throws HJBException;

    /**
     * @return the <code>TypePropertyCodec</code> used to by the
     *         EncodedValueCopier instance, if any is used.
     */
    public TypedValueCodec getCodec();

}
