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
package hjb.http;

import java.io.IOException;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import hjb.http.cmd.HttpDeleteGeneratorFactory;
import hjb.http.cmd.HttpGetGeneratorFactory;
import hjb.http.cmd.HttpPostGeneratorFactory;
import hjb.misc.HJBConstants;
import hjb.misc.HJBStrings;

/**
 * <code>HJBServlet</code> accepts requests and translates them into
 * <code>JMSCommands</code> that manipulate HJB's configured JMS resources.
 * 
 * <p />
 * The methods {@link #doDelete(HttpServletRequest, HttpServletResponse)},
 * {@link #doGet(HttpServletRequest, HttpServletResponse)} and
 * {@link #doPost(HttpServletRequest, HttpServletResponse)} are overridden so
 * that the
 * {@link HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 * method maps recognized HJB URLs to the correct <code>JMSCommands</code>.
 * 
 * @author Tim Emiola
 */
public class HJBServlet extends HttpServlet {

    public synchronized void init() throws ServletException {
        super.init();
        try {
            getServletContext().setAttribute(HJBConstants.HJB_APPLICATION_ATTRIBUTE,
                                             new HJBApplication(getServletConfig(),
                                                                new Timer()));
            LOG.info(strings().getString(HJBStrings.HELLO_FROM_HJB));
        } catch (Exception e) {
            LOG.error(strings().getString(HJBStrings.COULD_NOT_INITIALISE_HJB_SERVLET),
                      e);
            throw new ServletException(e);
        }
    }

    public synchronized void destroy() {
        try {
            findApplication().shutdown();
        } catch (ServletException e) {
            LOG.error(strings().getString(HJBStrings.SHUTDOWN_NOT_SMOOTH), e);
        }
    }

    /**
     * Gives
     * <code>HJBApplication<code> a {@link HttpDeleteGeneratorFactory} to use to create
     * the set of <code>JMSCommandGenerators</code>, one of which match the request's path and then complete its processing.
     */
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {
        findApplication().handleUsingMatchingGenerator(new HttpDeleteGeneratorFactory(),
                                                       request,
                                                       response);
    }

    /**
     * Gives
     * <code>HJBApplication<code> a {@link HttpGetGeneratorFactory} to use to create
     * the set of <code>JMSCommandGenerators</code>, one of which match the request's path and then complete its processing.
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
            IOException {
        findApplication().handleUsingMatchingGenerator(new HttpGetGeneratorFactory(),
                                                       request,
                                                       response);
    }

    /**
     * Gives
     * <code>HJBApplication<code> a {@link HttpPostGeneratorFactory} to use to create
     * the set of <code>JMSCommandGenerators</code>, one of which match the request's path and then complete its processing.
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        findApplication().handleUsingMatchingGenerator(new HttpPostGeneratorFactory(),
                                                       request,
                                                       response);
    }

    /**
     * Locates the instance of <code>HJBApplication</code> bound to this
     * servlet.
     * 
     * @return the <code>HJBApplication</code>
     * @throws ServletException
     *             if a container error occurs or the
     *             <code>HJBApplication</code> can't be found
     */
    protected HJBApplication findApplication() throws ServletException {
        HJBApplication application = (HJBApplication) getServletContext().getAttribute(HJBConstants.HJB_APPLICATION_ATTRIBUTE);
        if (null == application) {
            throw new ServletException(strings().getString(HJBStrings.HJB_ROOT_IS_AWOL));
        }
        return application;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private static final HJBStrings STRINGS = new HJBStrings();
    private static Logger LOG = Logger.getLogger(HJBServlet.class);
    private static final long serialVersionUID = 9018271691826775148L;
}
