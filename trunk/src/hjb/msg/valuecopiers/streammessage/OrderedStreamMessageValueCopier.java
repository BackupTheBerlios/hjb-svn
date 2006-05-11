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
package hjb.msg.valuecopiers.streammessage;

import java.util.LinkedList;

import hjb.msg.valuecopiers.EncodedValueCopier;
import hjb.msg.valuecopiers.OrderedPropertyValueCopier;

/**
 * <code>OrderedStreamMessageValueCopier</code>
 * 
 * @author Tim Emiola
 */
public class OrderedStreamMessageValueCopier extends OrderedPropertyValueCopier {

    public OrderedStreamMessageValueCopier(LinkedList valuesRead) {
        if (null == valuesRead)
            throw new IllegalArgumentException(strings().needsANonNull(LinkedList.class));
        this.valuesRead = valuesRead;
    }

    /**
     * Returns the ordered <code>StreamMessageValueCopier</code>s used in
     * implementations of various methods in this class.
     * <p />
     * They are ordered as follows:
     * <ol>
     * <li><code>StreamMessageByteValueCopier</code></li>
     * <li><code>StreamMessageShortValueCopier</code></li>
     * <li><code>StreamMessageCharValueCopier</code></li>
     * <li><code>StreamMessageIntValueCopier</code></li>
     * <li><code>StreamMessageLongValueCopier</code></li>
     * <li><code>StreamMessageFloatValueCopier</code></li>
     * <li><code>StreamMessageDoubleValueCopier</code></li>
     * <li><code>StreamMessageBooleanValueCopier</code></li>
     * <li><code>StreamMessageByteArrayValueCopier</code></li>
     * <li><code>StreamMessageStringValueCopier</code></li>
     * </ol>
     * 
     * @return the ordered <code>StreamMessageValueCopier</code>s as
     *         described above
     */
    public EncodedValueCopier[] getOrderedValueCopiers() {
        return new EncodedValueCopier[] {
                new StreamMessageByteValueCopier(valuesRead),
                new StreamMessageShortValueCopier(valuesRead),
                new StreamMessageCharValueCopier(valuesRead),
                new StreamMessageIntValueCopier(valuesRead),
                new StreamMessageLongValueCopier(valuesRead),
                new StreamMessageFloatValueCopier(valuesRead),
                new StreamMessageDoubleValueCopier(valuesRead),
                new StreamMessageBooleanValueCopier(valuesRead),
                new StreamMessageByteArrayValueCopier(valuesRead),
                new StreamMessageStringValueCopier(valuesRead),
        };
    }

    private transient LinkedList valuesRead;
}
