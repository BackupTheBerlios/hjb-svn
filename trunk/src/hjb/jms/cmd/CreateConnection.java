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

import javax.jms.JMSException;

public class CreateConnection extends ConnectionFactoryCommand {

    public CreateConnection(HJBConnectionFactory theFactory,
                            String username,
                            String password,
                            String clientId) {
        super(theFactory);
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.connectionIndex = UNSET_CONNECTION_INDEX;
    }

    public CreateConnection(HJBConnectionFactory theFactory) {
        this(theFactory, null, null, null);
    }

    public void execute() {
        assertNotCompleted();
        try {
            createConnectionAndReturnItsIndex();
        } catch (RuntimeException e) {
            recordFault(e);
        }
        completed();
    }

    protected void createConnectionAndReturnItsIndex() {
        if (null == getPassword() || null == getUsername()) {
            setConnectionIndex(getTheFactory().createHJBConnection(getClientId()));
        } else {
            setConnectionIndex(getTheFactory().createHJBConnection(getUsername(),
                                                                   getPassword(),
                                                                   getClientId()));
        }
    }

    protected boolean clientIdWasSentAndCanBeUsed() throws JMSException {
        return null != getClientId()
                && null == getTheFactory().getConnection(getConnectionIndex())
                    .getClientID();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_CREATE_COMMANDS,
                                   HJBConnection.class.getName(),
                                   strings().getString(HJBStrings.NOT_APPLICAPLE));
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_CREATE_COMMANDS,
                                       HJBConnection.class.getName(),
                                       strings().getString(HJBStrings.NOT_APPLICAPLE));
        } else {
            return getFault().getMessage();
        }
    }

    public int getConnectionIndex() {
        return connectionIndex;
    }

    public boolean isConnectionIndexSet() {
        return UNSET_CONNECTION_INDEX == getConnectionIndex();
    }

    protected void setConnectionIndex(int connectionIndex) {
        this.connectionIndex = connectionIndex;
    }

    protected String getPassword() {
        return password;
    }

    protected String getUsername() {
        return username;
    }

    protected String getClientId() {
        return clientId;
    }

    private int connectionIndex;
    private String username;
    private String password;
    private String clientId;
    public static final int UNSET_CONNECTION_INDEX = Integer.MIN_VALUE;
}
