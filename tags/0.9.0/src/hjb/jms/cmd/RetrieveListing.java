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
package hjb.jms.cmd;

import hjb.jms.info.JMSObjectListing;
import hjb.misc.HJBStrings;

public class RetrieveListing extends BaseJMSCommand {

    public RetrieveListing(JMSObjectListing listing,
                           String prefix,
                           String rawDescription,
                           boolean recursive) {
        validateArguments(listing, prefix, rawDescription);
        this.listing = listing;
        this.rawDescription = rawDescription;
        this.prefix = (null == prefix) ? "" : prefix;
        this.recursive = recursive;
    }

    public RetrieveListing(JMSObjectListing listing,
                           String prefix,
                           boolean recursive) {
        this(listing, prefix, prefix, recursive);
    }

    public void execute() {
        assertNotCompleted();
        try {
            setTheListing(listing.getListing(getPrefix(), isRecursive()));
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_LISTING,
                                   getRawDescription());
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_LISTING,
                                       getRawDescription());
        } else {
            return getFault().getMessage();
        }
    }

    public String getTheOutput() {
        return theOutput;
    }

    protected void validateArguments(JMSObjectListing listing,
                                     String prefix,
                                     String rawDescription)
            throws IllegalArgumentException {
        if (null == listing) {
            throw new IllegalArgumentException(strings().needsANonNull(JMSObjectListing.class));
        }
        if (null == rawDescription) {
            throw new IllegalArgumentException(strings().needsANonNull("'description'"));
        }
        if (null == prefix) {
            throw new IllegalArgumentException(strings().needsANonNull("'prefix'"));
        }
    }

    protected void setTheListing(String theListing) {
        this.theOutput = theListing;
    }

    protected String getPrefix() {
        return prefix;
    }

    protected boolean isRecursive() {
        return recursive;
    }

    protected JMSObjectListing getListing() {
        return listing;
    }

    protected String getRawDescription() {
        return rawDescription;
    }

    private final String prefix;
    private String theOutput;
    private final JMSObjectListing listing;
    private final String rawDescription;
    private final boolean recursive;
}
