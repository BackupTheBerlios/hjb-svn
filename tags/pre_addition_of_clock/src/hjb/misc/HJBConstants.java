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
package hjb.misc;

import javax.jms.Session;

/**
 * <code>HJBConstants</code> contains constants throughout the
 * <code>HJB</code>.
 * 
 * @author Tim Emiola
 */
public final class HJBConstants {

    /**
     * Constant that holds the name of the attribute used to store HJB
     * Application in the servlet context.
     * <p />
     * The value of this constant is "hjb.runtime.application".
     */
    public static final String HJB_APPLICATION_ATTRIBUTE = "hjb.runtime.application";

    /**
     * Constant that holds the name of the servlet parameter used to configure
     * HJBRoot's storage path.
     * <p />
     * The value of this constant is "hjb.config.rootpath".
     */
    public static final String ROOT_PATH_CONFIG = "hjb.config.rootpath";

    /**
     * Constant that holds the HTTP response header used to send a command's
     * status in the response.
     * <p />
     * The value of this constant is "hjb.command.status".
     */
    public static final String HJB_STATUS_HEADER = "hjb.command.status";

    /**
     * Constant that holds the standard HTTP response header for specifying a
     * location.
     * <p />
     * The value of this constant is "Location".
     */
    public static final String HTTP_LOCATION = "Location";

    /**
     * Constant that holds the standard HTTP response header for specifying
     * cache control parameters.
     * <p />
     * The value of this constant is "Cache-Control".
     */
    public static final String HTTP_CACHE_CONTROL = "Cache-Control";

    /**
     * Constant that holds the default HJB cache control response header value.
     * <p />
     * The value of this constant is "no-store".
     */
    public static final String HJB_DEFAULT_CACHE_CONTROL = "no-store";


    /**
     * Constant that holds the standard MIME type for plain text
     * <p />
     * The value of this constant is "text/plain".
     */
    public static final String HTTP_TEXT_PLAIN = "text/plain";

    /**
     * Constant that holds the name of used to describe java's UTF-8 charset
     * <p />
     * The value of this constant is "UTF-8".
     */
    public static final String JAVA_CHARSET_UTF8 = "UTF-8";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify a username for use in configuration of a JMS
     * <code>Connection</code>
     * <p />
     * The value of this constant is "username".
     */
    public static final String CONNECTION_USERNAME = "username";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify a password for use in configuration of a JMS
     * <code>Connection</code>
     * <p />
     * The value of this constant is "password".
     */
    public static final String CONNECTION_PASSWORD = "password";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * indicate that listing should be recursive.
     * <p />
     * The value of this constant is "recurse".
     */
    public static final String LISTING_RECURSE = "recurse";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify a subscriber name for use in configuration of a JMS
     * <code>DurableSubscriber</code>
     * <p />
     * The value of this constant is "subscriber-name".
     */
    public static final String SUBSCRIBER_NAME = "subscriber-name";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * determine the acknowledgement mode for use in configuration of a JMS
     * <code>Session</code>
     * <p />
     * The value of this constant is "acknowledgement-mode".
     */
    public static final String SESSION_ACKNOWLEDGEMENT_MODE = "acknowledgement-mode";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * determine if message Ids should be disabled for use in configuration of a
     * JMS <code>MessageProducer</code>
     * <p />
     * The value of this constant is "disable-message-ids".
     */
    public static final String DISABLE_MESSAGE_IDS = "disable-message-ids";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * determine if timestamps should be disabled for use in configuration of a
     * JMS <code>MessageProducer</code>
     * <p />
     * The value of this constant is "acknowledgement-mode".
     */
    public static final String DISABLE_TIMESTAMPS = "disable-timestamps";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * assign the time to live for use in configuration of a JMS
     * <code>MessageProducer</code>
     * <p />
     * The value of this constant is "time-to-live".
     */
    public static final String TIME_TO_LIVE = "time-to-live";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * assign the delivery mode for use in configuration of a JMS
     * <code>MessageProducer</code>
     * <p />
     * The value of this constant is "delivery-mode".
     */
    public static final String DELIVERY_MODE = "delivery-mode";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * assign the priority for use in configuration of a JMS
     * <code>MessageProducer</code>
     * <p />
     * The value of this constant is "priority".
     */
    public static final String PRIORITY = "priority";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify a transacted flag for use in configuration of a JMS
     * <code>Session</code>
     * <p />
     * The value of this constant is "transacted".
     */
    public static final String SESSION_TRANSACTED = "transacted";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify a destination, topic or queue for use in configuration of
     * various JMS components
     * <p />
     * The value of this constant is "destination-url".
     */
    public static final String DESTINATION_URL = "destination-url";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify the message selector expression for use in configuration of
     * various JMS components
     * <p />
     * The value of this constant is "message-selector".
     */
    public static final String MESSAGE_SELECTOR = "message-selector";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify the encoded JMS message to send.
     * <p />
     * The value of this constant is "message-to-send".
     */
    public static final String MESSAGE_TO_SEND = "message-to-send";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify the no-local flag for use in the configuration of JMS
     * <code>MessageConsumers</code>
     * <p />
     * The value of this constant is "no-local".
     */
    public static final String CONSUMER_NOLOCAL = "no-local";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify the timeout period in milliseconds for use in the configuration
     * of JMS <code>MessageConsumers</code>
     * <p />
     * The value of this constant is "timeout".
     */
    public static final String TIMEOUT = "timeout";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify a clientId for use in some JMS API calls.
     * <p />
     * The value of this constant is "clientId".
     */
    public static final String CLIENT_ID = "clientId";

    /**
     * Constant that holds the default value of the transacted flag for use in
     * configuration of JMS <code>Sessions</code>
     * <p />
     * The value of this constant is <code>false</code>.
     */
    public static final boolean DEFAULT_TRANSACTED = false;

    /**
     * Constant that holds the default value of the transacted flag for use in
     * configuration of JMS <code>Sessions</code>
     * <p />
     * The value of this constant is {@link Session#AUTO_ACKNOWLEDGE}.
     */
    public static final int DEFAULT_ACKNOWLEDGEMENT_MODE = Session.AUTO_ACKNOWLEDGE;

    /**
     * Constant that holds the default value of the no-local flag for use in
     * configuration of JMS <code>Sessions</code>
     * <p />
     * The value of this constant is <code>false</code>.
     */
    public static final boolean DEFAULT_NOLOCAL = false;

    /**
     * Constant that holds the value of the content-length when no content is
     * sent
     * <p />
     * The value of this constant is <code>0</code>.
     */
    public static final int NO_CONTENT_LENGTH = 0;

    /**
     * Constant that holds the name of the attribute and config parameter used
     * to set the command timeout (in seconds)
     * <p />
     * The value of this constant is "hjb.config.command.timeout".
     */
    public static final String COMMAND_TIMEOUT_CONFIG = "hjb.config.command.timeout";
}
