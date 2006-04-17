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

import javax.jms.Session;

import hjb.jms.HJBConnection;
import hjb.misc.HJBStrings;

public class CreateSession extends ConnectionCommand {

    public CreateSession(HJBConnection theConnection,
                         int acknowledgementMode,
                         boolean transacted) {
        super(theConnection);
        this.acknowledgementMode = acknowledgementMode;
        this.transacted = transacted;
        this.sessionIndex = UNSET_SESSION_INDEX;
    }

    public void execute() {
        assertNotCompleted();
        try {
            setSessionIndex(getTheConnection().createSessionAndReturnItsIndex(isTransacted(),
                                                                              getAcknowledgementMode()));
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_CREATE_COMMANDS,
                                   Session.class.getName(),
                                   new Integer(getTheConnection().getConnectionIndex()));
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_CREATE_COMMANDS,
                                       Session.class.getName(),
                                       strings().getString(HJBStrings.NOT_APPLICAPLE));
        } else {
            return getFault().getMessage();
        }
    }

    public int getSessionIndex() {
        return sessionIndex;
    }

    public boolean isSessionIndexSet() {
        return UNSET_SESSION_INDEX == getSessionIndex();
    }

    protected void setSessionIndex(int sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    protected int getAcknowledgementMode() {
        return acknowledgementMode;
    }

    protected boolean isTransacted() {
        return transacted;
    }

    private int sessionIndex;
    private int acknowledgementMode;
    private boolean transacted;
    public static final int UNSET_SESSION_INDEX = Integer.MIN_VALUE;
}
