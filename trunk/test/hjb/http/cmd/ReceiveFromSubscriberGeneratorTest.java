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
package hjb.http.cmd;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Mock;

import hjb.jms.HJBRoot;
import hjb.jms.cmd.ReceiveFromSubscriber;

public class ReceiveFromSubscriberGeneratorTest extends
        BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        JMSCommandGenerator generator = new ReceiveFromSubscriberGenerator();
        assertFalse(generator.matches("/"));
        assertFalse(generator.matches("//"));
        assertFalse(generator.matches("///"));
        assertFalse(generator.matches("/foo/"));
        assertFalse(generator.matches("/foo/bar/connection-1/session-0/createconsumer"));
        assertTrue(generator.matches("/foo/bar/connection-1/session-0/subscriber-0/receive"));
        assertTrue(generator.matches("/foo/baz/connection-5/session-4/subscriber-5/receive"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.stubs().method("getPathInfo").will(returnValue("/testProvider/testFactory/connection-0/session-0/subscriber-0/receive"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1SessionAnd1Destination(root,
                                            "testProvider",
                                            "testFactory",
                                            "testDestination",
                                            createMockDestination());

        JMSCommandGenerator generator = new ReceiveFromSubscriberGenerator();
        generator.generateCommand(testRequest, root);
        assertSame(root.getProvider("testProvider").getConnectionFactory("testFactory").getConnection(0).getSessionCommandRunner(0),
                   generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof ReceiveFromSubscriber);
    }
}
