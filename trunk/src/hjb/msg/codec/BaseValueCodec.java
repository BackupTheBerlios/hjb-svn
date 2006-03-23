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

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>BaseValueCodec</code> implements methods useful methods for
 * implementations of <code>TypedValueCodec</codec>
 *
 * @author Tim Emiola
 */
public abstract class BaseValueCodec implements TypedValueCodec {

    public boolean equals(Object o) {
        if (!(o instanceof BaseValueCodec)) return false;
        return this.getClass().getName().equals(o.getClass().getName());
    }

    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    public String toString() {
        String clazzName = this.getClass().getName();
        if (-1 == clazzName.lastIndexOf('.')) return "[" + clazzName + "]";
        return "[" + clazzName.substring(clazzName.lastIndexOf('.') + 1) + "]";
    }

    protected String handleBadEncodedType(Object value) {
        String errorMessage = strings().getString(HJBStrings.WRONG_TYPE_TO_ENCODE,
                                                  this,
                                                  (null == value ? "null"
                                                          : value),
                                                  (null == value ? "null"
                                                          : value.getClass().getName()));
        LOG.error(errorMessage);
        throw new HJBException(errorMessage);
    }

    protected void handleDecodingFailure(String value) {
        String message = strings().getString(HJBStrings.DECODER_MISMATCH,
                                             this,
                                             value);
        LOG.error(message);
        throw new HJBException(message);
    }

    protected void handleDecodingFailure(String value, Exception e) {
        String message = strings().getString(HJBStrings.DECODER_MISMATCH,
                                             this,
                                             value);
        LOG.error(message, e);
        throw new HJBException(message, e);
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private static final Logger LOG = Logger.getLogger(BaseValueCodec.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
