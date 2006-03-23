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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;

import hjb.misc.HJBStrings;
import hjb.msg.HJBMessage;

/**
 * <code>HJBMessageWriter</code> provides methods that convert one or many
 * <code>HJBMessage</code>s to text.
 * 
 * <p />
 * A single HJBMessage is represented as follows:<br />
 * All the headers are written as follows
 * 
 * <pre>
 *     headerName=headerValue 'CR'
 * </pre>
 * 
 * Then a '%' is written by itself:
 * 
 * <pre>
 *      % 'CR'
 * </pre>
 * 
 * Finally, the message body is written.
 * <p />
 * When multiple messages are written at once, each message is written as above,
 * and a '%% ' is used to separate each one.
 * 
 * <pre>
 *      % 'CR'
 * </pre>
 * 
 * @author Tim Emiola
 */
public class HJBMessageWriter {

    /**
     * Creates a text string representing the contents of
     * <code>HJBMessage</code>
     * 
     * @param aMessage
     *            a <code>HJBMessage</code>
     * @return the textual representation <code>message</code>
     */
    public String asText(HJBMessage aMessage) {
        if (null == aMessage) return "";
        StringWriter result = new StringWriter();
        writeAsText(aMessage, result);
        return result.toString();
    }

    /**
     * Writes the content of <code>HJBMessage</code> to <code>aWriter</code>.
     * 
     * @param aMessage
     *            a <code>HJBMessage</code>
     * @param aWriter
     *            a <code>Writer</code>
     */
    public void writeAsText(HJBMessage aMessage, Writer aWriter) {
        if (null == aMessage) return;
        PrintWriter pw = new PrintWriter(aWriter);
        for (Iterator i = aMessage.getHeaders().keySet().iterator(); i.hasNext();) {
            String headerName = (String) i.next();
            pw.println(strings().getString(HJBStrings.NAME_AND_VALUE,
                                           headerName,
                                           aMessage.getHeader(headerName)));
        }
        pw.println(HEADER_BODY_SEPARATOR);
        pw.println(aMessage.getBody());
    }

    /**
     * Returns <code>someMessages</code> as text.
     * 
     * @param someMessages
     *            an array of <code>HJBMessage</code>s
     * @return the textual representaton of <code>someMessages</code>
     */
    public String asText(HJBMessage[] someMessages) {
        StringWriter result = new StringWriter();
        writeAsText(someMessages, result);
        return result.toString();
    }

    /**
     * Writes the contents of <code>someMessages</code> to a
     * <code>Writer</code>
     * 
     * @param someMessages
     *            an array of <code>HJBMessage</code>s
     * @param aWriter
     *            a <code>Writer</code>
     */
    public void writeAsText(HJBMessage[] someMessages, Writer aWriter) {
        if (null == someMessages) return;
        PrintWriter pw = new PrintWriter(aWriter);
        for (int i = 0; i < someMessages.length; i++) {
            writeAsText(someMessages[i], aWriter);
            pw.println(MESSAGE_SEPARATOR);
        }
    }

    /**
     * Overrides {@link Object#equals(java.lang.Object)} to ensure all
     * <code>HJBMessageWriter</code> instances are equivalent.
     */
    public boolean equals(Object o) {
        return (o instanceof HJBMessageWriter);
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

    public String toString() {
        String clazzName = this.getClass().getName();
        if (-1 == clazzName.lastIndexOf('.')) return "[" + clazzName + "]";
        return "[" + clazzName.substring(clazzName.lastIndexOf('.') + 1) + "]";
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    public static String HEADER_BODY_SEPARATOR = "%";
    public static String MESSAGE_SEPARATOR = "%%";
    private static final HJBStrings STRINGS = new HJBStrings();
}
