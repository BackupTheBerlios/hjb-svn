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

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;

import org.jmock.Mock;

import hjb.http.HJBConstants;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.CreateSession;

public class CreateSessionGeneratorTest extends BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        assertFalse(generator.matches("/"));
        assertFalse(generator.matches("//"));
        assertFalse(generator.matches("///"));
        assertFalse(generator.matches("/foo/"));
        assertFalse(generator.matches("/foo/bar/connection-1/sesssion0"));
        assertTrue(generator.matches("/foo/bar/connection-1/create"));
        assertTrue(generator.matches("/foo/baz/multiple/slashes/connection-5/create"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.expects(atLeastOnce())
            .method("getPathInfo")
            .will(returnValue("/testProvider/testFactory/with/slash/connection-0/create"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Session(root, "testProvider", "testFactory/with/slash");

        CreateSessionGenerator generator = new CreateSessionGenerator();
        generator.generateCommand(testRequest, root);
        assertSame(root.getCommandRunner(),
                   generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof CreateSession);
        assertEquals("hjb/testProvider/testFactory/with/slash/connection-0/session-{0}",
                     generator.getCreatedLocationFormatter().toPattern());

    }

    protected Map generateMockParameterMap() {
        Map parameterMap = new HashMap();
        parameterMap.put(HJBConstants.SESSION_ACKNOWLEDGEMENT_MODE,
                         new Integer(Session.CLIENT_ACKNOWLEDGE));
        parameterMap.put(HJBConstants.SESSION_TRANSACTED,
                         new Boolean(false));
        return Collections.unmodifiableMap(parameterMap);
    }

    protected void setUp() throws Exception {
        super.setUp();
        generator = new CreateSessionGenerator();
    }
}
