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

import hjb.jms.cmd.JMSCommandRunner;
import hjb.misc.HJBClientException;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * HJBRoot is the root of an HJB runtime, a registry of the configured
 * Providers.
 * 
 * @author Tim Emiola
 */
public class HJBRoot {

    /**
     * Verifies that <code>storagePath</code> is accessible and starts this
     * <code>HJBRoot</code>'s command runner.
     * 
     * @param storagePath
     *            the path to persist provider details, should this be required.
     */
    public HJBRoot(File storagePath) {
        storagePathIsValid(storagePath);
        this.providers = Collections.synchronizedMap(new HashMap());
        this.storagePath = storagePath;
        startCommandRunner();
    }

    /**
     * Shuts down all registered <code>HJBProviders</code>.
     */
    public void shutdown() {
        LOG.info(strings().getString(HJBStrings.START_SHUTDOWN_PROVIDERS));
        synchronized (providers) {
            for (Iterator i = getProviders().keySet().iterator(); i.hasNext();) {
                try {
                    // this OK, getProviders() makes a copy of the
                    // providers, so deleting from providers is safe
                    // while iterating through it
                    deleteProvider((String) i.next());
                } catch (HJBException e) {}
                // ok to handle the exception in this way, as it is logged on
                // when it is created, and we don't want top until all providers
                // are removed
            }
        }
        LOG.info(strings().getString(HJBStrings.END_SHUTDOWN_PROVIDERS));
    }

    public JMSCommandRunner getCommandRunner() {
        return commandRunner;
    }

    public void addProvider(Hashtable environment) {
        synchronized (providers) {
            HJBProvider provider = new ProviderBuilder(environment).createProvider();
            if (isTheSameProviderAlreadyRegistered(provider)) {
                if (LOG.isDebugEnabled()) {
                    String message = strings().getString(HJBStrings.PROVIDER_ALREADY_REGISTERED,
                                                         provider.getName());
                    LOG.debug(message);
                }
                return;
            }
            assertThatProviderIsUnique(provider);
            providers.put(provider.getName(), provider);
        }
    }

    public void deleteProvider(String providerName) {
        synchronized (providers) {
            Object tobeRemoved = providers.remove(providerName);
            if (null == tobeRemoved) {
                String message = strings().getString(HJBStrings.PROVIDER_NOT_REMOVED,
                                                     providerName);
                LOG.warn(message);
                return;
            }
            HJBProvider removedProvider = (HJBProvider) tobeRemoved;
            removedProvider.shutdown();
        }
    }

    public HJBProvider getProvider(String providerName) {
        synchronized (providers) {
            return (HJBProvider) getProviders().get(providerName);
        }
    }

    public Map getProviders() {
        return new HashMap(providers);
    }

    public File getStoragePath() {
        return storagePath;
    }

    protected synchronized void startCommandRunner() {
        if (null != commandRunner) return;
        commandRunner = new JMSCommandRunner();
        Thread runnerThread = new Thread(commandRunner,
                                         ROOT_COMMAND_RUNNER_NAME);
        runnerThread.setDaemon(true);
        runnerThread.start();
    }

    protected void storagePathIsValid(File storagePath) {
        if (null == storagePath || !storagePath.isDirectory()
                || !storagePath.canWrite()) {
            String message = "Directory " + storagePath
                    + " can not be accessed";
            LOG.error(message);
            throw new HJBException(message);
        }
    }

    protected boolean isTheSameProviderAlreadyRegistered(HJBProvider provider) {
        if (!providers.containsKey(provider.getName())) return false;
        Map registeredEnvironment = ((HJBProvider) providers.get(provider.getName())).getEnvironment();
        return provider.getEnvironment().equals(registeredEnvironment);

    }

    protected void assertThatProviderIsUnique(HJBProvider provider) {
        if (providers.containsKey(provider.getName())) {
            String message = strings().getString(HJBStrings.PROVIDER_NAME_ALREADY_USED,
                                                 provider.getName());
            LOG.error(message);
            throw new HJBClientException(message);
        }
        if (providers.containsValue(provider)) {
            LOG.error(strings().getString(HJBStrings.PROVIDER_WITH_PARAMETERS_EXISTS));
            LOG.error(" cont - " + provider.getDescription());
            throw new HJBException(strings().getString(HJBStrings.PROVIDER_WITH_PARAMETERS_EXISTS));
        }
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private Map providers;
    private JMSCommandRunner commandRunner;
    private File storagePath;

    /**
     * Constant that holds the name given to the thread which runs the HJBRoot
     * command runner.
     * </p>
     * The value of this constant is "HJB Root Command Runner".
     */
    public static final String ROOT_COMMAND_RUNNER_NAME = "HJB Root Command Runner";
    private static final Logger LOG = Logger.getLogger(HJBRoot.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
