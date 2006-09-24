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

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.jmock.Mock;

import hjb.jms.*;
import hjb.misc.HJBException;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockHJBRuntime;
import hjb.testsupport.MockSessionBuilder;

public class CreateConsumerTest extends BaseHJBTestCase {

    public void testCreateConsumerThrowsOnNullConsumersOrDestination() {
        Mock mockDestination = mock(Destination.class);
        Destination testDestination = (Destination) mockDestination.proxy();
        try {
            new CreateConsumer(null, testDestination, "testSelector");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}

        HJBRoot root = new HJBRoot(testRootPath, defaultTestClock());
        mockHJB.make1Session(root, "testProvider", "testFactory");
        HJBSession testSession = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0).getSession(0);
        HJBSessionConsumers sessionConsumers = new HJBSessionConsumers(testSession);
        try {
            new CreateConsumer(sessionConsumers, null, "testSelector");
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteReportsAFaultOnPossibleExceptions() throws Exception {
        Exception[] possibleExceptions = new Exception[] {
                new JMSException("thrown as a test"),
                new RuntimeException("fire in the server room"),
        };
        for (int i = 0; i < possibleExceptions.length; i++) {
            HJBRoot root = new HJBRoot(testRootPath, defaultTestClock());
            Mock mockSession = new MockSessionBuilder().createMockSession();
            registerToVerify(mockSession);
            mockHJB.make1Session(root,
                                 (Session) mockSession.proxy(),
                                 "testProvider",
                                 "testFactory");
            HJBSession testSession = root.getProvider("testProvider")
                .getConnectionFactory("testFactory")
                .getConnection(0).getSession(0);
            HJBSessionConsumers sessionConsumers = testSession.getConsumers();
            Mock mockDestination = mock(Destination.class);
            Destination testDestination = (Destination) mockDestination.proxy();

            mockSession.expects(once())
                .method("createConsumer")
                .will(throwException(possibleExceptions[i]));
            assertEquals(0, sessionConsumers.asArray().length);
            CreateConsumer command = new CreateConsumer(sessionConsumers,
                                                        testDestination,
                                                        "*");
            command.execute();
            assertEquals(0, sessionConsumers.asArray().length);
            assertFalse(command.isExecutedOK());
            assertTrue(command.isComplete());
            assertNotNull(command.getFault());
            assertEquals(command.getStatusMessage(), command.getFault()
                .getMessage());
        }
    }

    public void testExecuteCreatesANewConsumer() {
        HJBRoot root = new HJBRoot(testRootPath, defaultTestClock());
        mockHJB.make1Session(root, "testProvider", "testFactory");
        HJBSession testSession = root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0).getSession(0);
        HJBSessionConsumers sessionConsumers = testSession.getConsumers();
        Mock mockDestination = mock(Destination.class);
        Destination testDestination = (Destination) mockDestination.proxy();

        assertEquals(0, sessionConsumers.asArray().length);
        CreateConsumer command = new CreateConsumer(sessionConsumers,
                                                    testDestination,
                                                    "*");
        command.execute();
        assertEquals(1, sessionConsumers.asArray().length);
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
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
