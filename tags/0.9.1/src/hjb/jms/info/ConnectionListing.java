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

import hjb.jms.HJBConnection;
import hjb.jms.HJBSession;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

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
        String totalLine = strings().getString(HJBStrings.LISTING_TOTAL,
                                               new Integer(getListingTotal()));
        String prefixEndingWithConnection = prefixEndingInSlash
                + PathNaming.CONNECTION + '-'
                + getTheConnection().getConnectionIndex();
        pw.println(prefixEndingWithConnection + ':');
        pw.println(totalLine);
        writeSessions(pw);
        if (getTheConnection().getActiveSessions().size() > 0 && recurse) {
            writeSessionListings(pw, prefixEndingWithConnection + "/");
        }
    }

    public int getListingTotal() {
        return getTheConnection().getActiveSessions().size();
    }

    protected void writeSessionListings(PrintWriter aWriter,
                                        String prefixEndingInSlash) {
        Map activeSessions = getTheConnection().getActiveSessions();
        if (activeSessions.size() > 0) {
            aWriter.println();
        }
        for (Iterator i = activeSessions.keySet().iterator(); i.hasNext();) {
            Integer sessionIndex = (Integer) i.next();
            aWriter.println();
            HJBSession theSession = getTheConnection().getSession(sessionIndex.intValue());
            new SessionListing(theSession).writeListing(aWriter,
                                                      prefixEndingInSlash,
                                                      true);
        }
    }

    protected void writeSessions(PrintWriter aWriter) {
        Map activeSessions = getTheConnection().getActiveSessions();
        for (Iterator i = activeSessions.keySet().iterator(); i.hasNext();) {
            Integer sessionIndex = (Integer) i.next();
            JMSObjectDescription d = getTheConnection().getSessionDescription(sessionIndex.intValue());
            aWriter.print(d.longDescription());
            if (i.hasNext()) {
                aWriter.println();
            }
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
