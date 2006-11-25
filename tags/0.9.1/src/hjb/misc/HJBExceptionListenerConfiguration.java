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

import java.io.File;

/**
 * <code>HJBExceptionListenerConfiguration</code> is used to configure the
 * system wide options affecting the HJBExceptionListener log files.
 * 
 * @author Tim Emiola
 */
public class HJBExceptionListenerConfiguration {

    /**
     * Returns the location of the connection log.
     * 
     * It defaults to the value of <code>user.home</code> system property, but
     * if {@link #CONNECTION_LOG_DIR_PROPERTY_NAME} is set and its value is
     * valid (it exists, is a directory and is writable), it uses that.
     * 
     * @return the bufferSize.
     */
    public String getConnectionLogDirectory() {
        String cwd = System.getProperty("user.home");
        String result = System.getProperty(CONNECTION_LOG_DIR_PROPERTY_NAME,
                                           cwd);
        if ((!result.equals(cwd)) && (new File(result).isDirectory())
                && (new File(result).canWrite())) {
            return result;
        }
        return cwd;
    }

    /**
     * Returns the location of the directory for storing the connection logs.
     * 
     * @return the bufferSize.
     */
    public String getConnectionLog4jPattern() {
        return System.getProperty(CONNECTION_LOG_LOG4J_PATTERN,
                                  DEFAULT_LOG4J_PATTERN_LAYOUT);
    }

    /**
     * System property name that can be used to specify the directory in which
     * to save the HJB connection log files.
     * <p />
     * The value of this constant is 'hjb.runtime.connection-log-dir'.
     */
    public static final String CONNECTION_LOG_DIR_PROPERTY_NAME = "hjb.runtime.connection-log-dir";

    /**
     * System property name that can be used to specify the log4j pattern to use
     * to be used in the connection error logs.
     * <p />
     * The value of this constant is 'hjb.runtime.connection-log-lo4j-pattern'.
     */
    public static final String CONNECTION_LOG_LOG4J_PATTERN = "hjb.runtime.connection-log-log4j-pattern";

    /**
     * Constant that contains the default value of the log4j pattern used in the
     * correction error logs.
     * <p />
     * The value of this constant is '%d [%t] %p %m%n'.
     */
    public static final String DEFAULT_LOG4J_PATTERN_LAYOUT = "%d [%t] %p %m%n";
}
