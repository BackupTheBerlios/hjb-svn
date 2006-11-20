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

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;

import org.jmock.Mock;

import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockHJBRuntime;

public abstract class BaseJMSCommandGeneratorTestCase extends
        BaseHJBTestCase {

    public void testGetGeneratedCommandThrowsIllegalStateExceptionIfNoCommandHasBeenGenerated() {
        assertGetGeneratedCommandThrowsIllegalStateException(generator);
    }

    protected Mock generateMockRequest() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.stubs().method("getContextPath").will(returnValue("hjb"));
        mockRequest.stubs().method("getServletPath").will(returnValue(""));
        mockRequest.stubs()
            .method("getParameterMap")
            .will(returnValue(generateMockParameterMap()));
        return mockRequest;
    }

    protected Map generateMockParameterMap() {
        Map parameterMap = new HashMap();
        return Collections.unmodifiableMap(parameterMap);
    }

    protected Destination createMockDestination() {
        Mock mockDestination = mock(Destination.class);
        Destination testDestination = (Destination) mockDestination.proxy();
        return testDestination;
    }

    protected void assertGetGeneratedCommandThrowsIllegalStateException(JMSCommandGenerator generator) {
        try {
            generator.getGeneratedCommand();
            fail("should have thrown and IllegalStateException");
        } catch (IllegalStateException e) {}
    }

    protected void setUp() throws Exception {
        super.setUp();
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    protected JMSCommandGenerator generator;
    protected File testRootPath;
    protected MockHJBRuntime mockHJB;
}
