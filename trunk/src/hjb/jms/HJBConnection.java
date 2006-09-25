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

import javax.jms.*;

import org.apache.log4j.Logger;

import hjb.jms.cmd.JMSCommandRunner;
import hjb.jms.info.ConnectionDescription;
import hjb.jms.info.JMSObjectDescription;
import hjb.jms.info.SessionDescription;
import hjb.misc.Clock;
import hjb.misc.HJBException;
import hjb.misc.HJBNotFoundException;
import hjb.misc.HJBStrings;

/**
 * HJBConnection decorates a {@link javax.jms.Connection}.
 * <p />
 * It provides
 * <ul>
 * <li>a cache of the <code>Sessions</code> created by the decorated
 * connection</li>
 * <li>automatic and exclusive use of <code>HJBMessageListener</code> as the
 * decorated connection's <code>MessageListener</code></li>
 * </ul>
 * <p />
 * <strong>N.B</strong> It does not support the optional JMS methods
 * {@link javax.jms.Connection#createConnectionConsumer} and
 * {@link javax.jms.Connection#createDurableConnectionConsumer}
 * 
 * @author Tim Emiola
 */
public class HJBConnection implements Connection {

    /**
     * Creates a <code>HJBConnection</code> that wraps
     * <code>theConnection</code>.
     * 
     * @param theConnection
     *            the wrapped connection
     * @param clientId
     *            the clientId to assign to the create connection
     * @param connectionIndex
     *            the index of this connection in the set of connections created
     *            by a connection factory.
     * @param clock
     *            used to assign times to events that occur within the
     *            <code>HJBConnection</code>
     */
    public HJBConnection(Connection theConnection,
                         String clientId,
                         int connectionIndex,
                         Clock aClock) {
        if (null == theConnection) {
            throw new IllegalArgumentException(strings().needsANonNull(Connection.class));
        }
        if (null == aClock) {
            throw new IllegalArgumentException(strings().needsANonNull(Clock.class));
        }
        this.clock = aClock;
        this.theConnection = theConnection;
        this.sessionIndices = Collections.synchronizedList(new ArrayList());
        this.activeSessions = Collections.synchronizedMap(new HashMap());
        this.connectionIndex = connectionIndex;
        this.creationTime = aClock.getCurrentTime();
        this.connectionListener = new HJBExceptionListener(this);
        assignExceptionListener();
        assignClientIdIfNecessary(clientId);
    }

    /**
     * Creates a <code>HJBConnection</code> that wraps
     * <code>theConnection</code>.
     * 
     * @param theConnection
     *            the wrapped connection
     * @param connectionIndex
     *            the index of this connection in the set of connections created
     *            by a connection factory.
     * @param clock
     *            used to assign times to events that occur within the
     *            <code>HJBConnection</code>
     */
    public HJBConnection(Connection theConnection,
                         int connectionIndex,
                         Clock aClock) {
        this(theConnection, null, connectionIndex, aClock);
    }

    public Session createSession(boolean transacted, int acknowledgeMode) {
        return getSession(createSessionAndReturnItsIndex(transacted,
                                                         acknowledgeMode));
    }

    public int createSessionAndReturnItsIndex(boolean transacted,
                                              int acknowledgeMode) {
        synchronized (activeSessions) {
            try {
                Session rawSession = getTheConnection().createSession(transacted,
                                                                      acknowledgeMode);
                Integer index = new Integer(sessionIndices.size());
                sessionIndices.add(index);
                HJBSession s = new HJBSession(rawSession,
                                              index.intValue(),
                                              getClock());
                activeSessions.put(index, s);
                s.startCommandRunner("" + this);
                return index.intValue();
            } catch (JMSException e) {
                return handleCreateSessionFailure(e);
            }
        }
    }

    public HJBSession getSession(int index) throws IndexOutOfBoundsException {
        synchronized (activeSessions) {
            if (!activeSessions.containsKey(new Integer(index)))
                throw new IndexOutOfBoundsException("" + index);
            return (HJBSession) activeSessions.get(new Integer(index));
        }
    }

    public JMSObjectDescription getSessionDescription(int index) {
        return new SessionDescription(getSession(index));
    }

    public void assertSessionExists(int sessionIndex) {
        getSession(sessionIndex);
    }

    public void deleteSession(int sessionIndex) {
        synchronized (activeSessions) {
            try {
                getSession(sessionIndex).stopCommandRunner();
                getSession(sessionIndex).close();
                activeSessions.remove(new Integer(sessionIndex));
            } catch (IndexOutOfBoundsException e) {
                String message = strings().getString(HJBStrings.SESSION_NOT_FOUND,
                                                     new Integer(sessionIndex));
                LOG.error(message);
                throw new HJBNotFoundException(message);
            } catch (JMSException e) {
                String message = strings().getString(HJBStrings.COULD_NOT_CLOSE_SESSION,
                                                     getSessionDescription(sessionIndex));
                LOG.error(message);
                throw new HJBException(message);
            }
        }
    }

