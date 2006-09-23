package hjb.http;

import java.io.File;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBConstants;
import hjb.testsupport.MockHJBRuntime;

public class HJBServletTest extends MockObjectTestCase {

    public void testInitExtractsExpectedValuesFromTheServletConfig()
            throws Exception {
        Mock mockConfig = mock(ServletConfig.class);
        Mock mockContext = mock(ServletContext.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getServletContext")
            .will(returnValue((ServletContext) mockContext.proxy()));
        mockContext.stubs().method("setAttribute").withAnyArguments();

        mockConfig.expects(once())
            .method("getInitParameter")
            .with(eq(HJBConstants.COMMAND_TIMEOUT_CONFIG))
            .will(returnValue("1000"));
        mockConfig.expects(once())
            .method("getInitParameter")
            .with(eq(HJBConstants.ROOT_PATH_CONFIG))
            .will(returnValue(testRootPath.getAbsolutePath()));

        HJBServlet testServlet = new HJBServletUsingAMockServletConfig(mockConfig);
        testServlet.init();
    }

    public void testInitAddsHJBServletApplicationToTheServletContext()
            throws Exception {
        Mock mockConfig = mock(ServletConfig.class);
        Mock mockContext = mock(ServletContext.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getServletContext")
            .will(returnValue((ServletContext) mockContext.proxy()));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBConstants.COMMAND_TIMEOUT_CONFIG))
            .will(returnValue("1000"));
        mockConfig.stubs()
            .method("getInitParameter")
            .with(eq(HJBConstants.ROOT_PATH_CONFIG))
            .will(returnValue(testRootPath.getAbsolutePath()));

        mockContext.expects(once())
            .method("setAttribute")
            .with(eq(CONSTANTS.HJB_APPLICATION_ATTRIBUTE),
                  isA(HJBApplication.class));

        HJBServlet testServlet = new HJBServletUsingAMockServletConfig(mockConfig);
        testServlet.init();
    }
    
    public void testDestroyShutsdownTheApplication() throws ServletException {
        Mock mockConfig = mock(ServletConfig.class);
        Mock mockContext = mock(ServletContext.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getServletContext")
            .will(returnValue((ServletContext) mockContext.proxy()));

        HJBApplication application = createValidApplication(new Timer());
        mockContext.expects(once())
            .method("getAttribute")
            .with(eq(CONSTANTS.HJB_APPLICATION_ATTRIBUTE))
            .will(returnValue(application));
        String testProvider = "foo";
        mockHJB.make1Provider(application.getRoot(), testProvider);

        assertTrue(application.getRoot().getProviders().size() > 0);
        new HJBServletUsingAMockServletConfig(mockConfig).destroy();
        assertFalse(application.getRoot().getProviders().size() > 0);                
    }

    public void testDoGetUsesTheHttpGetFactory() throws Exception {
        Mock mockRequest = mock(HttpServletRequest.class);
        Mock mockResponse = mock(HttpServletResponse.class);
        mockRequest.stubs().method("getMethod").will(returnValue("GET"));
        mockRequest.stubs()
            .method("getPathInfo")
            .will(returnValue("will/not/match"));

        // the response will be not found because nothing the path does not
        // match
        mockResponse.expects(once())
            .method("sendError")
            .with(eq(HttpServletResponse.SC_NOT_FOUND));

        createInitializedServlet().doDelete((HttpServletRequest) mockRequest.proxy(),
                                            (HttpServletResponse) mockResponse.proxy());

    }

    public void testDoPostUsesTheHttpPostFactory() throws Exception {
        Mock mockRequest = mock(HttpServletRequest.class);
        Mock mockResponse = mock(HttpServletResponse.class);
        mockRequest.stubs().method("getMethod").will(returnValue("POST"));
        mockRequest.stubs()
            .method("getPathInfo")
            .will(returnValue("will/not/match"));

        // the response will be not found because nothing the path does not
        // match
        mockResponse.expects(once())
            .method("sendError")
            .with(eq(HttpServletResponse.SC_NOT_FOUND));

        createInitializedServlet().doDelete((HttpServletRequest) mockRequest.proxy(),
                                            (HttpServletResponse) mockResponse.proxy());
    }

    public void testDoDeleteUsesTheHttpDeleteFactory() throws Exception {
        Mock mockRequest = mock(HttpServletRequest.class);
        Mock mockResponse = mock(HttpServletResponse.class);
        mockRequest.stubs().method("getMethod").will(returnValue("DELETE"));
        mockRequest.stubs()
            .method("getPathInfo")
            .will(returnValue("will/not/match"));

        // the response will be not found because nothing the path does not
        // match
        mockResponse.expects(once())
            .method("sendError")
            .with(eq(HttpServletResponse.SC_NOT_FOUND));

        createInitializedServlet().doDelete((HttpServletRequest) mockRequest.proxy(),
                                            (HttpServletResponse) mockResponse.proxy());
    }

    protected HJBServlet createInitializedServlet() throws ServletException {
        Mock mockConfig = mock(ServletConfig.class);
        Mock mockContext = mock(ServletContext.class);
        mockConfig.stubs().method("getInitParameter").will(returnValue(null));
        mockConfig.stubs()
            .method("getServletContext")
            .will(returnValue((ServletContext) mockContext.proxy()));

        HJBApplication application = createValidApplication(new Timer());
        mockContext.expects(once())
            .method("getAttribute")
            .with(eq(CONSTANTS.HJB_APPLICATION_ATTRIBUTE))
            .will(returnValue(application));
        String testProvider = "foo";
        mockHJB.make1Provider(application.getRoot(), testProvider);

        return new HJBServletUsingAMockServletConfig(mockConfig);
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

    protected void setUp() throws Exception {
        super.setUp();
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    private static class HJBServletUsingAMockServletConfig extends HJBServlet {

        public HJBServletUsingAMockServletConfig(Mock servletConfigMock) {
            this.servletConfigMock = servletConfigMock;
        }

        public ServletConfig getServletConfig() {
            return (ServletConfig) servletConfigMock.proxy();
        }

        private Mock servletConfigMock;
        private static final long serialVersionUID = 1;
    }

    private MockHJBRuntime mockHJB;
    private File testRootPath;
    private HJBConstants CONSTANTS = new HJBConstants();
}
