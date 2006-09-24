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

import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueBrowser;
import javax.jms.TopicSubscriber;

import hjb.jms.HJBSession;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

/**
 * <code>SessionListing</code> is used to generate a text list of the JMS
 * Session objects created by a given JMS <code>Session</code>.
 * 
 * <p>
 * If a non-recursive listing is required, each JMS object is listed as a single
 * line entry. If a recursive listing is required, each JMS object's entry may
 * span multiple lines.
 * </p>
 * 
 * @author Tim Emiola
 */
public class SessionListing implements JMSObjectListing {

    public SessionListing(HJBSession theSession) {
        if (null == theSession) {
            throw new IllegalArgumentException(strings().needsANonNull(HJBSession.class));
        }
        this.theSession = theSession;
    }

    /**
     * Returns text corresponding to the path portion of the URIs of the JMS
     * objects created by the JMS <code>Session</code>.
     * 
     * @param prefix
     *            the prefix to use in constructing the paths
     * @return text containing session URIs
     */
    public String getListing(String prefix) {
        return getListing(prefix, false);
    }

    /**
     * Returns text corresponding to the path portion of the URIs of the JMS
     * objects created by the JMS <code>Session</code>.
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
     * Writes the text corresponding to the path portion of the URIs of the JMS
     * objects created by the JMS <code>Session</code>.
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
        writeJMSObjects(pw, prefixEndingInSlash, recurse);
    }

    protected void writeJMSObjects(PrintWriter aWriter,
                                   String prefixEndingInSlash,
                                   boolean recurse) {
        String sessionPrefix = prefixEndingInSlash + PathNaming.SESSION + "-"
                + getSessionIndex() + "/";
        writeConsumers(aWriter, sessionPrefix, recurse);
        writeSubscribers(aWriter, sessionPrefix, recurse);
        writeProducers(aWriter, sessionPrefix, recurse);
        writeBrowsers(aWriter, sessionPrefix, recurse);
    }

    protected void writeBrowsers(PrintWriter aWriter,
                                 String sessionPrefix,
                                 boolean recurse) {
        QueueBrowser browsers[] = getTheSession().getBrowsers().asArray();
        BaseJMSObjectDescription descriptions[] = new BaseJMSObjectDescription[browsers.length];
        for (int i = 0; i < browsers.length; i++) {
            descriptions[i] = new BrowserDescription(browsers[i], i);
        }
        writeDescriptions(descriptions, aWriter, sessionPrefix, recurse);
    }

    protected void writeProducers(PrintWriter aWriter,
                                  String sessionPrefix,
                                  boolean recurse) {
        MessageProducer producers[] = getTheSession().getProducers().asArray();
        BaseJMSObjectDescription descriptions[] = new BaseJMSObjectDescription[producers.length];
        for (int i = 0; i < producers.length; i++) {
            descriptions[i] = new ProducerDescription(producers[i], i);
        }
        writeDescriptions(descriptions, aWriter, sessionPrefix, recurse);
        if (producers.length > 0) {
            aWriter.println();
        }
    }

    protected void writeConsumers(PrintWriter aWriter,
                                  String sessionPrefix,
                                  boolean recurse) {
        MessageConsumer consumers[] = getTheSession().getConsumers().asArray();
        BaseJMSObjectDescription descriptions[] = new BaseJMSObjectDescription[consumers.length];
        for (int i = 0; i < consumers.length; i++) {
            descriptions[i] = new ConsumerDescription(consumers[i], i);
        }
        writeDescriptions(descriptions, aWriter, sessionPrefix, recurse);
        if (consumers.length > 0) {
            aWriter.println();
        }
    }

    protected void writeSubscribers(PrintWriter aWriter,
                                    String sessionPrefix,
                                    boolean recurse) {
        TopicSubscriber subscribers[] = getTheSession().getSubscribers()
            .asArray();
        BaseJMSObjectDescription descriptions[] = new BaseJMSObjectDescription[subscribers.length];
        for (int i = 0; i < subscribers.length; i++) {
            descriptions[i] = new SubscriberDescription(subscribers[i], i);
        }
        writeDescriptions(descriptions, aWriter, sessionPrefix, recurse);
        if (subscribers.length > 0) {
            aWriter.println();
        }
    }

    private HJBSession getTheSession() {
        return theSession;
    }

    protected void writeDescriptions(BaseJMSObjectDescription[] descriptions,
                                     PrintWriter aWriter,
                                     String sessionPrefix,
                                     boolean recurse) {
        for (int i = 0; i < descriptions.length; i++) {
            aWriter.print(sessionPrefix);
            if (recurse) {
                aWriter.print(descriptions[i].longDescription());
            } else {
                aWriter.print(descriptions[i]);
            }
            if (i + 1 != descriptions.length) {
                aWriter.println();
            }
        }
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected int getSessionIndex() {
        return getTheSession().getSessionIndex();
    }

    private HJBSession theSession;
    private static final HJBStrings STRINGS = new HJBStrings();
}