    public void deleteSessions() {
        synchronized (activeSessions) {
            for (Iterator i = getActiveSessions().keySet().iterator(); i.hasNext();) {
                try {
                    deleteSession(((Integer) i.next()).intValue());
                    // this OK, getActiveSessions() makes a copy of the
                    // activeSessions, so deleting from activeSessions is safe
                    // while iterating through it
                } catch (HJBException e) {
                    // ok to handle the exception in this way - it is logged
                    // when it is created, and we don't want to stop until all
                    // the sessions are removed
                }
                sessionIndices.clear();
            }
        }
    }

    public Map getActiveSessions() {
        return new HashMap(activeSessions);
    }

    public int getConnectionIndex() {
        return connectionIndex;
    }
    
    public JMSObjectDescription getDescrption() {
        return new ConnectionDescription(this);
    }

    public String toString() {
        return "" + getDescrption();
    }

    public String getClientID() throws JMSException {
        return getTheConnection().getClientID();
    }

    public void setClientID(String clientId) throws JMSException {
        getTheConnection().setClientID(clientId);
    }

    public ConnectionMetaData getMetaData() throws JMSException {
        return getTheConnection().getMetaData();
    }

    public ExceptionListener getExceptionListener() throws JMSException {
        return getConnectionListener();
    }
    
    public HJBExceptionListener getConnectionListener() {
        return connectionListener;
    }

    public void setExceptionListener(ExceptionListener listener) {
        String listenerName = ""
                + (null == listener ? null : listener.getClass().getName());
        LOG.warn(strings().getString(HJBStrings.IGNORED_MESSAGE_LISTENER,
                                     listenerName));
    }

    public void start() throws JMSException {
        getTheConnection().start();
    }

    public void stop() throws JMSException {
        getTheConnection().stop();
    }

    public void close() throws JMSException {
        getTheConnection().close();
    }

    public ConnectionConsumer createConnectionConsumer(Destination destination,
                                                       String messageSelector,
                                                       ServerSessionPool sessionPool,
                                                       int maxMessages) {
        String message = strings().getString(HJBStrings.OPTIONAL_JMS_NOT_SUPPORTED,
                                             " Connection.createConnectionConsumer");
        LOG.error(message);
        throw new HJBException(message);
    }

    public ConnectionConsumer createDurableConnectionConsumer(Topic topic,
                                                              String subscriptionName,
                                                              String messageSelector,
                                                              ServerSessionPool sessionPool,
                                                              int maxMessages) {
        String message = strings().getString(HJBStrings.OPTIONAL_JMS_NOT_SUPPORTED,
                                             " Connection.createDurableConnectionConsumer");
        LOG.error(message);
        throw new HJBException(message);
    }

    public JMSCommandRunner getSessionCommandRunner(int sessionIndex) {
        synchronized (activeSessions) {
            assertSessionExists(sessionIndex);
            return getSession(sessionIndex).getCommandRunner();
        }
    }
    
    public Date getCreationTime() {
        return creationTime;
    }
    
    public String getErrorLog() throws HJBException {
        return getConnectionListener().getErrorLog();
    }

    protected void assignClientIdIfNecessary(String clientId) {
        try {
            if (clientIdWasSentAndCanBeUsed(clientId)) {
                getTheConnection().setClientID(clientId);
            }
        } catch (JMSException e) {
            String message = strings().getString(HJBStrings.COULD_NOT_SET_CLIENT_ID,
                                                 clientId,
                                                 this);
            LOG.error(message);
        }
    }

    protected void assignExceptionListener() {
        try {
            this.theConnection.setExceptionListener(getConnectionListener());
        } catch (JMSException e) {
            LOG.error(strings().getString(HJBStrings.COULD_NOT_ASSIGN_EXCEPTION_LISTENER));
        }
    }

    protected boolean clientIdWasSentAndCanBeUsed(String clientId)
            throws JMSException {
        return null != clientId && null == getTheConnection().getClientID();
    }

    protected int handleCreateSessionFailure(JMSException e) {
        String message = strings().getString(HJBStrings.COULD_NOT_REGISTER,
                                             " a new JMS session");
        logAndThrowFailure(message, e);
        return HJBStrings.INTEGER_NOT_REACHED;
    }

    protected void logAndThrowFailure(String message, Exception e) {
        LOG.error(message, e);
        throw new HJBException(message, e);
    }

    protected Connection getTheConnection() {
        return theConnection;
    }

    protected Clock getClock() {
        return clock;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private final int connectionIndex;
    private final List sessionIndices;
    private final Map activeSessions;
    private final Connection theConnection;
    private final Clock clock;
    private final Date creationTime;
    private final HJBExceptionListener connectionListener;

    private static final Logger LOG = Logger.getLogger(HJBConnection.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
