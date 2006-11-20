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

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import hjb.misc.*;

/**
 * <code>HJBProvider</code> represents a JMS Provider registered with HJB.
 * 
 * @author Tim Emiola
 */
public class HJBProvider {

    public HJBProvider(String name,
                       Hashtable environment,
                       Context providerContext,
                       Clock aClock) {
        if (null == aClock) {
            throw new IllegalArgumentException(strings().needsANonNull(Clock.class));
        }
        this.name = name;
        this.environment = new HashMap(environment);
        this.providerContext = providerContext;
        this.clock = aClock;
        this.creationTime = aClock.getCurrentTime();
        destinations = Collections.synchronizedSortedMap(new TreeMap());
        factories = Collections.synchronizedSortedMap(new TreeMap());
    }

    /**
     * <code>Deletes all registered <code>ConnectionFactories</code> and <code>Destinations</code>.
     *
     */
    public void shutdown() {
        LOG.info(strings().getString(HJBStrings.START_SHUTDOWN_1_PROVIDER,
                                     getName()));
        shutdownConnectionFactories();
        shutdownDestinations();
        LOG.info(strings().getString(HJBStrings.END_SHUTDOWN_1_PROVIDER,
                                     getName()));
    }

    public String getName() {
        return name;
    }

    public Map getEnvironment() {
        return new HashMap(environment);
    }

    public void registerConnectionFactory(String name) {
        synchronized (factories) {
            ConnectionFactory f = (ConnectionFactory) locate(name);
            if (factories.containsKey(name)) {
                if (LOG.isDebugEnabled()) {
                    String message = strings().getString(HJBStrings.REGISTRATION_IGNORED,
                                                         "factory " + name);
                    LOG.debug(message);
                }
            } else {
                factories.put(name, new HJBConnectionFactory(f,
                                                             getClock()));
            }
        }
    }

    public void registerDestination(String name) {
        synchronized (destinations) {
            Destination d = (Destination) locate(name);
            if (destinations.containsKey(name)) {
                if (LOG.isDebugEnabled()) {
                    String message = strings().getString(HJBStrings.REGISTRATION_IGNORED,
                                                         "destination " + name);
                    LOG.debug(message);
                }
            } else {
                destinations.put(name, d);
            }
        }
    }

    public void deleteConnectionFactory(String factoryName) {
        Object tobeRemoved = factories.remove(factoryName);
        if (null == tobeRemoved) {
            String message = strings().getString(HJBStrings.NOT_REMOVED,
                                                 "Connection Factory "
                                                         + factoryName);
            LOG.warn(message);
            return;
        }
        HJBConnectionFactory removedFactory = (HJBConnectionFactory) tobeRemoved;
        removedFactory.removeConnections();
    }

    public void deleteDestination(String destinationName) {
        Object tobeRemoved = destinations.remove(destinationName);
        if (null == tobeRemoved) {
            String message = strings().getString(HJBStrings.NOT_REMOVED,
                                                 "Destination "
                                                         + destinationName);
            LOG.warn(message);
        }
    }

    public HJBConnectionFactory getConnectionFactory(String factoryName) {
        return (HJBConnectionFactory) getConnectionFactories().get(factoryName);
    }

    public Destination getDestination(String destinationName) {
        return (Destination) getDestinations().get(destinationName);
    }

    public Map getConnectionFactories() {
        return new HashMap(factories);
    }

    public Map getDestinations() {
        return new HashMap(destinations);
    }

    public String getDescription() {
        return "Name:  " + getName() + ", environment: " + getEnvironment();
    }

    /**
     * Providers are considered to be equivalent if their environments are the
     * same. HJBRoot ensures that names are always different, so these are not
     * included in the comparison.
     * 
     * @return <code>true</code> if the <code>Provider</code> have the same
     *         environments
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof HJBProvider)) return false;
        return ((HJBProvider) obj).getEnvironment()
            .equals(this.getEnvironment());
    }

    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * In accordance with rules in {@link Object#hashCode()}, this is
     * overridden to be consistent with {@link #equals(Object)}.
     * 
     * @return a hashCode consistent with the
     *         <code>Provider.equals(Object)</code>
     */
    public int hashCode() {
        return getEnvironment().hashCode();
    }

    public String toString() {
        return getDescription();
    }

    protected void shutdownDestinations() {
        synchronized (destinations) {
            for (Iterator i = getDestinations().keySet().iterator(); i.hasNext();) {
                try {
                    // this OK, getDestinations() makes a copy of the
                    // destinations, so deleting from destinations is safe
                    // while iterating through it
                    deleteDestination((String) i.next());
                } catch (HJBException e) {}
                // ok to handle the exception in this way - it is logged
                // when it is created, and we don't want to stop until all the
                // destinations are removed
            }
        }
    }

    protected void shutdownConnectionFactories() {
        synchronized (factories) {
            for (Iterator i = getConnectionFactories().keySet().iterator(); i.hasNext();) {
                try {
                    // this OK, getConnectionFactories() makes a copy of the
                    // factories, so deleting from factories is safe
                    // while iterating through it
                    deleteConnectionFactory((String) i.next());
                } catch (HJBException e) {}
                // ok to handle the exception in this way - it is logged
                // when it is created, and we don't want to stop until all the
                // factories are removed
            }
        }
    }

    protected Object locate(String name) {
        try {
            Object result = getProviderContext().lookup(name);
            if (null != result) {
                return result;
            } else {
                result = getProviderContext().lookup(name);
                if (null != result) {
                    return result;
                }
                return handleResourceNotFound(name, null);
            }
        } catch (NamingException e) {
            return handleResourceNotFound(name, e);
        }
    }

    /**
     * Adds a leading slash to a name.
     * 
     * this is a kludge. TODO fix the need for this.
     * 
     * @param name
     *            a potential name stored in JNDI.
     * @return name with a leading '/'
     */
    protected String addInitialSlashTo(String name) {
        return "/" + name;
    }

    protected Object handleResourceNotFound(String name, NamingException e) {
        String message = strings().getString(HJBStrings.RESOURCE_NOT_FOUND,
                                             name,
                                             getName());
        if (null != e) {
            LOG.error(message, e);
            throw new HJBNotFoundException(message, e);
        } else {
            LOG.error(message);
            throw new HJBNotFoundException(message);
        }
    }

    protected void handleFailedRegistration(String name, Exception e) {
        String message = strings().getString(HJBStrings.COULD_NOT_REGISTER,
                                             name);
        LOG.error(message, e);
        throw new HJBClientException(message, e);
    }

    protected Context getProviderContext() {
        return providerContext;
    }

    protected Clock getClock() {
        return clock;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    /**
     * Constant that holds the environment property for specifying the name of
     * the provider in the hjb resource hierarchy.
     * </p>
     * The value of this constant is "hjb.provider.name".
     */
    public static String HJB_PROVIDER_NAME = "hjb.provider.name";

    private final String name;
    private final Context providerContext;
    private final Map environment;
    private final Map destinations;
    private final Map factories;
    private final Clock clock;
    private final Date creationTime;

    private static final Logger LOG = Logger.getLogger(HJBProvider.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
