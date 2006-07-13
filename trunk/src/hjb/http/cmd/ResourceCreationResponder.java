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

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import hjb.jms.cmd.JMSCommand;
import hjb.misc.HJBConstants;
import hjb.misc.HJBException;

/**
 * <code>StatusOnlyResponseSender</code> is a <code>JMSCommandResponder</code>
 * {@link #sendResponse(HttpServletResponse)} sends no content, just a status
 * code and status text.
 * 
 * @author Tim Emiola
 */
public class ResourceCreationResponder extends BaseJMSCommandResponder {

    public ResourceCreationResponder(JMSCommand executedCommand,
                                     String createdLocation) {
        super(executedCommand);
        if (null == createdLocation) {
            throw new IllegalArgumentException(strings().needsANonNull("Location"));
        }
        this.createdLocation = createdLocation;
    }

    public void sendResponse(HttpServletResponse response) throws HJBException,
            IOException {
        sendCreatedResponse(response);
    }

    /**
     * Sends <code>response</code> when this <code>JMSCommandGenerator</code>
     * generates a <code>JMSCommand</code> that creates a JMS resource.
     * <p>
     * Subclasses may use this method when they override
     * {@link #sendResponse(HttpServletResponse)} if they do so, they
     * <em>must</em> also override
     * {@link #addLocationHeaderTo(HttpServletResponse)}
     * </p>
     * 
     * @param response
     *            a <code>HttpServletResponse</code>
     * @throws IOException
     *             if a problem occurs while sending <code>response</code>
     */
    protected void sendCreatedResponse(HttpServletResponse response)
            throws IOException {
        if (getExecutedCommand().isExecutedOK()) {
            addStatusHeader(response);
            addLocationHeaderTo(response);
            sendCreatedOK(response);
        } else {
            sendHJBFault(response);
        }
    }

    /**
     * Used by {@link #sendCreatedResponse(HttpServletResponse)} to add the
     * location header to the response.
     * 
     * @param response
     *            a <code>HttpServletResponse</code>
     */
    protected void addLocationHeaderTo(HttpServletResponse response) {
        response.addHeader(HJBConstants.HTTP_LOCATION,
                           getCreatedLocation());
    }

    protected void sendCreatedOK(HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentLength(HJBConstants.NO_CONTENT_LENGTH);
        response.flushBuffer();
    }

    protected String getCreatedLocation() {
        return createdLocation;
    }

    private String createdLocation;
}
