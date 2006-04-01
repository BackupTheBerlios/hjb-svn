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

import hjb.misc.HJBException;

/**
 * <code>JMSResponseSender</code> declares methods used so send the response
 * upon execution of a <code>JMSCommand</code>.
 * 
 * @author Tim Emiola
 */
public interface JMSCommandResponder {

    /**
     * Sends a response based on the results of executing the generated
     * <code>JMSCommand</code>.
     * 
     * @param response
     *            the <code>HttpServletResponse</code>
     * @throws HJBException
     *             if a problem occurs getting information from the executed
     *             <code>JMSCommand</code>
     * @throws IOException
     *             if a problem occurs while writing the response
     */
    public void sendResponse(HttpServletResponse response) throws HJBException,
            IOException;

}
