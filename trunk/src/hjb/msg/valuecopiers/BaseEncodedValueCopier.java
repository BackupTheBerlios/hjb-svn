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

import javax.jms.MessageEOFException;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.codec.TypedValueCodec;

public abstract class BaseEncodedValueCopier implements EncodedValueCopier {

    public BaseEncodedValueCopier(TypedValueCodec codec) {
        if (null == codec)
            throw new IllegalArgumentException(strings().needsANonNull(TypedValueCodec.class));
        this.codec = codec;
    }

    public TypedValueCodec getCodec() {
        return codec;
    }

    public boolean equals(Object o) {
        if (!(o instanceof BaseEncodedValueCopier)) return false;
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

    protected void handleValueWriteFailure(String name,
                                           String encodedValue,
                                           Exception e) {
        String errorMessage = strings().getString(HJBStrings.COULD_NOT_WRITE_VALUE_TO_MESSAGE,
                                                  name,
                                                  encodedValue);
        LOG.error(errorMessage, e);
        throw new HJBException(errorMessage, e);
    }

    protected String handleValueReadFailure(String name, Exception e) {
        if (e instanceof MessageEOFException) {
            throw new IllegalStateException(e.getMessage());
        }
        String errorMessage = strings().getString(HJBStrings.COULD_NOT_READ_VALUE_FROM_MESSAGE,
                                                  name);
        LOG.error(errorMessage, e);
        throw new HJBException(errorMessage, e);
    }

    protected Object decode(String encodedValue) {
        return getCodec().decode(encodedValue);
    }

    protected String encode(Object value) {
        return getCodec().encode(value);
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private TypedValueCodec codec;
    private static final Logger LOG = Logger.getLogger(BaseEncodedValueCopier.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
