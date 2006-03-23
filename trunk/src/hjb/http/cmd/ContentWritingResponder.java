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

import hjb.http.HJBServletConstants;
import hjb.jms.cmd.JMSCommand;
import hjb.misc.HJBException;

/**
 * <code>ContentWritingResponder</code> is a <code>JMSCommandResponder</code>
 * used to send content
 * 
 * @author Tim Emiola
 */
public class ContentWritingResponder extends BaseJMSCommandResponder {

    public ContentWritingResponder(JMSCommand executedCommand,
                                   String contentToWrite) {
        super(executedCommand);
        if (null == contentToWrite) {
            throw new IllegalArgumentException(strings().needsANonNull("Content"));
        }
        this.contentToWrite = contentToWrite;
    }

    public void sendResponse(HttpServletResponse response) throws HJBException,
            IOException {
        sendResponseWithContent(response);
    }

    /**
     * Sends <code>response</code> when this <code>JMSCommandGenerator</code>
     * generates a <code>JMSCommand</code> that creates content.
     * 
     * @param response
     *            a <code>HttpServletResponse</code>
     * @throws IOException
     *             if a problem occurs while sending <code>response</code>
     */
    protected void sendResponseWithContent(HttpServletResponse response)
            throws IOException {
        if (getExecutedCommand().isExecutedOK()) {
            addStatusHeader(response);
            sendOkWithContent(response);
            response.setContentType(HJBServletConstants.HTTP_TEXT_PLAIN);
            writeContentTo(response);
            response.flushBuffer();
        } else {
            sendHJBFault(response);
        }
    }

    /**
     * Used to write the content.
     * 
     * @param response
     *            a <code>HttpServletResponse</code>
     * @throws IOException
     *             if a problem occurs while sending <code>response</code>
     */
    protected void writeContentTo(HttpServletResponse response)
            throws IOException {
        response.getWriter().write(getContentToWrite());
    }

    protected void sendOkWithContent(HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected String getContentToWrite() {
        return contentToWrite;
    }

    private String contentToWrite;
}
