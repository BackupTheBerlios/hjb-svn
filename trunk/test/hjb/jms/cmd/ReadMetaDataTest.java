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
package hjb.jms.cmd;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.HJBConnection;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.testsupport.MockHJBRuntime;

public class ReadMetaDataTest extends MockObjectTestCase {

    public void testReadMetaDataThrowsOnNullConnection() {
        try {
            new ReadMetaData(null);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public ConnectionMetaData makeMockMetaData() {
        Mock mockMetaData = new Mock(ConnectionMetaData.class);
        mockMetaData.stubs().method("getJMSMajorVersion").will(returnValue(1));
        mockMetaData.stubs().method("getJMSMinorVersion").will(returnValue(2));
        mockMetaData.stubs().method("getJMSVersion").will(returnValue("1.2"));
        mockMetaData.stubs()
            .method("getJMSProviderName")
            .will(returnValue("HJB Test"));
        mockMetaData.stubs()
            .method("getProviderMajorVersion")
            .will(returnValue(0));
        mockMetaData.stubs()
            .method("getProviderMinorVersion")
            .will(returnValue(7));
        mockMetaData.stubs()
            .method("getProviderVersion")
            .will(returnValue("0.7"));
        mockMetaData.stubs()
            .method("getJMSXPropertyNames")
            .will(returnValue(Collections.enumeration(new ArrayList())));
        return (ConnectionMetaData) mockMetaData.proxy();
    }

    public void testExecuteStartsAConnection() {
        HJBRoot root = new HJBRoot(testRootPath);
        Mock connectionMock = mock(Connection.class);
        Connection testConnection = (Connection) connectionMock.proxy();
        connectionMock.expects(once())
            .method("getMetaData")
            .will(returnValue(makeMockMetaData()));
        connectionMock.stubs().method("setExceptionListener");

        mockHJB.make1Connection(root,
                                testConnection,
                                "testProvider",
                                "testFactory");
        HJBConnection testHJBConnection = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0);
        ReadMetaData command = new ReadMetaData(testHJBConnection);
        command.execute();
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNotNull(command.getMetaDataAsText());
        System.err.println(command.getMetaDataAsText());
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    protected File testRootPath;
    protected MockHJBRuntime mockHJB;
}
