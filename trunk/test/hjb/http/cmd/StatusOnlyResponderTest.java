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

import javax.servlet.http.HttpServletResponse;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.cmd.JMSCommand;
import hjb.misc.HJBConstants;
import hjb.misc.HJBException;

public class StatusOnlyResponderTest extends MockObjectTestCase {

    public void testStatusOnlyResponderThrowsOnNullJMSCommand() {
        try {
            new StatusOnlyResponder(null);
            fail("Should have thrown and exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testSendResponseHandlesFaultsCorrectly() throws Exception {
        Mock mockJMSCommand = mock(JMSCommand.class);
        mockJMSCommand.stubs().method("isExecutedOK").will(returnValue(false));
        mockJMSCommand.stubs()
            .method("getStatusMessage")
            .will(returnValue("test Status"));
        mockJMSCommand.expects(once())
            .method("getFault")
            .will(returnValue(new HJBException("thrown as a Test")));

        JMSCommand testCommand = (JMSCommand) mockJMSCommand.proxy();

        Mock mockResponse = mock(HttpServletResponse.class);
        mockResponse.expects(once())
            .method("addHeader")
            .with(eq(HJBConstants.HJB_STATUS_HEADER), eq("test Status"));
        mockResponse.expects(once())
            .method("sendError")
            .with(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
                  eq("test Status"));
        HttpServletResponse testResponse = (HttpServletResponse) mockResponse.proxy();

        JMSCommandResponder responder = new StatusOnlyResponder(testCommand);
        responder.sendResponse(testResponse);
    }

    public void testSendResponseHandlesSuccessCorrectly() throws Exception {
        Mock mockJMSCommand = mock(JMSCommand.class);
        mockJMSCommand.stubs().method("isExecutedOK").will(returnValue(true));
        mockJMSCommand.stubs()
            .method("getStatusMessage")
            .will(returnValue("test status"));

        JMSCommand testCommand = (JMSCommand) mockJMSCommand.proxy();

        Mock mockResponse = mock(HttpServletResponse.class);
        mockResponse.expects(once())
            .method("addHeader")
            .with(eq(HJBConstants.HJB_STATUS_HEADER), eq("test status"));
        mockResponse.expects(once()).method("flushBuffer");
        mockResponse.expects(once()).method("setContentLength").with(eq(0));
        mockResponse.expects(once())
            .method("setStatus")
            .with(eq(HttpServletResponse.SC_NO_CONTENT));
        HttpServletResponse testResponse = (HttpServletResponse) mockResponse.proxy();

        JMSCommandResponder responder = new StatusOnlyResponder(testCommand);
        responder.sendResponse(testResponse);
    }

}
