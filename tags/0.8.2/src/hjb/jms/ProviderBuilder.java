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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

import org.apache.log4j.Logger;

import hjb.misc.HJBClientException;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>ProviderBuilder</code> builds a <code>Provider</code> from the
 * contents of a <code>Hashtable</code>.
 * 
 * @author Tim Emiola
 */
public class ProviderBuilder {

    public ProviderBuilder(Hashtable environment) {
        verifyProviderNameIsValid(environment);
        this.environment = new Hashtable(environment);
    }

    public Hashtable obtainJNDIEnvironment() {
        Hashtable result = new Hashtable(environment);
        result.remove(HJBProvider.HJB_PROVIDER_NAME);
        return result;
    }

    public HJBProvider createProvider() {
        return new HJBProvider(obtainHJBProviderName(),
                               obtainJNDIEnvironment(),
                               obtainInitialContext());
    }

    public String obtainHJBProviderName() {
        return (String) environment.get(HJBProvider.HJB_PROVIDER_NAME);
    }

    public Context obtainInitialContext() {
        try {
            return NamingManager.getInitialContext(environment);
        } catch (NamingException e) {
            LOG.error(strings().getString(HJBStrings.COULD_NOT_BUILD_PROVIDER),
                      e);
            LOG.error(" (cont), enviroment = " + new HashMap(environment));
            throw new HJBException(strings().getString(HJBStrings.COULD_NOT_BUILD_PROVIDER),
                                   e);
        }
    }

    protected void verifyProviderNameIsPresent(Hashtable environment) {
        if (environment.containsKey(HJBProvider.HJB_PROVIDER_NAME)) return;
        String message = strings().getString(HJBStrings.NO_PROVIDER_NAME_PRESENT,
                                             HJBProvider.HJB_PROVIDER_NAME);
        LOG.error(message);
        LOG.error(" (cont), enviroment = " + new HashMap(environment));
        throw new HJBClientException(message);
    }

    protected void verifyProviderNameIsValid(Hashtable environment) {
        verifyProviderNameIsPresent(environment);
        String providerName = (String) environment.get(HJBProvider.HJB_PROVIDER_NAME);
        Matcher m = getProviderMatcher().matcher(providerName);
        if (!m.matches()) {
            String message = strings().getString(HJBStrings.PROVIDER_IS_INVALID,
                                                 HJBProvider.HJB_PROVIDER_NAME,
                                                 providerName,
                                                 getProviderMatcher().pattern());
            LOG.error(message);
            LOG.error(" (cont), enviroment = " + new HashMap(environment));
            throw new HJBClientException(message);
        }
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected Pattern getProviderMatcher() {
        return PROVIDER_MATCHER;
    }

    private Hashtable environment;
    private static final Pattern PROVIDER_MATCHER = Pattern.compile("^[_a-zA-Z]\\w*$");
    private static final HJBStrings STRINGS = new HJBStrings();
    private static final Logger LOG = Logger.getLogger(ProviderBuilder.class);
}
