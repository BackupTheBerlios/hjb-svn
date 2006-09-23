package hjb.http;

import java.io.File;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.http.cmd.JMSCommandGenerator;
import hjb.http.cmd.JMSCommandGeneratorFactory;
import hjb.jms.cmd.DeleteProvider;
import hjb.jms.cmd.JMSCommandRunner;
import hjb.misc.HJBConstants;
import hjb.misc.HJBException;
import hjb.testsupport.MockHJBRuntime;

public class HJBApplicationTest extends MockObjectTestCase {

    public void testIsNotConstructedIfArgumentsAreNull() throws Exception {
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

    public void testIsNotConstructedIfNoTimeOutValueIsSupplied()
            throws Exception {
        Mock mockConfig = mock(ServletConfig.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBConstants.ROOT_PATH_CONFIG))
            .will(returnValue(testRootPath.getAbsolutePath()));
        try {
            new HJBApplication((ServletConfig) mockConfig.proxy(), new Timer());
            fail("Should have thrown a ServletException");
        } catch (ServletException e) {}

    }

    public void testIsNotConstructedIfNoRootPathIsSupplied() throws Exception {
        Mock mockConfig = mock(ServletConfig.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBConstants.COMMAND_TIMEOUT_CONFIG))
            .will(returnValue("1000"));
        try {
            new HJBApplication((ServletConfig) mockConfig.proxy(), new Timer());
            fail("Should have thrown a ServletException");
        } catch (ServletException e) {}
    }

    public void testIsConstructedIfAValidConfigValuesArePresent()
            throws Exception {
        HJBApplication application = createValidApplication(new Timer());
        assertNotNull(application);
        assertEquals(1000 * HJBApplication.MILLISECONDS,
                     application.getCommandTimeout());
        assertNotNull(application.getRoot());
    }

    public void testShutdownCancelsTimer() throws Exception {
        final boolean[] shutdown = new boolean[] {
            false
        };
        Timer timer = new Timer() {
            public void cancel() {
                super.cancel();
                shutdown[0] = true;
            }
        };
        HJBApplication application = createValidApplication(timer);
        application.shutdown();
        assertTrue(shutdown[0]);
    }

    public void testShutdownShutsdownHJBRoot() throws Exception {
        HJBApplication application = createValidApplication(new Timer());
        String testProvider = "foo";
        mockHJB.make1Provider(application.getRoot(), testProvider);
        assertEquals(1, application.getRoot().getProviders().size());
        application.shutdown();
        assertEquals(0, application.getRoot().getProviders().size());
    }

    public void testSendsNotFoundIfNoGeneratorsMatch() throws Exception {
        Mock mockGeneratorFactory = mock(JMSCommandGeneratorFactory.class);
        Mock mockRequest = mock(HttpServletRequest.class);
        Mock mockResponse = mock(HttpServletResponse.class);
        HJBApplication application = createValidApplication(new Timer());

        mockRequest.stubs().method("getMethod");
        mockRequest.stubs().method("getPathInfo");
        mockGeneratorFactory.stubs()
            .method("getGenerators")
            .withNoArguments()
            .will(returnValue(new JMSCommandGenerator[0]));
        mockResponse.expects(once())
            .method("sendError")
            .with(eq(HttpServletResponse.SC_NOT_FOUND));

        application.handleUsingMatchingGenerator((JMSCommandGeneratorFactory) mockGeneratorFactory.proxy(),
                                                 (HttpServletRequest) mockRequest.proxy(),
                                                 (HttpServletResponse) mockResponse.proxy());

    }

    public void testSendsErrorResponseWhenCommandGeneratorFails()
            throws Exception {
        Exception[] possibleException = new Exception[] {
                new RuntimeException("thrown as a test"),
                new HJBException("thrown as a test"),
        };
        for (int i = 0; i < possibleException.length; i++) {
            Mock mockGeneratorFactory = mock(JMSCommandGeneratorFactory.class);
            Mock mockRequest = mock(HttpServletRequest.class);
            Mock mockResponse = mock(HttpServletResponse.class);
            Mock mockInvokedGenerator = mock(JMSCommandGenerator.class);
            HJBApplication application = createValidApplication(new Timer());
            String testProvider = "foo";
            mockHJB.make1Provider(application.getRoot(), testProvider);

            mockRequest.stubs().method("getMethod");
            mockRequest.stubs().method("getPathInfo");
            mockGeneratorFactory.stubs()
                .method("getGenerators")
                .withNoArguments()
                .will(returnValue(new JMSCommandGenerator[] {
                    (JMSCommandGenerator) mockInvokedGenerator.proxy(),
                }));

            mockInvokedGenerator.stubs()
                .method("matches")
                .withAnyArguments()
                .will(returnValue(true));
            mockInvokedGenerator.stubs()
                .method("generateCommand")
                .withAnyArguments()
                .will(throwException(possibleException[i]));
            mockResponse.expects(once())
                .method("addHeader")
                .with(eq(HJBConstants.HJB_STATUS_HEADER),
                      eq("thrown as a test"));
            mockResponse.expects(once())
                .method("addHeader")
                .with(eq(HJBConstants.HTTP_CACHE_CONTROL),
                      eq(HJBConstants.HJB_DEFAULT_CACHE_CONTROL));
            mockResponse.expects(once())
                .method("sendError")
                .with(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
                      eq("thrown as a test"));

            application.handleUsingMatchingGenerator((JMSCommandGeneratorFactory) mockGeneratorFactory.proxy(),
                                                     (HttpServletRequest) mockRequest.proxy(),
                                                     (HttpServletResponse) mockResponse.proxy());

        }
    }

    public void testInvokesFirstCommandGeneratorThatMatches() throws Exception {
        Mock mockGeneratorFactory = mock(JMSCommandGeneratorFactory.class);
        Mock mockRequest = mock(HttpServletRequest.class);
        Mock mockResponse = mock(HttpServletResponse.class);
        Mock mockInvokedGenerator = mock(JMSCommandGenerator.class);
        Mock mockOtherGenerator = mock(JMSCommandGenerator.class);
        HJBApplication application = createValidApplication(new Timer());
        String testProvider = "foo";
        mockHJB.make1Provider(application.getRoot(), testProvider);

        mockRequest.stubs().method("getMethod");
        mockRequest.stubs().method("getPathInfo");
        mockGeneratorFactory.stubs()
            .method("getGenerators")
            .withNoArguments()
            .will(returnValue(new JMSCommandGenerator[] {
                    (JMSCommandGenerator) mockInvokedGenerator.proxy(),
                    (JMSCommandGenerator) mockOtherGenerator.proxy(),
            }));

        mockInvokedGenerator.stubs()
            .method("matches")
            .withAnyArguments()
            .will(returnValue(true));
        mockInvokedGenerator.stubs()
            .method("getAssignedCommandRunner")
            .will(returnValue(createAndStartCommandRunner()));
        mockInvokedGenerator.stubs().method("generateCommand");
        mockInvokedGenerator.stubs()
            .method("getGeneratedCommand")
            .will(returnValue(new DeleteProvider(application.getRoot(),
                                                 testProvider)));
        mockInvokedGenerator.stubs().method("sendResponse").withAnyArguments();

        mockOtherGenerator.expects(never()).method("matches");

        application.handleUsingMatchingGenerator((JMSCommandGeneratorFactory) mockGeneratorFactory.proxy(),
                                                 (HttpServletRequest) mockRequest.proxy(),
                                                 (HttpServletResponse) mockResponse.proxy());

    }

    public void testCommandTimeoutTaskBarIsInitiallyOpen() {
        assertFalse(new HJBApplication.CommandTimeoutTask().barIsClosed());
    }

    public void testCommandTimeoutTaskBarIsClosedAfterARun() {
        HJBApplication.CommandTimeoutTask task = new HJBApplication.CommandTimeoutTask();
        task.run();
        assertTrue(task.barIsClosed());
    }

    protected JMSCommandRunner createAndStartCommandRunner() {
        testCommandRunner = new JMSCommandRunner();
        Thread t = new Thread(testCommandRunner);
        t.setDaemon(true);
        t.start();
        return testCommandRunner;
    }

    protected void setUp() throws Exception {
        super.setUp();
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    protected void tearDown() throws Exception {
        if (null != testCommandRunner) {
            testCommandRunner.terminate();
            testCommandRunner = null;
        }
    }

    protected HJBApplication createValidApplication(Timer timer)
            throws ServletException {
        Mock mockConfig = mock(ServletConfig.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBConstants.COMMAND_TIMEOUT_CONFIG))
            .will(returnValue("1000"));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBConstants.ROOT_PATH_CONFIG))
            .will(returnValue(testRootPath.getAbsolutePath()));
        HJBApplication application = new HJBApplication((ServletConfig) mockConfig.proxy(),
                                                        timer);
        return application;
    }

    private MockHJBRuntime mockHJB;
    private JMSCommandRunner testCommandRunner;
    private File testRootPath;
}
