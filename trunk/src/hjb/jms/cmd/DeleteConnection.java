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

import hjb.jms.HJBConnectionFactory;
import hjb.misc.HJBStrings;

public class DeleteConnection extends ConnectionFactoryCommand {

    public DeleteConnection(HJBConnectionFactory theFactory, int connectionIndex) {
        super(theFactory);
        setConnectionIndex(connectionIndex);
        updateSuccessMessage(connectionIndex);
        updateDescription(connectionIndex);
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
        return description;
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return getSuccessMessage();
        } else {
            return getFault().getMessage();
        }
    }

    protected int getConnectionIndex() {
        return connectionIndex;
    }

    protected void setConnectionIndex(int connectionIndex) {
        this.connectionIndex = connectionIndex;
    }
    
    protected String getSuccessMessage() {
        return successMessage;
    }
    
    protected void updateDescription(int connectionIndex) {
        description = strings().getString(HJBStrings.SIMPLE_DELETE_DESCRIPTION,
                                           getTheFactory().getConnection(connectionIndex));
    }
     
    protected void updateSuccessMessage(int connectionIndex) {
        successMessage = strings().getString(HJBStrings.SIMPLE_DELETE_SUCCESS_MESSAGE,
                                             getTheFactory().getConnection(connectionIndex));
    }
    
    private String successMessage;
    private String description;
    private int connectionIndex;
}
