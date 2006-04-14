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
import hjb.jms.cmd.CommitSession;

public class CommitSessionGeneratorTest extends BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        JMSCommandGenerator generator = new CommitSessionGenerator();
        assertFalse(generator.matches("/"));
        assertFalse(generator.matches("//"));
        assertFalse(generator.matches("///"));
        assertFalse(generator.matches("/foo/"));
        assertFalse(generator.matches("/foo/bar/connection-1/sesssion0"));
        assertTrue(generator.matches("/foo/bar/connection-1/session-1/commit"));
        assertTrue(generator.matches("/foo/bar/connection-5/session-50/commit"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.stubs()
            .method("getPathInfo")
            .will(returnValue("/testProvider/testFactory/connection-0/session-0/commit"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Session(root, "testProvider", "testFactory");

        JMSCommandGenerator generator = new CommitSessionGenerator();
        generator.generateCommand(testRequest, root);
        assertSame(root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0)
            .getSessionCommandRunner(0), generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof CommitSession);
    }

}
