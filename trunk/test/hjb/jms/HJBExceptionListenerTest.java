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

import java.io.File;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.jmock.Mock;

import hjb.misc.HJBStrings;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockConnectionBuilder;

public class HJBExceptionListenerTest extends BaseHJBTestCase {

    public void testHJBConnectionConstructionCreatesAHJBExceptionListener() {
        mockConnection.reset();
        mockConnection.expects(once())
            .method("setExceptionListener")
            .with(isA(HJBExceptionListener.class));
        new HJBConnection(((Connection) mockConnection.proxy()),
                          0,
                          defaultTestClock());
    }

    public void testShouldIncludeTimestampInFilename() {
        assertContains(testListener.getUniqueFileName(), ""
                + defaultTestClock().getCurrentTime().getTime());
    }

    public void testConstructionDoesNotCreateTheLogFile() {
        assertFalse(new File(testListener.getUniqueFilePath()).exists());
    }

    public void testErrorLogContainsCommentsWhenNoExceptionsAreLogged() {
        assertFalse(new File(testListener.getUniqueFilePath()).exists());
        assertEquals(strings().getString(HJBStrings.NO_ERRORS_WRITTEN,
                                         new Integer(0)),
                     testListener.getErrorLog());
    }

    public void testShouldCreateLogFileOnError() {
        File testFile = new File(testListener.getUniqueFilePath());
        JMSException testException = new JMSException("thrown as a test");
        testException.fillInStackTrace();
        testListener.onException(testException);
        assertTrue(new File(testListener.getUniqueFilePath()).exists());
        testFile.deleteOnExit();
    }

    public void testShouldWriteLogFileOnError() {
        File testFile = new File(testListener.getUniqueFilePath());
        JMSException testException = new JMSException("thrown as a test");
        testException.fillInStackTrace();
        testListener.onException(testException);
        assertContains(testListener.getErrorLog(),
                       testException.getMessage());
        testFile.deleteOnExit();
        System.err.println("Contents of " + testFile + " after a unit test");
        System.err.println(testListener.getErrorLog());
        System.err.println();
    }

    protected void setUp() throws Exception {
        super.setUp();
        connectionBuilder = new MockConnectionBuilder();
        mockConnection = connectionBuilder.createMockConnection();
        registerToVerify(mockConnection);
        testConnection = new HJBConnection(((Connection) mockConnection.proxy()),
                                           0,
                                           defaultTestClock());
        testListener = testConnection.getConnectionListener();
    }

    private Mock mockConnection;
    private HJBConnection testConnection;
    private MockConnectionBuilder connectionBuilder;
    private HJBExceptionListener testListener;
}   
