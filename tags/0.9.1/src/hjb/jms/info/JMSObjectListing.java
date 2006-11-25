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

import java.io.Writer;

/**
 * <code>JMSObjectListing</code> defines methods used to obtain text listings
 * from JMS objects.
 * 
 * @author Tim Emiola
 */
public interface JMSObjectListing {

    /**
     * Returns text containing the HJB URIs of the child JMS objects created by or
     * registered with the target JMS object.
     * 
     * @param prefix
     *            the prefix to use in constructing the URIs
     * @return text containing connection URIs
     */
    public String getListing(String prefix);

    /**
     * Returns text containing the HJB URIs of the child JMS objects created by or
     * registered with the target JMS object.
     * 
     * @param prefix
     *            the prefix to use in constructing the URIs
     * @param recurse
     *            <code>true</code> indicates that a recursive listing is
     *            required.
     * @return text containing JMS Object URIs
     */
    public String getListing(String prefix, boolean recurse);

    /**
     * Writes the text containing the HJB URIs of the JMS objects created by or
     * registered with the target JMS object to <code>aWriter</code>.
     * 
     * @param aWriter
     *            the Writer to which the listing text is written
     * @param prefix
     *            the prefix to use in constructing the URIs
     * @param recurse
     *            <code>true</code> indicates that a recursive listing is
     *            required.
     */
    public void writeListing(Writer aWriter, String prefix, boolean recurse);
    
    /**
     * Gets the total number of items contained in the listing.
     * @return the total number of items contained in the listing.
     */
    public int getListingTotal();
}