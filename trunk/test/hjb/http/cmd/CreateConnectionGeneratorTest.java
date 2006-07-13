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

import javax.servlet.http.HttpServletRequest;

import org.jmock.Mock;

import hjb.http.HJBConstants;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.CreateConnection;

public class CreateConnectionGeneratorTest extends
        BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        assertFalse(generator.matches("/foo/"));
        assertFalse(generator.matches("/foo/bar/not-end-with-slash-create"));
        assertFalse(generator.matches("/foo/bar/looks/like/a/session/connection-134/create"));
        assertTrue(generator.matches("/foo/bar/create"));
        assertTrue(generator.matches("/foo/baz/multiple/slashes/create"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.expects(atLeastOnce())
            .method("getPathInfo")
            .will(returnValue("/testProvider/testFactory/slash/create"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Session(root, "testProvider", "testFactory/slash");

        CreateConnectionGenerator generator = new CreateConnectionGenerator();
        generator.generateCommand(testRequest, root);
        assertSame(root.getCommandRunner(),
                   generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof CreateConnection);
        assertEquals("hjb/testProvider/testFactory/slash/connection-{0}",
                     generator.getCreatedLocationFormatter().toPattern());

    }

    protected Map generateMockParameterMap() {
        Map parameterMap = new HashMap();
        parameterMap.put(HJBConstants.CONNECTION_USERNAME,
                         "testUsername");
        parameterMap.put(HJBConstants.CONNECTION_PASSWORD,
                         "testPassword");
        return Collections.unmodifiableMap(parameterMap);
    }

    protected void setUp() throws Exception {
        super.setUp();
        generator = new CreateConnectionGenerator();
    }
}
