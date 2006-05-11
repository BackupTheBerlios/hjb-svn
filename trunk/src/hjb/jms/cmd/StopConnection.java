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
package hjb.jms.cmd;

import javax.jms.JMSException;

import hjb.jms.HJBConnection;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

public class StopConnection extends ConnectionCommand {

    public StopConnection(HJBConnection theConnection) {
        super(theConnection);
    }

    public void execute() {
        assertNotCompleted();
        try {
            getTheConnection().stop();
        } catch (RuntimeException e) {
            recordFault(e);
        } catch (JMSException e) {
            recordFault(new HJBException(e));
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_STOP_CONNECTION);
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_STOP_CONNECTION);
        } else {
            return getFault().getMessage();
        }
    }
}
