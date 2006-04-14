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

import hjb.http.HJBServletConstants;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.SendHJBMessage;

public class SendHJBMessageGeneratorTest extends
        BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        JMSCommandGenerator generator = new SendHJBMessageGenerator();
        assertFalse(generator.matches("/"));
        assertFalse(generator.matches("//"));
        assertFalse(generator.matches("///"));
        assertFalse(generator.matches("/foo/"));
        assertFalse(generator.matches("/foo/bar/connection-1/session-0/createconsumer"));
        assertTrue(generator.matches("/foo/bar/connection-1/session-0/producer-0/send"));
        assertTrue(generator.matches("/foo/baz/connection-5/session-4/producer-5/send"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.stubs()
            .method("getPathInfo")
            .will(returnValue("/testProvider/testFactory/connection-0/session-0/producer-0/send"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1SessionAnd1Destination(root,
                                            "testProvider",
                                            "testFactory",
                                            "testDestination",
                                            createMockDestination());

        JMSCommandGenerator generator = new SendHJBMessageGenerator();
        generator.generateCommand(testRequest, root);
        assertSame(root.getProvider("testProvider")
            .getConnectionFactory("testFactory")
            .getConnection(0)
            .getSessionCommandRunner(0), generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof SendHJBMessage);
    }

    protected Map generateMockParameterMap() {
        Map parameterMap = new HashMap();
        parameterMap.put(HJBServletConstants.MESSAGE_TO_SEND, new String[] {
            "Dummy message"
        });
        return Collections.unmodifiableMap(parameterMap);
    }

}
