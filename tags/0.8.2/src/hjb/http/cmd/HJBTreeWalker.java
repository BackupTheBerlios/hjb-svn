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

import javax.jms.Destination;

import org.apache.log4j.Logger;

import hjb.jms.HJBConnection;
import hjb.jms.HJBConnectionFactory;
import hjb.jms.HJBProvider;
import hjb.jms.HJBRoot;
import hjb.misc.HJBException;
import hjb.misc.HJBNotFoundException;
import hjb.misc.HJBStrings;

/**
 * <code>HJBTreeWalker</code> is used to locate various HJB object instances
 * within a <code>HJBRoot</code> at runtime.
 * 
 * @author Tim Emiola
 */
public class HJBTreeWalker {

    public HJBTreeWalker(HJBRoot root, String requestPath) {
        this(root, requestPath, true);
    }

    public HJBTreeWalker(HJBRoot root,
                         String requestPath,
                         boolean failOnMissingComponent) {
        if (null == root) {
            throw new IllegalArgumentException(strings().needsANonNull(HJBRoot.class));
        }
        if (null == requestPath) {
            throw new IllegalArgumentException(strings().needsANonNull("request path"));
        }
        this.root = root;
        this.requestPath = requestPath;
        this.failOnMissingComponent = failOnMissingComponent;
    }

    public HJBProvider findProvider(String providerName) {
        HJBProvider result = root.getProvider(providerName);
        if (null == result) {
            handleMissingComponent(providerName);
        }
        return result;
    }

    public Destination findDestination(String providerName,
                                       String destinationName) {
        HJBProvider provider = findProvider(providerName);
        if (null == provider) {
            return null;
        }
        Destination result = provider.getDestination(destinationName);
        if (null == result) {
            handleMissingComponent(destinationName);
        }
        return result;
    }

    public HJBConnectionFactory findConnectionFactory(String providerName,
                                                      String factoryName) {
        HJBProvider provider = findProvider(providerName);
        if (null == provider) {
            return null;
        }
        HJBConnectionFactory result = provider.getConnectionFactory(factoryName);
        if (null == result) {
            handleMissingComponent(factoryName);
        }
        return result;
    }

    public HJBConnection findConnection(String providerName,
                                        String factoryName,
                                        int connectionIndex) {
        try {
            HJBConnectionFactory factory = findConnectionFactory(providerName,
                                                                 factoryName);
            if (null == factory) {
                return null;
            }
            HJBConnection result = factory.getConnection(connectionIndex);
            if (null == result) {
                handleMissingComponent("" + PathNaming.CONNECTION + ""
                        + connectionIndex);
            }
            return result;
        } catch (IndexOutOfBoundsException e) {
            handleMissingComponent("" + PathNaming.CONNECTION + ""
                    + connectionIndex);
            return null;
        }
    }

    protected void handleMissingComponent(String component) throws HJBException {
        if (failsOnMissingComponent()) {
            String message = strings().getString(HJBStrings.ALLOWED_PATH_NOT_FOUND,
                                                 getRequestPath(),
                                                 component);
            LOG.error(message);
            throw new HJBNotFoundException(message);
        } else {
            if (LOG.isDebugEnabled()) {
                String message = strings().getString(HJBStrings.ALLOWED_PATH_NOT_FOUND,
                                                     getRequestPath(),
                                                     component);
                LOG.debug(message);
            }
        }
    }

    protected String getRequestPath() {
        return requestPath;
    }

    protected HJBRoot getRoot() {
        return root;
    }

    protected boolean failsOnMissingComponent() {
        return failOnMissingComponent;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private HJBRoot root;
    private String requestPath;
    private boolean failOnMissingComponent;
    private static final HJBStrings STRINGS = new HJBStrings();
    private static final Logger LOG = Logger.getLogger(HJBTreeWalker.class);
}