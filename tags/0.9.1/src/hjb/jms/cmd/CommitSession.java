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

import hjb.jms.HJBSession;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

public class CommitSession extends SessionCommand {

    public CommitSession(HJBSession theSession) {
        super(theSession);
    }

    public void execute() {
        assertNotCompleted();
        try {
            getTheSession().commit();
        } catch (RuntimeException e) {
            recordFault(e);
        } catch (JMSException e) {
            recordFault(new HJBException(e));
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_SESSION_COMMIT,
                                   getTheSession().getDescription());
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_SESSION_COMMIT,
                                       getTheSession().getDescription());
        } else {
            return getFault().getMessage();
        }
    }
}
