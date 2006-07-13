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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.servlet.http.HttpServletRequest;

import org.jmock.Mock;

import hjb.http.HJBConstants;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.CreateBrowser;

public class CreateBrowserGeneratorTest extends BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        assertFalse(generator.matches("/"));
        assertFalse(generator.matches("//"));
        assertFalse(generator.matches("///"));
        assertFalse(generator.matches("/foo/"));
        assertFalse(generator.matches("/foo/bar/connection-1/session-0/createconsumer"));
        assertTrue(generator.matches("/foo/bar/connection-1/session-0/create-browser"));
        assertTrue(generator.matches("/foo/baz/multiple/slashes/connection-5/session-4/create-browser"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.stubs()
            .method("getPathInfo")
            .will(returnValue("/testProvider/testFactory/slash/connection-0/session-0/create-browser"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1SessionAnd1Destination(root,
                                            "testProvider",
                                            "testFactory/slash",
                                            "testDestination/with/slashes",
                                            createMockDestination());

        CreateBrowserGenerator generator = new CreateBrowserGenerator();
        generator.generateCommand(testRequest, root);
        assertSame(root.getProvider("testProvider")
            .getConnectionFactory("testFactory/slash")
            .getConnection(0)
            .getSessionCommandRunner(0), generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof CreateBrowser);
        assertEquals("hjb/testProvider/testFactory/slash/connection-0/session-0/browser-{0}",
                     generator.getCreatedLocationFormatter().toPattern());
    }

    protected Map generateMockParameterMap() {
        Map parameterMap = new HashMap();
        parameterMap.put(HJBConstants.MESSAGE_SELECTOR, new String[] {
            "*"
        });
        parameterMap.put(HJBConstants.DESTINATION_URL, new String[] {
            "/context/servlet/testProvider/destination/testDestination/with/slashes"
        });
        return Collections.unmodifiableMap(parameterMap);
    }

    protected Destination createMockDestination() {
        Mock mockDestination = mock(Queue.class);
        return (Queue) mockDestination.proxy();
    }

    protected void setUp() throws Exception {
        super.setUp();
        generator = new CreateBrowserGenerator();
    }

}
