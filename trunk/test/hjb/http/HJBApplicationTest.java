package hjb.http;

import java.io.File;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class HJBApplicationTest extends MockObjectTestCase {

    public void testApplicationIsNotConstructedIfArgumentsAreNull()
            throws Exception {
        Mock mockConfig = mock(ServletConfig.class);
        try {
            new HJBApplication((ServletConfig) mockConfig.proxy(), null);
            fail("should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
        try {
            new HJBApplication(null, new Timer());
            fail("should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testApplicationIsNotConstructedIfNoTimeOutValueIsSupplied()
            throws Exception {
        Mock mockConfig = mock(ServletConfig.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBServletConstants.ROOT_PATH_CONFIG))
            .will(returnValue(testRootPath.getAbsolutePath()));
        try {
            new HJBApplication((ServletConfig) mockConfig.proxy(), new Timer());
            fail("Should have thrown a ServletException");
        } catch (ServletException e) {}

    }

    public void testApplicationIsNotConstructedIfNoRootPathIsSupplied()
            throws Exception {
        Mock mockConfig = mock(ServletConfig.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBServletConstants.COMMAND_TIMEOUT_CONFIG))
            .will(returnValue("1000"));
        try {
            new HJBApplication((ServletConfig) mockConfig.proxy(), new Timer());
            fail("Should have thrown a ServletException");
        } catch (ServletException e) {}

    }

    public void testApplicationIsConstructedIfAValidConfigValuesArePresent()
            throws Exception {
        Mock mockConfig = mock(ServletConfig.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBServletConstants.COMMAND_TIMEOUT_CONFIG))
            .will(returnValue("1000"));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBServletConstants.ROOT_PATH_CONFIG))
            .will(returnValue(testRootPath.getAbsolutePath()));
        HJBApplication application = new HJBApplication((ServletConfig) mockConfig.proxy(),
                                                        new Timer());
        assertNotNull(application);
        assertEquals(1000 * HJBApplication.MILLISECONDS,
                     application.getCommandTimeout());
        assertNotNull(application.getRoot());
    }

    public void testCommandTimeoutTaskBarIsInitiallyOpen() {
        assertFalse(new HJBApplication.CommandTimeoutTask().barIsClosed());
    }

    public void testCommandTimeoutTaskBarIsClosedAfterARun() {
        HJBApplication.CommandTimeoutTask task = new HJBApplication.CommandTimeoutTask();
        task.run();
        assertTrue(task.barIsClosed());
    }

    protected void setUp() throws Exception {
        super.setUp();
        testRootPath = File.createTempFile("test", null).getParentFile();
    }

    private File testRootPath;
}
