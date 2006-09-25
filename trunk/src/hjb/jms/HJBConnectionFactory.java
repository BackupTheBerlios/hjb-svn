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
package hjb.jms;

import java.util.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

import hjb.misc.Clock;
import hjb.misc.HJBException;
import hjb.misc.HJBNotFoundException;
import hjb.misc.HJBStrings;

/**
 * HJBConnectionFactory decorates a {@link javax.jms.ConnectionFactory}, and
 * adds methods that allow it to access the connections it creates.
 * 
 * <p />
 * It provides:
 * <ul>
 * <li>a cache of the <code>Connections</code> created by the decorated
 * <code>ConnectionFactory</code> </li>
 * <li>various utility methods that to the allow the connections to operated on
 * via their creation index</li>
 * <li>a method that allows all connections to be shutdown</li>
 * </ul>
 * 
 * @author Tim Emiola
 */
public class HJBConnectionFactory implements ConnectionFactory {

    public HJBConnectionFactory(ConnectionFactory connectionFactory, Clock aClock) {
        if (null == connectionFactory) {
            throw new IllegalArgumentException(strings().needsANonNull(ConnectionFactory.class.getName()));
        }
        if (null == aClock) {
            throw new IllegalArgumentException(strings().needsANonNull(Clock.class));
        }
        this.clock = aClock;
        this.creationTime = aClock.getCurrentTime();
        this.connectionFactory = connectionFactory;
        this.activeConnections = Collections.synchronizedMap(new TreeMap());
        this.connectionIndices = Collections.synchronizedList(new ArrayList());
    }

    public void removeConnections() {
        synchronized (activeConnections) {
            for (Iterator i = getActiveConnections().values().iterator(); i.hasNext();) {
                try {
                    stopThenCloseConnection((HJBConnection) i.next());
                } catch (HJBException e) {
                    // it is ok to handle the exception in this way - the
                    // exception is
                    // logged on creation, and we don't want to stop until all
                    // the connections are closed
                }
            }
            connectionIndices.clear();
            activeConnections.clear();
        }
    }

    public Map getActiveConnections() {
        return new HashMap(activeConnections);
    }

    public HJBConnection getConnection(int index) {
        synchronized (activeConnections) {
            if (!activeConnections.containsKey(new Integer(index)))
                throw new IndexOutOfBoundsException("" + index);
            return (HJBConnection) activeConnections.get(new Integer(index));
        }
    }

    public int createHJBConnection(String clientId) {
        synchronized (activeConnections) {
            try {
                Integer index = new Integer(connectionIndices.size());
                connectionIndices.add(index);
                HJBConnection result = new HJBConnection(connectionFactory.createConnection(),
                                                         clientId,
                                                         index.intValue(),
                                                         getClock());
                activeConnections.put(index, result);
                return index.intValue();
            } catch (JMSException e) {
                handleFailedConnectionRegistration(e);
                return HJBStrings.INTEGER_NOT_REACHED;
            }
        }
    }

    public int createHJBConnection(String user, String password, String clientId) {
        synchronized (activeConnections) {
            try {
                Integer index = new Integer(connectionIndices.size());
                connectionIndices.add(index);
                HJBConnection result = new HJBConnection(getConnectionFactory().createConnection(),
                                                         clientId,
                                                         0,
                                                         getClock());
                activeConnections.put(index, result);
                return index.intValue();
            } catch (JMSException e) {
                handleFailedConnectionRegistration(e);
                return HJBStrings.INTEGER_NOT_REACHED;
            }
        }
    }

    public Connection createConnection() {
        return getConnection(createHJBConnection(null));
    }

    public Connection createConnection(String user, String password) {
        return getConnection(createHJBConnection(user, password, null));
    }

