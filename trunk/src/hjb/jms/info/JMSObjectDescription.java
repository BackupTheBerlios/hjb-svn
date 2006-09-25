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
package hjb.jms.info;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import hjb.http.cmd.HJBMessageWriter;
import hjb.misc.HJBStrings;
import hjb.msg.codec.OrderedTypedValueCodec;

/**
 * <code>JMSObjectDescription</code> is the base class for the various
 * XXXDescription classes, which are used to provided descriptions of JMS
 * objects in log and status messages.
 * 
 * It defines a number of template methods for its subclasses override.
 * 
 * @author Tim Emiola
 */
public class JMSObjectDescription {

    public JMSObjectDescription(int index, String invalidIndexMessage) {
        if (index < 0) {
            throw new IllegalArgumentException(strings().getString(invalidIndexMessage,
                                                                   new Integer(index)));
        }
        this.codec = new OrderedTypedValueCodec();
        this.index = index;
    }

    /**
     * Overrides the {@link Object#toString()} to return {@link #getBaseName()}
     * combined with {@link #getExtraInformation()}.
     */
    public String toString() {
        return getBaseName() + getExtraInformation();
    }

    /**
     * Provides a detailed description of the JMS object, often spread over
     * multiple lines.
     * 
     * @return a detailed description of the JMS object.
     */
    public String longDescription() {
        StringWriter sw = new StringWriter();
        writeLongDescription(sw);
        return sw.toString();
    }

    /**
     * Writes a detailed description of the JMS object as (in
     * {@link #longDescription()} to <code>aWriter</code>.
     * 
     * @param aWriter
     *            has <code>longDescription</code> written to it.
     */
    public void writeLongDescription(Writer aWriter) {
        PrintWriter pw = new PrintWriter(aWriter);
        pw.print(this);
        pw.println();
        new HJBMessageWriter().writeAsText(attributesAsAMap(), pw);
    }

    /**
     * @return a Map containing the JMS object's attributes.
     */
    protected Map attributesAsAMap() {
        return Collections.EMPTY_MAP;
    }

    /**
     * Returns the basename of a JMS object.
     * <p>
     * The basename is the final part of the JMS object's URI.
     * </p>
     * 
     * @return the basename of a JMS object
     */
    protected String getBaseName() {
        return "";
    }

    /**
     * Returns the extra information about a JMS object.
     * <p>
     * The extra information is intended to be a small amount of text, suitable
     * for showing inline with the session object's URI
     * </p>
     * 
     * @return a string containing the extra information
     */
    protected String getExtraInformation() {
        return "";
    }

    /**
     * @return an OrderedTypedValueCodec instance.
     */
    protected OrderedTypedValueCodec getCodec() {
        return codec;
    }

    protected int getIndex() {
        return index;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private int index;
    private final OrderedTypedValueCodec codec;
    private static final HJBStrings STRINGS = new HJBStrings();
}
