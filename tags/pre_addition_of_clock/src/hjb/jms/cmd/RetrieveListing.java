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
