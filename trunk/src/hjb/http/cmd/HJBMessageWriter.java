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

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.HJBMessage;

/**
 * <code>HJBMessageWriter</code> provides methods that convert one or many
 * <code>HJBMessage</code>s to text.
 * 
 * <p />
 * A single HJBMessage is represented as follows which <code>&lt;CR&gt;</code>
 * indicates the platform-specific end-of-line character sequence<br />
 * All the headers are written first on separate lines, next a '%' is written by
 * itself, and finally the message body is written.
 * 
 * <pre>
 *   header1=headerValue1&lt;CR&gt;
 *   header2=headerValue2&lt;CR&gt;
 *   header3=headerValue3&lt;CR&gt;
 *   ...
 *   headerN=headerValueN&lt;CR&gt;
 *   %&lt;CR&gt;
 *   ... insert message body here ...
 * </pre>
 * 
 * <p />
 * When multiple messages are written at once, each message is written as above,
 * and with a '%%' is used to separate each one:
 * 
 * <pre>
 *  message1
 *   %%&lt;CR&gt;
 *  message2
 *   %%&lt;CR&gt;
 *  ....
 *  messageN
 *   %%&lt;CR&gt;
 * </pre>
 * 
 * @author Tim Emiola
 */
public class HJBMessageWriter {

    /**
     * Returns the map of values represented by <code>mapAsText</code>.
     * 
     * <p/> <code>mapAsText</code> is assumed to have been created in a manner
     * similar to that used by {@link #asText(Map)}.
     * 
     * @param mapAsText
     *            a textual representation of a <code>Map</code>
     * @return a <code>Map</code>
     * @throws HJBException
     *             if a line occurs that is not in the correct format, or if any
     *             other problem occurs
     */
    public Map asMap(String mapAsText) throws HJBException {
        try {
            LineNumberReader lines = new LineNumberReader(new StringReader(mapAsText));
            Map result = new HashMap();
            for (String line = lines.readLine(); null != line; line = lines.readLine()) {
                if ("".equals(line.trim())) continue;
                int equalsPosition = line.indexOf('=');
                if (-1 == equalsPosition) {
                    handleBadMapData(lines.getLineNumber(), mapAsText);
                }
                result.put(line.substring(0, equalsPosition),
                           line.substring(equalsPosition + 1).trim());
            }
            return result;
        } catch (IOException e) {
            String errorMessage = strings().getString(HJBStrings.COULD_NOT_GET_MAP_FROM_TEXT);
            LOG.error(errorMessage, e);
            throw new HJBException(errorMessage, e);
        }
    }

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
     * Creates a text string representing the contents of <code>anyMap</code>
     * 
     * @param anyMap
     *            a <code>Map</code>
     * @return the textual representation <code>anyMap</code>
     */
    public String asText(Map anyMap) {
        if (null == anyMap) return "";
        StringWriter result = new StringWriter();
        writeAsText(anyMap, result);
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
        writeAsText(aMessage.getHeaders(), aWriter);
        PrintWriter pw = new PrintWriter(aWriter);
        pw.println();
        pw.println(HEADER_BODY_SEPARATOR);
        pw.println(aMessage.getBody());
    }

    /**
     * Writes the content of <code>anyMap</code> to <code>aWriter</code>.
     * 
     * @param anyMap
     *            a <code>Map</code>
     * @param aWriter
     *            a <code>Writer</code>
     */
    public void writeAsText(Map anyMap, Writer aWriter) {
        if (null == anyMap) return;
        PrintWriter pw = new PrintWriter(aWriter);
        for (Iterator i = anyMap.keySet().iterator(); i.hasNext();) {
            Object key = i.next();
            pw.print(strings().getString(HJBStrings.NAME_AND_VALUE,
                                         key,
                                         anyMap.get(key)));
            if (i.hasNext()) {
                pw.println();
            }
        }
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
            if (i + 1 != someMessages.length) {
                pw.println();
                pw.println(MESSAGE_SEPARATOR);
            }
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

    protected void handleBadMapData(int i, String line) throws HJBException {
        String message = strings().getString(HJBStrings.INCORRECT_FORMAT_IN_MAP_DATA,
                                             new Integer(i),
                                             line);
        LOG.error(message);
        throw new HJBException(message);
    }

    public static final String HEADER_BODY_SEPARATOR = "%";
    public static final String MESSAGE_SEPARATOR = "%%";
    private static final HJBStrings STRINGS = new HJBStrings();
    private static final Logger LOG = Logger.getLogger(HJBMessageWriter.class);
}
