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

import hjb.jms.HJBRoot;
import hjb.jms.cmd.RetrieveListing;
import hjb.misc.HJBConstants;

public class ListProviderGeneratorTest extends
        BaseJMSCommandGeneratorTestCase {

    public void testMatchWorksCorrectly() {
        assertFalse(generator.matches("/foo/destination/list"));
        assertFalse(generator.matches("/foo/bar/baz/list"));
        assertTrue(generator.matches("/foo/list"));
        assertTrue(generator.matches("/foo/list"));
    }

    public void testJMSCommandAndItsRunnerAreGeneratedCorrectly() {
        Mock mockRequest = generateMockRequest();
        mockRequest.expects(atLeastOnce())
            .method("getPathInfo")
            .will(returnValue("/testProvider/list"));
        HttpServletRequest testRequest = (HttpServletRequest) mockRequest.proxy();

        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Session(root, "testProvider", "testFactory/with/slash");

        generator.generateCommand(testRequest, root);
        assertSame(root.getCommandRunner(),
                   generator.getAssignedCommandRunner());
        assertTrue(generator.getGeneratedCommand() instanceof RetrieveListing);
        RetrieveListing listingCommand = (RetrieveListing) generator.getGeneratedCommand();
        assertContains(listingCommand.getDescription(), "testProvider");
    }

    protected Map generateMockParameterMap() {
        Map parameterMap = new HashMap();
        parameterMap.put(HJBConstants.LISTING_RECURSE, new Boolean(false));
        return Collections.unmodifiableMap(parameterMap);
    }

    protected void setUp() throws Exception {
        super.setUp();
        generator = new ListProviderGenerator();
    }
}
