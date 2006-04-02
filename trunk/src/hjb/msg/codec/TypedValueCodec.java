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
package hjb.msg.codec;

import hjb.misc.HJBException;

/**
 * <code>TypedValueCodec</code> declares methods used to assist translation
 * between the typed property/header/attribute values in JMS
 * {@link javax.jms.Message}s and their encoded string versions in
 * {@link hjb.msg.HJBMessage}s.
 * 
 * @author Tim Emiola
 */
public interface TypedValueCodec {

    /**
     * Checks whether a <code>value</code> is an encoded form corresponding to
     * this <code>TypedValueCodec</code>.
     * 
     * @param value
     *            the value being tested
     * @return <code>true</code> if <code>value</code> is the encoded form
     *         corresponding to the type of this
     *         <code>TypePropertyConverter</code>
     */
    public boolean isEncoded(String value);

    /**
     * Decodes <code>value</code> as if it was of the type corresponding to
     * this <code>TypedValueCodec</code>.
     * 
     * @param value
     *            the value to be decoded.
     * @throws HJBException
     *             if <code>value</code> cannot be decoded by this
     *             <code>TypedValueCodec</code>.
     */
    public Object decode(String value) throws HJBException;

    /**
     * Encodes <code>value</code>.
     * 
     * @param value
     *            the value to be encode.
     * @return the result of applying this <code>TypePropertyCodec's</code>
     *         encoding to <code>value<code>
     * @throws HJBException if <code>value</code> cannot be encoded by this 
     * <code>TypedValueCodec</code>
     */
    public String encode(Object value) throws HJBException;

}
