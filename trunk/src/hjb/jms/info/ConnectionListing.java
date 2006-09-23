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
import java.util.Iterator;
import java.util.Map;

import hjb.jms.HJBConnection;
import hjb.misc.HJBStrings;

/**
 * <code>ConnectionListing</code> is used to generate a text list of the
 * sessions created by a given <code>HJBConnection</code>.
 * 
 * <p>
 * If a recursive listing is required, each session appends its own recursive
 * <code>SessionListing</code> to the output.
 * </p>
 * 
 * @author Tim Emiola
 */
public class ConnectionListing implements JMSObjectListing {

    public ConnectionListing(HJBConnection theConnection) {
        if (null == theConnection) {
            throw new IllegalArgumentException(strings().needsANonNull(HJBConnection.class));
        }
        this.theConnection = theConnection;
    }

    /**
     * Returns text corresponding to the path portion of the URIs of the
     * sessions created by the <code>HJBConnection</code>.
     * 
     * @param prefix
     *            the prefix to use in constructing the paths
     * @return text containing session URIs
     */
    public String getListing(String prefix) {
        return getListing(prefix, false);
    }

    /**
     * Returns text corresponding to the path portion of the URIs of the
     * sessions created by the <code>HJBConnection</code>.
     * 
     * @param prefix
     *            the prefix to use in constructing the paths
     * @param recurse
     *            <code>true</code> indicates that a recursive listing is
     *            required.
     * @return text containing session URIs
     */
    public String getListing(String prefix, boolean recurse) {
        StringWriter sw = new StringWriter();
        writeListing(sw, prefix, recurse);
        return sw.toString();
    }

    /**
     * Writes the text corresponding to the path portion of the URIs of the
     * sessions created by the <code>HJBConnection</code>.
     * 
     * @param aWriter
     *            the Writer to which the listing is written
     * @param prefix
     *            the prefix to use in constructing the paths
     * @param recurse
     *            <code>true</code> indicates that a recursive listing is
     *            required.
     */
    public void writeListing(Writer aWriter, String prefix, boolean recurse) {
        PrintWriter pw = new PrintWriter(aWriter);
        String prefixEndingInSlash = prefix.endsWith("/") ? prefix : prefix
                + "/";
        writeSessions(pw, prefixEndingInSlash);
        if (getTheConnection().getActiveSessions().size() > 0 && recurse) {
            writeSessionListings(pw, prefixEndingInSlash);
        }
    }

    protected void writeSessionListings(PrintWriter aWriter,
                                        String prefixEndingInSlash) {
        Map activeSessions = getTheConnection().getActiveSessions();
        for (Iterator i = activeSessions.keySet().iterator(); i.hasNext();) {
            Integer sessionIndex = (Integer) i.next();
            SessionDescription sd = getTheConnection().getSessionDescription(sessionIndex.intValue());
            aWriter.println();
            aWriter.println(prefixEndingInSlash + sd.longDescription());
            new SessionListing(getTheConnection(), sessionIndex.intValue()).writeListing(aWriter,
                                                                                         prefixEndingInSlash,
                                                                                         true);
        }
    }

    protected void writeSessions(PrintWriter aWriter, String prefixEndingInSlash) {
        Map activeSessions = getTheConnection().getActiveSessions();
        for (Iterator i = activeSessions.keySet().iterator(); i.hasNext();) {
            Integer sessionIndex = (Integer) i.next();
            SessionDescription sd = getTheConnection().getSessionDescription(sessionIndex.intValue());
            aWriter.print(prefixEndingInSlash + sd);
            aWriter.println();
        }
    }

    protected HJBConnection getTheConnection() {
        return theConnection;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private HJBConnection theConnection;
    private static final HJBStrings STRINGS = new HJBStrings();
}
