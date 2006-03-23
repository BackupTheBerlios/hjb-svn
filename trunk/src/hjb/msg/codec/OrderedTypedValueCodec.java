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

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>OrderedTypedValueCodec</code> is used to encode/decode between
 * property values of all types in JMS {@link javax.jms.Message}s and string
 * values in {@link hjb.msg.HJBMessage}s
 * 
 * <p />
 * It achieves this by chaining together the <code>TypedValueCodec</code>s
 * defined in the package <code>hjb.msg</code> so that any valid
 * encoded/decoded property value is correctly handled.
 * 
 * <p />
 * All its implementation methods are implemented by looping through the chain
 * of TypePropertyCodec and executiing when the appropriate one is reached.
 * 
 * @author Tim Emiola
 */
public class OrderedTypedValueCodec extends BaseValueCodec {

    public boolean isEncoded(String value) {
        TypedValueCodec[] orderedCodecs = getOrderedCodecs();
        for (int i = 0; i < orderedCodecs.length; i++) {
            if (orderedCodecs[i].isEncoded(value)) return true;
        }
        return false;
    }

    public Object decode(String value) throws HJBException {
        TypedValueCodec[] orderedCodecs = getOrderedCodecs();
        for (int i = 0; i < orderedCodecs.length; i++) {
            if (orderedCodecs[i].isEncoded(value)) {
                return orderedCodecs[i].decode(value);
            }
        }
        String message = strings().getString(HJBStrings.COULD_NOT_FIND_CODEC_TO_DECODE,
                                             value,
                                             new ArrayList(Arrays.asList(orderedCodecs)));
        LOG.error(message);
        throw new HJBException(message);
    }

    public String encode(Object value) throws HJBException {
        TypedValueCodec[] orderedCodecs = getOrderedCodecs();
        for (int i = 0; i < orderedCodecs.length; i++) {
            try {
                return orderedCodecs[i].encode(value);
            } catch (HJBException e) {}
        }
        String message = strings().getString(HJBStrings.COULD_NOT_FIND_CODEC_TO_ENCODE,
                                             value,
                                             new ArrayList(Arrays.asList(orderedCodecs)));
        LOG.error(message);
        throw new HJBException(message);
    }

    /**
     * Returns the ordered TypePropertyCodecs used in implementations of various
     * methods in this class.
     * <p />
     * They are ordered as follows:
     * <ol>
     * <li><code>ByteCodec</code></li>
     * <li><code>ShortCodec</code></li>
     * <li><code>CharCodec</code></li>
     * <li><code>IntCodec</code></li>
     * <li><code>LongCodec</code></li>
     * <li><code>FloatCodec</code></li>
     * <li><code>DoubleCodec</code></li>
     * <li><code>BooleanCodec</code></li>
     * <li><code>ByteArrayCodec</code></li>
     * <li><code>StringCodec</code></li>
     * </ol>
     * 
     * @return the ordered TypePropertyCodecs as described above
     */
    public TypedValueCodec[] getOrderedCodecs() {
        return ORDERED_CODECS;
    }

    private static TypedValueCodec ORDERED_CODECS[] = new TypedValueCodec[] {
            new ByteCodec(),
            new ShortCodec(),
            new CharCodec(),
            new IntCodec(),
            new LongCodec(),
            new FloatCodec(),
            new DoubleCodec(),
            new BooleanCodec(),
            new ByteArrayCodec(),
            new StringCodec(),
    };

    private static final Logger LOG = Logger.getLogger(OrderedTypedValueCodec.class);

}
