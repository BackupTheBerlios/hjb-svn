package hjb.jms.info;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import hjb.jms.HJBProvider;
import hjb.misc.HJBStrings;

/**
 * <code>ProviderListing</code> is used to generate a text list of the
 * connection factories and destinations that are registered with HJB for a
 * particular provider.
 * 
 * <p>
 * Each factory or destination is entered on a separate line, with the
 * destinations being listed first.
 * </p>
 * <p>
 * If a recursive listing is required, each connection factory appends its own
 * recursive <code>ConnectionFactoryListing</code> to the output.
 * </p>
 * 
 * @author Tim Emiola
 */
public class ProviderListing implements JMSObjectListing {

    public ProviderListing(HJBProvider theProvider) {
        if (null == theProvider) {
            throw new IllegalArgumentException(strings().needsANonNull(HJBProvider.class));
        }
        this.theProvider = theProvider;
    }

    /**
     * Returns text corresponding to the path portion of the URIs of the
     * destinations and connection factories registered with the provider.
     * 
     * <p>
     * The destinations are listed first.
     * </p>
     * 
     * @param prefix
     *            the prefix to use in constructing the paths
     * @return text containing destination and connection factory URIs
     */
    public String getListing(String prefix) {
        return getListing(prefix, false);
    }

    /**
     * Returns text corresponding to the path portion of the URIs of the
     * destinations and connection factories registered with the provider.
     * 
     * <p>
     * The destinations are listed first.
     * </p>
     * 
     * @param prefix
     *            the prefix to use in constructing the paths
     * @param recurse
     *            <code>true</code> indicates that a recursive listing is
     *            required.
     * @return text containing destination and connection factory URIs
     */
    public String getListing(String prefix, boolean recurse) {
        StringWriter sw = new StringWriter();
        writeListing(sw, prefix, recurse);
        return sw.toString();
    }

    /**
     * Writes the text corresponding to the path portion of the URIs of the
     * destinations and connection factories registered with the provider.
     * 
     * <p>
     * The destinations are listed first.
     * </p>
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
        writeDestinations(pw, prefixEndingInSlash);
        writeConnectionFactories(pw, prefixEndingInSlash);
        if (getTheProvider().getConnectionFactories().size() > 0 && recurse) {
            writeConnectionFactoryListings(pw, prefixEndingInSlash);
        }
    }

    protected void writeConnectionFactoryListings(PrintWriter aWriter,
                                                  String prefixEndingInSlash) {
        Map connectionFactories = getTheProvider().getConnectionFactories();
        for (Iterator i = connectionFactories.keySet().iterator(); i.hasNext();) {
            String factoryName = (String) i.next();
            aWriter.println();
            aWriter.println(prefixEndingInSlash + factoryName);
            listingFor(factoryName).writeListing(aWriter,
                                                 prefixEndingInSlash + factoryName + "/",
                                                 true);
        }
    }

    protected JMSObjectListing listingFor(String factoryName) {
        return new ConnectionFactoryListing(getTheProvider().getConnectionFactory(factoryName));
    }

    protected void writeConnectionFactories(PrintWriter aWriter,
                                            String prefixEndingInSlash) {
        Map connectionFactories = getTheProvider().getConnectionFactories();
        for (Iterator i = connectionFactories.keySet().iterator(); i.hasNext();) {
            String factoryName = (String) i.next();
            aWriter.print(prefixEndingInSlash + factoryName);
            aWriter.println();
        }
    }

    protected void writeDestinations(PrintWriter aWriter,
                                     String prefixEndingInSlash) {
        Map destinations = getTheProvider().getDestinations();
        for (Iterator i = destinations.keySet().iterator(); i.hasNext();) {
            String destinationName = (String) i.next();
            aWriter.println(prefixEndingInSlash + destinationName);
        }
    }

    protected HJBProvider getTheProvider() {
        return theProvider;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private HJBProvider theProvider;
    private static final HJBStrings STRINGS = new HJBStrings();
}
