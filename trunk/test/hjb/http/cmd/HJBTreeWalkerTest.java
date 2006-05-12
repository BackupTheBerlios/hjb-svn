package hjb.http.cmd;

import java.io.File;

import junit.framework.TestCase;

import hjb.jms.HJBRoot;
import hjb.misc.HJBNotFoundException;
import hjb.testsupport.MockHJBRuntime;

public class HJBTreeWalkerTest extends TestCase {

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

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        root = new HJBRoot(testRootPath);
        mockHJB = new MockHJBRuntime();
        mockHJB.make1Session(root, TEST_PROVIDER, TEST_FACTORY);
    }

    private static final String TEST_FACTORY = "testFactory";
    private static final String TEST_PROVIDER = "testProvider";

    private HJBRoot root;
    private File testRootPath;
    private MockHJBRuntime mockHJB;
}
