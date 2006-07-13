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

import hjb.http.HJBConstants;
import hjb.http.RuntimeExceptionHandler;
import hjb.jms.cmd.JMSCommand;
import hjb.misc.HJBStrings;

/**
 * <code>BaseJMSCommandResponder</code> is the base class for all
 * <code>JMSCommandResponder</code> implementations in this package.
 * 
 * @author Tim Emiola
 */
public abstract class BaseJMSCommandResponder implements JMSCommandResponder {

    public BaseJMSCommandResponder(JMSCommand executedCommand) {
        if (null == executedCommand) {
            throw new IllegalArgumentException(strings().needsANonNull(JMSCommand.class));
        }
        this.executedCommand = executedCommand;
    }

    /**
     * Retrieves the status message of the generated command.
     * 
     * @return the status message of the generated command
     */
    protected String getStatusText() {
        return getExecutedCommand().getStatusMessage();
    }

    /**
     * Used by the various methods that write the response to add a custom HJB
     * header containing the status message resulting from processing the
     * command.
     * <p>
     * The name of header is the value of
     * {@link HJBConstants#HJB_STATUS_HEADER}.
     * </p>
     * 
     * @param response
     *            a <code>HttpServletResponse</code>
     */
    protected void addStatusHeader(HttpServletResponse response) {
        response.addHeader(HJBConstants.HJB_STATUS_HEADER,
                           getStatusText());
    }

    /**
     * Used by the various methods that write the response to signal that a
     * problem has occurred.
     * 
     * @param response
     *            a <code>HttpServletResponse</code>
     */
    protected void sendHJBFault(HttpServletResponse response)
            throws IOException {
        RuntimeExceptionHandler h = new RuntimeExceptionHandler();
        h.sendHJBFault(response,
                       getExecutedCommand().getFault(),
                       getStatusText());
    }

    protected JMSCommand getExecutedCommand() {
        return executedCommand;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private JMSCommand executedCommand;
    private static HJBStrings STRINGS = new HJBStrings();
}
