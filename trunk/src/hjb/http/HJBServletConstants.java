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
package hjb.http;

import javax.jms.Session;

/**
 * <code>HJBServletConstants</code> contains constants used during processing
 * of <code>HttpServletRequests</code> by the <code>HJBServlet</code>.
 * 
 * @author Tim Emiola
 */
public final class HJBServletConstants {

    /**
     * Constant that holds the name of the servlet parameter used to configure
     * HJBRoot's storage path.
     * <p />
     * The value of this constant is "hjb.config.rootpath".
     */
    public static final String ROOT_PATH_CONFIG = "hjb.config.rootpath";

    /**
     * Constant that holds the name of the attribute used to store HJBRoot in
     * the servlet context.
     * <p />
     * The value of this constant is "hjb.runtime.root".
     */
    public static final String HJB_ROOT_ATTRIBUTE = "hjb.runtime.root";

    /**
     * Constant that holds the name of the attribute used to store the Command
     * timeout timer in the servlet context.
     * <p />
     * The value of this constant is "hjb.runtime.root".
     */
    public static final String COMMAND_TIMEOUT_ATTRIBUTE = "hjb.runtime.timeout.timer";

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
     * identify a subscriber name for use in configuration of a JMS
     * <code>DurableSubscriber</code>
     * <p />
     * The value of this constant is "subscriber-name".
     */
    public static final String SUBSCRIBER_NAME = "subscriber-name";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify a subscriber name for use in configuration of a JMS
     * <code>DurableSubscriber</code>
     * <p />
     * The value of this constant is "acknowledgement-mode".
     */
    public static final String SESSION_ACKNOWLEDGEMENT_MODE = "acknowledgement-mode";

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
     * identify the no-local flag for use in configuration of various JMS
     * <code>MessageConsumers</code>
     * <p />
     * The value of this constant is "no-local".
     */
    public static final String CONSUMER_NOLOCAL = "no-local";

    /**
     * Constant that holds the name of the request parameter that HJB uses to
     * identify the timeout flag for use in configuration of JMS
     * <code>MessageProducers</code>
     * <p />
     * The value of this constant is "timeout".
     */
    public static final String TIMEOUT = "timeout";

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
