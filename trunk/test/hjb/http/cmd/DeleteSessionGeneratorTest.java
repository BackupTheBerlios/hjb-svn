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
import hjb.jms.cmd.DeleteSession;

public class DeleteSessionGeneratorTest extends BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        JMSCommandGenerator generator = new DeleteSessionGenerator();
        assertFalse(generator.matches("/"));
        assertFalse(generator.matches("//"));
        assertFalse(generator.matches("///"));
        assertFalse(generator.matches("/foo/"));
        assertFalse(generator.matches("/foo/bar/connection-1/sesssion0"));
        assertTrue(generator.matches("/foo/bar/connection-1/session-1"));
        assertTrue(generator.matches("/foo/bar/connection-5/session-50/"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.expects(atLeastOnce()).method("getPathInfo").will(returnValue("/testProvider/testFactory/connection-0/session-0"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Session(root, "testProvider", "testFactory");

        JMSCommandGenerator generator = new DeleteSessionGenerator();
        generator.generateCommand(testRequest, root);
        assertSame(root.getCommandRunner(),
                   generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof DeleteSession);
        assertTrue("" + generator.getGeneratedCommand().getDescription()
                           + " should contain 0",
                   -1 != generator.getGeneratedCommand().getDescription().indexOf("0"));
    }

}
