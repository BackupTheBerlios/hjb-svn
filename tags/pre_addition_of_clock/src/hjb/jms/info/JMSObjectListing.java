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

}