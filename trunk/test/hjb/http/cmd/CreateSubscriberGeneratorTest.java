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
import javax.jms.Topic;
import javax.servlet.http.HttpServletRequest;

import org.jmock.Mock;

import hjb.http.HJBServletConstants;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.CreateSubscriber;

public class CreateSubscriberGeneratorTest extends
        BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        JMSCommandGenerator generator = new CreateSubscriberGenerator();
        assertFalse(generator.matches("/"));
        assertFalse(generator.matches("//"));
        assertFalse(generator.matches("///"));
        assertFalse(generator.matches("/foo/"));
        assertFalse(generator.matches("/foo/bar/connection-1/session-0/createconsumer"));
        assertTrue(generator.matches("/foo/bar/connection-1/session-0/create-durable-subscriber"));
        assertTrue(generator.matches("/foo/baz/multiple/slashes/connection-5/session-4/create-durable-subscriber"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.expects(atLeastOnce())
            .method("getPathInfo")
            .will(returnValue("/testProvider/testFactory/with/slash/connection-0/session-0/create-durable-subscriber"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1SessionAnd1Destination(root,
                                            "testProvider",
                                            "testFactory/with/slash",
                                            "testDestination/with/slashes",
                                            createMockDestination());

        CreateSubscriberGenerator generator = new CreateSubscriberGenerator();
        generator.generateCommand(testRequest, root);
        assertSame(root.getProvider("testProvider")
            .getConnectionFactory("testFactory/with/slash")
            .getConnection(0)
            .getSessionCommandRunner(0), generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof CreateSubscriber);
        assertEquals("hjb/testProvider/testFactory/with/slash/connection-0/session-0/subscriber-{0}",
                     generator.getCreatedLocationFormatter().toPattern());
    }

    protected Map generateMockParameterMap() {
        Map parameterMap = new HashMap();
        parameterMap.put(HJBServletConstants.MESSAGE_SELECTOR, new String[] {
            "*"
        });
        parameterMap.put(HJBServletConstants.CONSUMER_NOLOCAL, new String[] {
            "(boolean false)"
        });
        parameterMap.put(HJBServletConstants.SUBSCRIBER_NAME, new String[] {
            "testSubscriber"
        });
        parameterMap.put(HJBServletConstants.DESTINATION_URL, new String[] {
            "/context/servlet/testProvider/testDestination/with/slashes"
        });
        return Collections.unmodifiableMap(parameterMap);
    }

    protected Destination createMockDestination() {
        Mock mockDestination = mock(Topic.class);
        return (Topic) mockDestination.proxy();
    }
}
