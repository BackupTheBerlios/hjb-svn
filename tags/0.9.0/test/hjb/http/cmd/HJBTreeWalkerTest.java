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

import java.io.File;

import hjb.jms.HJBRoot;
import hjb.misc.HJBNotFoundException;
import hjb.misc.PathNaming;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockHJBRuntime;

public class HJBTreeWalkerTest extends BaseHJBTestCase {

    public void testHJBTreeWalkerThrowsExceptionIfHJBRootIsNull() {
        try {
            new HJBTreeWalker(null, "");
        } catch (IllegalArgumentException e) {}
    }

    public void testHJBTreeWalkerThrowsExceptionIfRequestPathIsNull() {
        try {
            new HJBTreeWalker(root, null);
        } catch (IllegalArgumentException e) {}
    }

    public void testFindsProviderIfOneIsPresent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER);
        assertNotNull(tw.findProvider(TEST_PROVIDER));
    }

    public void testThrowsIfProviderIsNotPresentAndFailsOnMissingComponent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER, true);
        try {
            tw.findProvider("anyProvider");
            fail("Should have thrown a HJBNotFoundException");
        } catch (HJBNotFoundException e) {}
    }

    public void testReturnsNullIfProviderIsNotPresentAndNotFailsOnMissingComponent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, "notSetProvider", false);
        assertNull(tw.findProvider("notSetProvider"));
    }

    public void testFindsConnectionFactoryIfOneIsPresent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER + "/"
                + TEST_FACTORY);
        assertNotNull(tw.findConnectionFactory(TEST_PROVIDER, TEST_FACTORY));
    }

    public void testThrowsIfFactoryIsNotPresentAndFailsOnMissingComponent() {
        HJBTreeWalker tw = new HJBTreeWalker(root,
                                             TEST_PROVIDER + "/notHere",
                                             true);
        try {
            tw.findConnectionFactory(TEST_PROVIDER, "notHere");
            fail("Should have thrown an HJBNotFoundException");
        } catch (HJBNotFoundException e) {}
    }

    public void testReturnsNullIfFactoryIsNotPresentAndNotFailsOnMissingComponent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER + "/"
                + "notHere", false);
        assertNull(tw.findConnectionFactory(TEST_FACTORY, "notSetFactory"));
    }

    public void testFindsConnectionIfOneIsPresent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER + "/"
                + TEST_FACTORY + "/" + PathNaming.CONNECTION + "-0");
        assertNotNull(tw.findConnection(TEST_PROVIDER, TEST_FACTORY, 0));
    }

    public void testThrowsIfConnectionIsNotPresentAndFailsOnMissingComponent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER + "/"
                + TEST_FACTORY + "/" + PathNaming.CONNECTION + "-1");
        try {
            tw.findConnection(TEST_PROVIDER, TEST_FACTORY, 1);
            fail("Should have thrown an HJBNotFoundException");
        } catch (HJBNotFoundException e) {}
    }

    public void testReturnsNullIfConnectionIsNotPresentAndNotFailsOnMissingComponent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER + "/"
                + TEST_FACTORY + "/" + PathNaming.CONNECTION + "-1", false);
        assertNull(tw.findConnection(TEST_PROVIDER, TEST_FACTORY, 1));
    }

    public void testFindsSessionIfOneIsPresent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER + "/"
                + TEST_FACTORY + "/" + PathNaming.CONNECTION + "-0/"
                + PathNaming.SESSION + "-0");
        assertNotNull(tw.findSession(TEST_PROVIDER, TEST_FACTORY, 0, 0));
    }

    public void testThrowsIfSessionIsNotPresentAndFailsOnMissingComponent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER + "/"
                + TEST_FACTORY + "/" + PathNaming.CONNECTION + "-0/"
                + PathNaming.SESSION + "-1");
        try {
            tw.findSession(TEST_PROVIDER, TEST_FACTORY, 0, 1);
            fail("Should have thrown an HJBNotFoundException");
        } catch (HJBNotFoundException e) {}
    }

    public void testReturnsNullIfSessionIsNotPresentAndNotFailsOnMissingComponent() {
        HJBTreeWalker tw = new HJBTreeWalker(root, TEST_PROVIDER + "/"
                + TEST_FACTORY + "/" + PathNaming.CONNECTION + "-0/"
                + PathNaming.SESSION + "-1", false);
        assertNull(tw.findSession(TEST_PROVIDER, TEST_FACTORY, 0, 1));
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        root = new HJBRoot(testRootPath, defaultTestClock());
        mockHJB = new MockHJBRuntime();
        mockHJB.make1Session(root, TEST_PROVIDER, TEST_FACTORY);
    }

    private static final String TEST_FACTORY = "testFactory";
    private static final String TEST_PROVIDER = "testProvider";

    private HJBRoot root;
    private File testRootPath;
    private MockHJBRuntime mockHJB;
}
