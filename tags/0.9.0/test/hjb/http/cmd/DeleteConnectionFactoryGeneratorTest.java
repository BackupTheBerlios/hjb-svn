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
import hjb.jms.cmd.DeleteConnectionFactory;

public class DeleteConnectionFactoryGeneratorTest extends
        BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        assertFalse(generator.matches("///"));
        assertFalse(generator.matches("/foo/destination/like/a/destination"));
        assertFalse(generator.matches("/foo/bar/like/a/connection/connection-524"));
        assertTrue(generator.matches("/foo/bar/baz"));
        assertTrue(generator.matches("/foo/bar/"));
        assertTrue(generator.matches("/foo/bar/multiple/slashes/"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.expects(atLeastOnce())
            .method("getPathInfo")
            .will(returnValue("/testProvider/testFactory/with/slash"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath, defaultTestClock());
        mockHJB.make1Session(root, "testProvider", "testFactory/with/slash");

        JMSCommandGenerator generator = new DeleteConnectionFactoryGenerator();
        generator.generateCommand(testRequest, root);
        assertSame(root.getCommandRunner(),
                   generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof DeleteConnectionFactory);
        assertTrue("" + generator.getGeneratedCommand().getDescription()
                           + " should contain testFactory/with/slash",
                   -1 != generator.getGeneratedCommand()
                       .getDescription()
                       .indexOf("testFactory/with/slash"));
    }

    protected void setUp() throws Exception {
        super.setUp();
        generator = new DeleteConnectionFactoryGenerator();
    }
}
