package hjb.jms.info;

import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;

import hjb.http.HJBConstants;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

/**
 * <code>BrowserDescription</code> is used to provide a description of JMS
 * <code>QueueBrowsers</code> in HJB status messages and logs.
 * 
 * @author Tim Emiola
 */
public class BrowserDescription extends BaseJMSObjectDescription {

    public BrowserDescription(QueueBrowser theBrowser, int browserIndex) {
        super(browserIndex, HJBStrings.INVALID_BROWSER_INDEX);
        if (null == theBrowser) {
            throw new IllegalArgumentException(strings().needsANonNull(QueueBrowser.class));
        }
        this.theBrowser = theBrowser;
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

    protected Map attributesAsAMap() {
        Map result = new TreeMap();
        try {
            result.put(HJBConstants.MESSAGE_SELECTOR,
                       (null == getTheBrowser().getMessageSelector() ? ""
                               : getTheBrowser().getMessageSelector()));
        } catch (JMSException e) {}
        return result;
    }

    protected String getBaseName() {
        return PathNaming.BROWSER + "-" + getIndex();
    }

    protected QueueBrowser getTheBrowser() {
        return theBrowser;
    }

    private final QueueBrowser theBrowser;
}