    public void startConnection(int index) {
        synchronized (activeConnections) {
            try {
                getConnection(index).start();
                LOG.info(strings().getString(HJBStrings.STARTED_CONNECTION,
                                             getConnection(index)));
            } catch (IndexOutOfBoundsException e) {
                handleConnectionNotFound(index, e);
            } catch (JMSException e) {
                handleConnectionStartFailure(index, e);
            }
        }
    }

    public void stopConnection(int index) {
        synchronized (activeConnections) {
            try {
                getConnection(index).stop();
                LOG.info(strings().getString(HJBStrings.STOPPED_CONNECTION,
                                             getConnection(index)));
            } catch (IndexOutOfBoundsException e) {
                handleConnectionNotFound(index, e);
            } catch (JMSException e) {
                handleConnectionStopFailure(index, e);
            }
        }
    }

    public void deleteConnection(int index) {
        synchronized (activeConnections) {
            try {
                HJBConnection c = getConnection(index);
                String description = c.toString();
                stopThenCloseConnection(c);
                activeConnections.remove(new Integer(index));
                LOG.info(strings().getString(HJBStrings.DELETED_CONNECTION,
                                             description));
            } catch (IndexOutOfBoundsException e) {
                handleConnectionNotFound(index, e);
            }
        }
    }

    public ConnectionMetaData getConnectionMetaData(int index) {
        synchronized (activeConnections) {
            try {
                return getConnection(index).getMetaData();
            } catch (IndexOutOfBoundsException e) {
                handleConnectionNotFound(index, e);
                return null;
            } catch (JMSException e) {
                handleConnectionMetaDataFailure(index, e);
                return null;
            }
        }
    }
    
    public Date getCreationTime() {
        return creationTime;
    }

    protected void stopThenCloseConnection(HJBConnection c) {
        try {
            c.stop();
            c.deleteSessions();
            c.close();
        } catch (JMSException e) {
            handleFailedConnectionClosure(e, c);
        }
    }

    protected HJBConnection handleFailedConnectionClosure(Exception e,
                                                          HJBConnection c) {
        String message = strings().getString(HJBStrings.COULD_NOT_CLOSE_CONNECTION,
                                             c);
        LOG.error(message, e);
        throw new HJBException(message, e);
    }

    protected HJBConnection handleFailedConnectionRegistration(Exception e) {
        String message = strings().getString(HJBStrings.COULD_NOT_REGISTER,
                                             " a new JMS connection");
        LOG.error(message, e);
        throw new HJBException(message, e);
    }

    protected void handleConnectionStopFailure(int index, Exception e) {
        String message = strings().getString(HJBStrings.COULD_NOT_STOP_CONNECTION,
                                             getConnection(index));
        logAndThrowFailure(message, e);
    }

    protected void handleConnectionStartFailure(int index, Exception e) {
        String message = strings().getString(HJBStrings.COULD_NOT_START_CONNECTION,
                                             getConnection(index));
        logAndThrowFailure(message, e);
    }

    protected void handleConnectionMetaDataFailure(int index, Exception e) {
        String message = strings().getString(HJBStrings.COULD_NOT_OBTAIN_CONNECTION_METADATA,
                                             getConnection(index));
        logAndThrowFailure(message, e);
    }

    protected void handleConnectionNotFound(int index, Exception e) {
        String message = strings().getString(HJBStrings.CONNECTION_NOT_FOUND,
                                             new Integer(index));
        logAndThrowClientFailure(message, e);
    }

    protected void logAndThrowFailure(String message, Exception e) {
        LOG.error(message, e);
        throw new HJBException(message, e);
    }

    protected void logAndThrowClientFailure(String message, Exception e) {
        LOG.error(message, e);
        throw new HJBNotFoundException(message, e);
    }

    protected ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected Clock getClock() {
        return clock;
    }

    private final ConnectionFactory connectionFactory;
    private final List connectionIndices;
    private final Map activeConnections;
    private final Clock clock;
    private final Date creationTime;

    private static final Logger LOG = Logger.getLogger(HJBConnectionFactory.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
