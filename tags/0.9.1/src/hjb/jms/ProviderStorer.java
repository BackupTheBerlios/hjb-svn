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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import hjb.misc.Clock;
import hjb.misc.HJBStrings;

/**
 * <code>ProviderStorer</code> is used to load and/or save the persistent
 * details of a <code>Provider</code> to the filesystem.
 * 
 * @author Tim Emiola
 */
public class ProviderStorer {

    public ProviderStorer(HJBProvider toBeStored, File storageRoot) {
        if (null == storageRoot || !storageRoot.isDirectory()
                || !storageRoot.canWrite()) {
            throw new IllegalArgumentException(strings().getString(HJBStrings.STORAGE_IS_NOT_WRITABLE,
                                                                   storageRoot));
        }
        this.theProvider = toBeStored;
        this.storageRoot = storageRoot;
    }

    public ProviderStorer(File storageRoot) {
        this(null, storageRoot);
    }

    public Properties asProperties() {
        Properties result = new Properties();
        result.putAll(theProvider.getEnvironment());
        result.put(HJBProvider.HJB_PROVIDER_NAME, theProvider.getName());
        return result;
    }

    public void store() throws IOException {
        asProperties().store(new FileOutputStream(getStoragePath(theProvider.getName())),
                             "environment of Provider: "
                                     + theProvider.getName());
    }

    public void load(String name) throws IOException {
        Properties p = new Properties();
        p.load(new FileInputStream(getStoragePath(name)));
        theProvider = new ProviderBuilder(p, new Clock()).createProvider();
    }

    public HJBProvider getTheProvider() {
        return theProvider;
    }

    protected File getStoragePath(String name) {
        return new File(storageRoot, name + PROVIDER_STORE_EXTENSION);
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private HJBProvider theProvider;

    private File storageRoot;
    private static HJBStrings STRINGS = new HJBStrings();

    public static final String PROVIDER_STORE_EXTENSION = "_provider.properties";
}
