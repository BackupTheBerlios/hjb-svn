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
package hjb.jms;

import java.io.*;
import java.util.Date;
import java.util.Random;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import hjb.misc.HJBException;
import hjb.misc.HJBExceptionListenerConfiguration;
import hjb.misc.HJBStrings;

/**
 * <code>HJBExceptionListener</code> logs exception using a <code>Log4j</code>
 * Logger.
 * 
 * @author Tim Emiola
 */
public class HJBExceptionListener implements ExceptionListener {

    public HJBExceptionListener(HJBConnection connection) {
        this.creationTime = connection.getCreationTime();
        this.connectionIndex = new Integer(connection.getConnectionIndex());
        this.randomPart = new Integer(Math.abs(RANDOM.nextInt()));
        this.config = new HJBExceptionListenerConfiguration();
        this.connectionLogger = Logger.getLogger(CONNECTION_LOG_PREFIX
                + HJBExceptionListener.class.getName());
        logIsConstructedOK();
    }

    protected void logIsConstructedOK() {
        LOG.info(strings().getString(HJBStrings.CONNECTION_ERRORS_LOGGED,
                                     getConnectionIndex(),
                                     getUniqueFilePath()));
    }

    public synchronized String getErrorLog() throws HJBException {
        try {
            StringWriter sw = new StringWriter();
            FileReader fw = new FileReader(getUniqueFilePath());
            int nextChar = fw.read();
            while (-1 != nextChar) {
                sw.write(nextChar);
                nextChar = fw.read();
            }
            String result = sw.toString();
            if (0 == result.length()) {
                result = strings().getString(HJBStrings.NO_ERRORS_WRITTEN,
                                             getConnectionIndex());
            }
            return result;
        } catch (FileNotFoundException e) {
            return strings().getString(HJBStrings.NO_ERRORS_WRITTEN,
                                       getConnectionIndex());
        } catch (IOException e) {
            String message = strings().getString(HJBStrings.COULD_NOT_READ_EXCEPTION_LOG,
                                                 getUniqueFilePath());
            LOG.error(message, e);
            throw new HJBException(message, e);
        }
    }

    protected void addUniqueLogAppender() {
        FileAppender a = new FileAppender();
        a.setFile(getUniqueFilePath());
        a.setLayout(new PatternLayout(config.getConnectionLog4jPattern()));
        a.activateOptions();
        getConnectionLogger().addAppender(a);
    }

    public synchronized void onException(JMSException e) {
        if (!errorHasBeenReceived) {
            // the appender is lazily initialized, so there will
            // be no file if no error's occur
            addUniqueLogAppender();
            errorHasBeenReceived = true;
        }
        getConnectionLogger().error(e.getMessage(), e);
    }

    protected Integer getRandomPart() {
        return randomPart;
    }

    protected Integer getConnectionIndex() {
        return connectionIndex;
    }

    protected Date getCreationTime() {
        return creationTime;
    }

    protected String getUniqueFilePath() {
        return new File(getLogDirectory(), getUniqueFileName()).getAbsolutePath();
    }

    protected String getLogDirectory() {
        return config.getConnectionLogDirectory();
    }

    protected String getUniqueFileName() {
        return strings().getString(HJBStrings.EXCEPTION_LOGFILE_NAME,
                                   getConnectionIndex(),
                                   new Long(getCreationTime().getTime()),
                                   getRandomPart());
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected Logger getConnectionLogger() {
        return connectionLogger;
    }

    private boolean errorHasBeenReceived;
    private final Integer randomPart;
    private final Integer connectionIndex;
    private final Date creationTime;
    private final Logger connectionLogger;
    private final HJBExceptionListenerConfiguration config;
    private static final String CONNECTION_LOG_PREFIX = "connection.";
    private static final Random RANDOM = new Random();
    private static final HJBStrings STRINGS = new HJBStrings();
    private static final Logger LOG = Logger.getLogger(HJBExceptionListener.class);
    public static final String DEFAULT_LOG4J_PATTERN_LAYOUT = "%d [%t] %p %m%n";
}
