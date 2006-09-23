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
package hjb.http;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBClientException;
import hjb.misc.HJBConstants;
import hjb.misc.HJBNotFoundException;

public class RuntimeExceptionHandlerTest extends MockObjectTestCase {

    public void testDoesNotThrowOnNullResponse() throws IOException {
        RuntimeExceptionHandler handler = new RuntimeExceptionHandler();
        handler.sendHJBFault(null, new RuntimeException());
    }

    public void testSendsErrorResponseContainingTheStatusText()
            throws IOException {
        Mock mockResponse = mock(HttpServletResponse.class);
        String statusText = "test status Text";

        mockResponse.expects(once())
            .method("addHeader")
            .with(eq(HJBConstants.HJB_STATUS_HEADER), eq(statusText));
        mockResponse.expects(once())
            .method("addHeader")
            .with(eq(HJBConstants.HTTP_CACHE_CONTROL),
                  eq(HJBConstants.HJB_DEFAULT_CACHE_CONTROL));

        mockResponse.expects(once()).method("sendError").with(ANYTHING,
                                                              eq(statusText));
        RuntimeExceptionHandler handler = new RuntimeExceptionHandler();
        handler.sendHJBFault((HttpServletResponse) mockResponse.proxy(),
                             new RuntimeException(),
                             statusText);
    }

    public void testSendsNotFoundCodeOnHJBNotFound() throws IOException {
        Mock mockResponse = mock(HttpServletResponse.class);
        String statusText = "test status Text";

        mockResponse.stubs().method("addHeader").with(ANYTHING, ANYTHING);
        mockResponse.expects(once())
            .method("sendError")
            .with(eq(HttpServletResponse.SC_NOT_FOUND), ANYTHING);
        RuntimeExceptionHandler handler = new RuntimeExceptionHandler();
        handler.sendHJBFault((HttpServletResponse) mockResponse.proxy(),
                             new HJBNotFoundException(),
                             statusText);
    }

    public void testSendsBadRequestCodeOnHJBClient() throws IOException {
        Mock mockResponse = mock(HttpServletResponse.class);
        String statusText = "test status Text";

        mockResponse.stubs().method("addHeader").with(ANYTHING, ANYTHING);
        mockResponse.expects(once())
            .method("sendError")
            .with(eq(HttpServletResponse.SC_BAD_REQUEST), ANYTHING);
        RuntimeExceptionHandler handler = new RuntimeExceptionHandler();
        handler.sendHJBFault((HttpServletResponse) mockResponse.proxy(),
                             new HJBClientException(),
                             statusText);
    }

    public void testSendsServerErrorCodeOnRuntimeException() throws IOException {
        Mock mockResponse = mock(HttpServletResponse.class);
        String statusText = "test status Text";

        mockResponse.stubs().method("addHeader").with(ANYTHING, ANYTHING);
        mockResponse.expects(once())
            .method("sendError")
            .with(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), ANYTHING);
        RuntimeExceptionHandler handler = new RuntimeExceptionHandler();
        handler.sendHJBFault((HttpServletResponse) mockResponse.proxy(),
                             new RuntimeException(),
                             statusText);
    }

    public void testUsesExceptionMessageWhenStatusIsProvided()
            throws IOException {
        Mock mockResponse = mock(HttpServletResponse.class);
        String statusText = "sent as a test";
        mockResponse.stubs().method("addHeader").with(ANYTHING, eq(statusText));
        mockResponse.expects(once())
            .method("addHeader")
            .with(eq(HJBConstants.HTTP_CACHE_CONTROL),
                  eq(HJBConstants.HJB_DEFAULT_CACHE_CONTROL));

        mockResponse.expects(once()).method("sendError").with(ANYTHING,
                                                              eq(statusText));
        RuntimeExceptionHandler handler = new RuntimeExceptionHandler();

        handler.sendHJBFault((HttpServletResponse) mockResponse.proxy(),
                             new RuntimeException(statusText));
    }
}