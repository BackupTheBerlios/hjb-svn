package hjb.jms;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;

import hjb.http.cmd.PathNaming;
import hjb.misc.HJBStrings;

/**
 * <code>BrowserDescription</code> is used to provide textual description of
 * the JMS <code>QueueBrowsers</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class BrowserDescription {

    public BrowserDescription(QueueBrowser theBrowser, int browserIndex) {
        if (null == theBrowser) {
            throw new IllegalArgumentException(strings().needsANonNull(QueueBrowser.class));
        }
        if (browserIndex < 0) {
            throw new IllegalArgumentException(strings().getString(HJBStrings.INVALID_BROWSER_INDEX,
                                                                   new Integer(browserIndex)));
        }
        this.theBrowser = theBrowser;
        this.browserIndex = browserIndex;
    }

    public String toString() {
        return PathNaming.BROWSER + "-" + getBrowserIndex()
                + getExtraInformation();
    }

    protected String getExtraInformation() {
        try {
            String browserDestination = null == getTheBrowser().getQueue() ? ""
                    : "" + getTheBrowser().getQueue();
            return strings().getString(HJBStrings.BROWSER_DESCRIPTION,
                                       browserDestination);
        } catch (JMSException e) {
            return "";
        }
    }

    protected QueueBrowser getTheBrowser() {
        return theBrowser;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected int getBrowserIndex() {
        return browserIndex;
    }

    private final QueueBrowser theBrowser;
    private final int browserIndex;
    private static final HJBStrings STRINGS = new HJBStrings();
}
