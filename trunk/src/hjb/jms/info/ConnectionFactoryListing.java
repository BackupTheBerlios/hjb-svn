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
import hjb.jms.HJBConnectionFactory;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

/**
 * <code>ConnectionFactoryListing</code> is used to generate a text list of
 * the connections created by a given <code>HJBConnectionFactory</code>.
 * 
 * <p>
 * If a recursive listing is required, each connection appends its own recursive
 * <code>ConnectionListing</code> to the output.
 * </p>
 * 
 * @author Tim Emiola
 */
public class ConnectionFactoryListing implements JMSObjectListing {

    public ConnectionFactoryListing(HJBConnectionFactory theConnectionFactory) {
        if (null == theConnectionFactory) {
            throw new IllegalArgumentException(strings().needsANonNull(HJBConnectionFactory.class));
        }
        this.theConnectionFactory = theConnectionFactory;
    }

    /**
     * Returns text corresponding to the path portion of the URIs of the
     * connections created by the <code>HJBConnectionFactory</code>.
     * 
     * @param prefix
     *            the prefix to use in constructing the paths
     * @return text containing connection URIs
     */
    public String getListing(String prefix) {
        return getListing(prefix, false);
    }

    /**
     * Returns text corresponding to the path portion of the URIs of the
     * connections created by the <code>HJBConnectionFactory</code>.
     * 
     * @param prefix
     *            the prefix to use in constructing the paths
     * @param recurse
     *            <code>true</code> indicates that a recursive listing is
     *            required.
     * @return text containing connection URIs
     */
    public String getListing(String prefix, boolean recurse) {
        StringWriter sw = new StringWriter();
        writeListing(sw, prefix, recurse);
        return sw.toString();
    }

    /**
     * Writes the text corresponding to the path portion of the URIs of the
     * connections created by the <code>HJBConnectionFactory</code>.
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
        writeConnections(pw, prefixEndingInSlash);
        if (getTheConnectionFactory().getActiveConnections().size() > 0
                && recurse) {
            writeConnectionListings(pw, prefixEndingInSlash);
        }
    }

    protected void writeConnectionListings(PrintWriter aWriter,
                                           String prefixEndingInSlash) {
        Map activeConnections = getTheConnectionFactory().getActiveConnections();
        for (Iterator i = activeConnections.keySet().iterator(); i.hasNext();) {
            HJBConnection aConnection = (HJBConnection) activeConnections.get(i.next());
            aWriter.println();
            aWriter.println(prefixEndingInSlash + aConnection);
            new ConnectionListing(aConnection).writeListing(aWriter,
                                                            prefixEndingInSlash
                                                                    + PathNaming.CONNECTION
                                                                    + "-"
                                                                    + aConnection.getConnectionIndex(),
                                                            true);
        }
    }

    protected void writeConnections(PrintWriter aWriter,
                                    String prefixEndingInSlash) {
        Map activeConnections = getTheConnectionFactory().getActiveConnections();
        for (Iterator i = activeConnections.keySet().iterator(); i.hasNext();) {
            HJBConnection aConnection = (HJBConnection) activeConnections.get(i.next());
            aWriter.print(prefixEndingInSlash + aConnection);
            aWriter.println();
        }
    }

    protected HJBConnectionFactory getTheConnectionFactory() {
        return theConnectionFactory;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private HJBConnectionFactory theConnectionFactory;
    private static final HJBStrings STRINGS = new HJBStrings();
}
