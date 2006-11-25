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
package hjb.http.cmd;

import hjb.misc.HJBConstants;
import hjb.misc.HJBStrings;
import hjb.msg.codec.OrderedTypedValueCodec;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * <code>ParameterMapDecoder</code> is used to translate the values in the
 * <code>Map</code> returned by
 * {@link javax.servlet.ServletRequest#getParameterMap()}.
 * 
 * 
 * @author Tim Emiola
 */
public class ParameterMapDecoder {

    public ParameterMapDecoder(String[] compulsoryDecodings) {
        if (null != compulsoryDecodings) {
            this.compulsoryDecodings = compulsoryDecodings;
        } else {
            this.compulsoryDecodings = new String[0];
        }
    }

    public ParameterMapDecoder() {
        this(MUST_BE_DECODED);
    }

    /**
     * Decodes the values in <code>parameterMap</code>.
     * <p />
     * 
     * It assumes that
     * <ul>
     * <li>Only the first element of the array value (the input map's values
     * are <code>String</code> arrays) is relevant, (because the other value
     * <em>must</em> have been sent by mistake!)</li>
     * <li>The value will be encoded, and may require decoding using a
     * {@link hjb.msg.codec.TypedValueCodec}</li>
     * <li>Only the value of the parameters returned by
     * {@link #getCompulsoryDecodings()} are decoded. The others as passed
     * through as-is.
     * </ul>
     * 
     * @param parameterMap
     *            a <code>Map</code> like that returned by
     *            {@link javax.servlet.ServletRequest#getParameterMap()}
     * @return an unmodifiable <code>Map</code> containing the values decoded
     *         from <code>parameterMap</code>
     */
    public Map decode(Map parameterMap) {
        Map result = new HashMap();
        if (null == parameterMap) return Collections.unmodifiableMap(result);
        showMapInDebug(parameterMap, "Raw");
        List toBeDecoded = getCompulsoryDecodings();
        for (Iterator i = parameterMap.keySet().iterator(); i.hasNext();) {
            Object next = i.next();
            try {
                String parameterName = (String) next;
                String[] value = (String[]) parameterMap.get(next);
                if (toBeDecoded.contains(parameterName)) {
                    result.put(parameterName, getCodec().decode(value[0]));
                } else {
                    result.put(parameterName, value[0]);
                }
            } catch (ClassCastException e) {
                LOG.warn(strings().getString(HJBStrings.BAD_VALUES_IN_PARAMETER_MAP));
            } catch (IndexOutOfBoundsException e) {
                LOG.error(e); // paranoia setting in!
            }
        }
        showMapInDebug(result, "Decoded");
        return Collections.unmodifiableMap(result);
    }
    
    protected void showMapInDebug(Map aMap, String title) {
        if (! LOG.isDebugEnabled()) {
            return;
        }
        Map toPrint = new HashMap();
        for (Iterator i = aMap.entrySet().iterator(); i.hasNext();) {
            Map.Entry anEntry = (Map.Entry) i.next();
            Object value = anEntry.getValue();
            if (value instanceof String []) {
                String[] valueStrings = (String []) value;
                StringBuffer asText = new StringBuffer();
                for (int j = 0; j < valueStrings.length; j++) {
                    if (j != 0) {
                        asText.append(",");
                    }
                    asText.append(valueStrings[j]);
                }
                toPrint.put("" + anEntry.getKey(), asText.toString());
            } else {
                toPrint.put("" + anEntry.getKey(), "" + value);
            }
        }
        LOG.debug(title + ": " + toPrint);
    }

    /**
     * Overrides {@link Object#equals(java.lang.Object)} to ensure all
     * <code>ParameterMapDecoder</code> instances are equivalent.
     */
    public boolean equals(Object o) {
        return o instanceof ParameterMapDecoder;
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

    protected OrderedTypedValueCodec getCodec() {
        return CODEC;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected List getCompulsoryDecodings() {
        return Arrays.asList(compulsoryDecodings);
    }

    public static String[] MUST_BE_DECODED = new String[] {
            HJBConstants.CONSUMER_NOLOCAL,
            HJBConstants.DELIVERY_MODE,
            HJBConstants.DISABLE_TIMESTAMPS,
            HJBConstants.DISABLE_MESSAGE_IDS,
            HJBConstants.PRIORITY,
            HJBConstants.SESSION_ACKNOWLEDGEMENT_MODE,
            HJBConstants.SESSION_TRANSACTED,
            HJBConstants.TIME_TO_LIVE,
            HJBConstants.TIMEOUT,
    };

    private String[] compulsoryDecodings;
    private static OrderedTypedValueCodec CODEC = new OrderedTypedValueCodec();
    private static final Logger LOG = Logger.getLogger(ParameterMapDecoder.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
