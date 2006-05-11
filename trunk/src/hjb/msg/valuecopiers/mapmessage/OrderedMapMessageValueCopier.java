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
package hjb.msg.valuecopiers.mapmessage;

import hjb.msg.valuecopiers.EncodedValueCopier;
import hjb.msg.valuecopiers.OrderedPropertyValueCopier;

public class OrderedMapMessageValueCopier extends OrderedPropertyValueCopier {

    /**
     * Returns the ordered TypeMapMessageValueCopiers used in implementations of
     * various methods in this class.
     * <p />
     * They are ordered as follows:
     * <ol>
     * <li><code>MapMessageByteValueCopier</code></li>
     * <li><code>MapMessageShortValueCopier</code></li>
     * <li><code>MapMessageCharValueCopier</code></li>
     * <li><code>MapMessageIntValueCopier</code></li>
     * <li><code>MapMessageLongValueCopier</code></li>
     * <li><code>MapMessageFloatValueCopier</code></li>
     * <li><code>MapMessageDoubleValueCopier</code></li>
     * <li><code>MapMessageBooleanValueCopier</code></li>
     * <li><code>MapMessageByteArrayValueCopier</code></li>
     * <li><code>MapMessageStringValueCopier</code></li>
     * </ol>
     * 
     * @return the ordered TypeMapMessageValueCopiers as described above
     */
    public EncodedValueCopier[] getOrderedValueCopiers() {
        return ORDERED_VALUE_COPIERS;
    }

    private static EncodedValueCopier ORDERED_VALUE_COPIERS[] = new EncodedValueCopier[] {
            new MapMessageByteValueCopier(),
            new MapMessageShortValueCopier(),
            new MapMessageCharValueCopier(),
            new MapMessageIntValueCopier(),
            new MapMessageLongValueCopier(),
            new MapMessageFloatValueCopier(),
            new MapMessageDoubleValueCopier(),
            new MapMessageBooleanValueCopier(),
            new MapMessageByteArrayValueCopier(),
            new MapMessageStringValueCopier(),
    };

}
