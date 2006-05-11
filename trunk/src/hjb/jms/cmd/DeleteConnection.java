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

import hjb.jms.HJBConnection;
import hjb.jms.HJBConnectionFactory;
import hjb.misc.HJBStrings;

public class DeleteConnection extends ConnectionFactoryCommand {

    public DeleteConnection(HJBConnectionFactory theFactory, int connectionIndex) {
        super(theFactory);
        setConnectionIndex(connectionIndex);
    }

    public void execute() {
        assertNotCompleted();
        try {
            getTheFactory().deleteConnection(connectionIndex);
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_DELETE_COMMANDS,
                                   new Integer(getConnectionIndex()),
                                   HJBConnection.class.getName());
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_DELETE_COMMANDS,
                                       HJBConnection.class.getName(),
                                       new Integer(getConnectionIndex()));
        } else {
            return getFault().getMessage();
        }
    }

    public int getConnectionIndex() {
        return connectionIndex;
    }

    protected void setConnectionIndex(int connectionIndex) {
        this.connectionIndex = connectionIndex;
    }

    private int connectionIndex;
    public static final int UNSET_CONNECTION_INDEX = Integer.MIN_VALUE;
}
