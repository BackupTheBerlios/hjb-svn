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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.Connection;

import hjb.jms.HJBConnection;
import hjb.misc.HJBConstants;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

/**
 * <code>ConnectionDescription</code> is used to provide a description of JMS
 * <code>Connections</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class ConnectionDescription extends BaseJMSObjectDescription {

    public ConnectionDescription(HJBConnection theConnection) {
        super(DUMMY, HJBStrings.INVALID_CONNECTION_INDEX);
        if (null == theConnection) {
            throw new IllegalArgumentException(strings().needsANonNull(Connection.class));
        }
        setIndex(theConnection.getConnectionIndex());
        this.theConnection = theConnection;
    }

    protected Map attributesAsAMap() {
        Map result = new TreeMap();
        try {
            result.put(HJBConstants.CLIENT_ID,
                       null == getTheConnection().getClientID() ? ""
                               : getTheConnection().getClientID());
            result.putAll(getModifiedMetadataMap());
            new MetadataReaderAssistant().metaDataAsMap(getTheConnection().getMetaData());
        } catch (JMSException e) {}
        return result;
    }

    protected Map getModifiedMetadataMap() {
        Map result = new TreeMap();
        try {
            Map originalMetaData = new MetadataReaderAssistant().metaDataAsMap(getTheConnection().getMetaData());
            for (Iterator i = originalMetaData.keySet().iterator(); i.hasNext();) {
                String metaDataKey = (String) i.next();
                String shortKey = metaDataKey;
                if (metaDataKey.startsWith(MetadataReaderAssistant.METADATA_KEY_ROOT)) {
                    shortKey = metaDataKey.substring(MetadataReaderAssistant.METADATA_KEY_ROOT.length());
                }
                result.put(shortKey, originalMetaData.get(metaDataKey));
            }
        } catch (JMSException e) {}
        return result;
    }

    protected String getBaseName() {
        return PathNaming.CONNECTION + "-" + getIndex();
    }

    protected String getExtraInformation() {
        try {
            return null == getTheConnection().getClientID() ? ""
                    : strings().getString(HJBStrings.FORMAT_CLIENT_ID,
                                          getTheConnection().getClientID());
        } catch (JMSException e) {
            return "";
        }
    }

    protected Connection getTheConnection() {
        return theConnection;
    }

    private static final int DUMMY = 0;
    private Connection theConnection;
}
