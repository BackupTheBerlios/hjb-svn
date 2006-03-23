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

import java.util.ArrayList;
import java.util.Arrays;

import javax.jms.Message;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.codec.OrderedTypedValueCodec;
import hjb.msg.codec.TypedValueCodec;

public class OrderedPropertyValueCopier extends BaseEncodedValueCopier {

    public OrderedPropertyValueCopier() {
        super(new OrderedTypedValueCodec());
    }

    public void addToMessage(String name, String encodedValue, Message message)
            throws HJBException {
        TypedValueCodec[] orderedCodecs = getOrderedCodecs();
        EncodedValueCopier[] orderedCopiers = getOrderedValueCopiers();

        for (int i = 0; i < orderedCodecs.length; i++) {
            if (!orderedCodecs[i].isEncoded(encodedValue)) {
                continue;
            }
            orderedCopiers[i].addToMessage(name, encodedValue, message);
            return;
        }
        String errorMessage = strings().getString(HJBStrings.COULD_NOT_FIND_CODEC_TO_DECODE,
                                                  encodedValue,
                                                  new ArrayList(Arrays.asList(orderedCodecs)));
        HJBException e = new HJBException(errorMessage);
        e.fillInStackTrace();
        handleValueWriteFailure(name, encodedValue, e);
    }

    public boolean canBeEncoded(String name, Message message)
            throws HJBException {
        EncodedValueCopier[] orderedCopiers = getOrderedValueCopiers();
        for (int i = 0; i < orderedCopiers.length; i++) {
            if (orderedCopiers[i].canBeEncoded(name, message)) return true;
        }
        return false;
    }

    public String getAsEncodedValue(String name, Message message)
            throws HJBException {

        EncodedValueCopier[] orderedCopiers = getOrderedValueCopiers();
        for (int i = 0; i < orderedCopiers.length; i++) {
            if (orderedCopiers[i].canBeEncoded(name, message))
                return orderedCopiers[i].getAsEncodedValue(name, message);
        }
        String errorMessage = strings().getString(HJBStrings.COULD_NOT_FIND_CODEC_TO_DECODE,
                                                  "name: " + name,
                                                  new ArrayList(Arrays.asList(orderedCopiers)));
        HJBException e = new HJBException(errorMessage);
        e.fillInStackTrace();
        handleValueReadFailure(name, e);
        return null;
    }

    protected TypedValueCodec[] getOrderedCodecs() {
        ArrayList codecs = new ArrayList();
        for (int i = 0; i < getOrderedValueCopiers().length; i++) {
            codecs.add(getOrderedValueCopiers()[i].getCodec());
        }
        return (TypedValueCodec[]) codecs.toArray(new TypedValueCodec[codecs.size()]);
    }

    /**
     * Returns the ordered TypeEncodedValueCopiers used in implementations of
     * various methods in this class.
     * <p />
     * They are ordered as follows:
     * <ol>
     * <li><code>BytePropertyValueCopier</code></li>
     * <li><code>ShortPropertyValueCopier</code></li>
     * <li><code>IntPropertyValueCopier</code></li>
     * <li><code>LongPropertyValueCopier</code></li>
     * <li><code>FloatPropertyValueCopier</code></li>
     * <li><code>DoublePropertyValueCopier</code></li>
     * <li><code>BooleanPropertyValueCopier</code></li>
     * <li><code>StringPropertyValueCopier</code></li>
     * </ol>
     * 
     * @return the ordered TypeEncodedValueCopiers as described above
     */
    public EncodedValueCopier[] getOrderedValueCopiers() {
        return ORDERED_VALUE_COPIERS;
    }

    private static EncodedValueCopier ORDERED_VALUE_COPIERS[] = new EncodedValueCopier[] {
            new BytePropertyValueCopier(),
            new ShortPropertyValueCopier(),
            new IntPropertyValueCopier(),
            new LongPropertyValueCopier(),
            new FloatPropertyValueCopier(),
            new DoublePropertyValueCopier(),
            new BooleanPropertyValueCopier(),
            new StringPropertyValueCopier(),
    };

}
