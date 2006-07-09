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

import org.apache.log4j.Logger;

import hjb.misc.HJBClientException;
import hjb.misc.HJBStrings;
import hjb.misc.HJBNotFoundException;

/**
 * <code>RuntimeExceptionHandler</code> is used to handle any runtime
 * exceptions that occur during the processing of a request by the
 * <code>HJBServlet</code>
 * 
 * @author Tim Emiola
 */
public class RuntimeExceptionHandler {

    /**
     * Sends the response corresponding to <code>exception</code>, including
     * <code>statusText</code> as a hjb specific header.
     * 
     * <p />
     * The method sends a different response code depending the type of
     * <code>exception</code> If exception is {@link HJBNotFoundException} the
     * response code is {@link HttpServletResponse#SC_NOT_FOUND}, If it is
     * {@link HJBClientException} the response code is
     * {@link HttpServletResponse#SC_BAD_REQUEST} Otherwise it is
     * {@link HttpServletResponse#SC_INTERNAL_SERVER_ERROR}.
     * 
     * @param response
     *            a <code>HttpServletResponse</code>
     * @param exception
     *            a <code>RuntimeException</code>
     * @param statusText
     *            a status message
     * @throws IOException
     *             if a problem occurs while sending the response
     */
    public void sendHJBFault(HttpServletResponse response,
                             RuntimeException exception,
                             String statusText) throws IOException {
        if (null == response) {
            String message = strings().getString(HJBStrings.RESPONSE_IS_AWOL,
                                                 statusText);
            LOG.error(message, exception);
            return;
        }
        response.addHeader(HJBServletConstants.HJB_STATUS_HEADER,
                           (null != statusText) ? statusText
                                   : strings().getString(HJBStrings.UNKNOWN_CAUSE));
        if (exception instanceof HJBNotFoundException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, statusText);
        } else if (exception instanceof HJBClientException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, statusText);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                               (null != statusText) ? statusText
                                       : strings().getString(HJBStrings.UNKNOWN_CAUSE));
        }
    }

    /**
     * Sends the response corresponding to <code>exception</code>, including
     * <code>statusText</code> as a hjb specific header.
     * 
     * <p />
     * Like {@link #sendHJBFault(HttpServletResponse, RuntimeException, String)}
     * except that status text is taken from the <code>RuntimeException</code>'s
     * message.
     * 
     * @param response
     *            a <code>HttpServletResponse</code>
     * @param exception
     *            a <code>RuntimeException</code>
     * @throws IOException
     *             if a problem occurs while sending the response
     */
    public void sendHJBFault(HttpServletResponse response,
                             RuntimeException exception) throws IOException {
        sendHJBFault(response, exception, exception.getMessage());
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private static final HJBStrings STRINGS = new HJBStrings();
    private static final Logger LOG = Logger.getLogger(RuntimeExceptionHandler.class);
}
