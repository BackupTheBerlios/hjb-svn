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

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import hjb.http.cmd.JMSCommandGenerator;
import hjb.http.cmd.JMSCommandGeneratorFactory;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.JMSCommand;
import hjb.misc.HJBException;
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
            String hjbRootPath = getServletConfig().getInitParameter(HJBServletConstants.ROOT_PATH_CONFIG);
            Long commandTimeout = new Long(MILLISECONDS
                    * Long.parseLong(getServletConfig().getInitParameter(HJBServletConstants.COMMAND_TIMEOUT_CONFIG)));
            HJBRoot root = new HJBRoot(new File(hjbRootPath));
            getServletContext().setAttribute(HJBServletConstants.HJB_ROOT_ATTRIBUTE,
                                             root);
            getServletContext().setAttribute(HJBServletConstants.COMMAND_TIMEOUT_CONFIG,
                                             commandTimeout);
            getServletContext().setAttribute(HJBServletConstants.COMMAND_TIMEOUT_ATTRIBUTE,
                                             new Timer());
            getServletContext().log(strings().getString(HJBStrings.HELLO_FROM_HJB));
            LOG.info(strings().getString(HJBStrings.HELLO_FROM_HJB));
        } catch (Exception e) {
            getServletContext().log(strings().getString(HJBStrings.COULD_NOT_INITIALISE_HJB_SERVLET),
                                    e);
            LOG.error(strings().getString(HJBStrings.COULD_NOT_INITIALISE_HJB_SERVLET),
                      e);
            throw new ServletException(e);
        }
    }

    /**
     * Removes the <code>HJBRoot</code> and any of its configured resources
     * from the servlet context.
     */
    public synchronized void destroy() {
        try {
            findHJBRoot().shutdown();
            findTimer().cancel();
        } catch (ServletException e) {
            LOG.error(strings().getString(HJBStrings.SHUTDOWN_NOT_SMOOTH), e);
            getServletContext().log(strings().getString(HJBStrings.SHUTDOWN_NOT_SMOOTH),
                                    e);
        }
    }

    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {
        handleUsingMatchingGenerator(getGeneratorFactory().getDeleteGenerators(),
                                     request,
                                     response);
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
            IOException {
        handleUsingMatchingGenerator(getGeneratorFactory().getGetGenerators(),
                                     request,
                                     response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        handleUsingMatchingGenerator(getGeneratorFactory().getPostGenerators(),
                                     request,
                                     response);
    }

    /**
     * Scans through <code>generators</code> and uses the first matching one
     * to process the request.
     * 
     * @param generators
     *            some <code>JMSCommandGenerators</code>
     * @param request
     *            a <code>HttpServletRequest</code>
     * @param response
     *            a <code>HttpResponse</code>
     * @throws ServletException
     *             if there is a problem accessing the servlets
     *             <code>HJBRoot</code>
     * @throws IOException
     *             if a problem occurs while writing the response
     */
    protected void handleUsingMatchingGenerator(JMSCommandGenerator generators[],
                                                HttpServletRequest request,
                                                HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(strings().getString(HJBStrings.REQUEST_METHOD_AND_PATH,
                                              request.getMethod(),
                                              request.getPathInfo()));
            }
            for (int i = 0; i < generators.length; i++) {
                if (generators[i].matches(request.getPathInfo())) {
                    LOG.info(strings().getString(HJBStrings.HANDLING_REQUEST,
                                                 request.getMethod(),
                                                 request.getPathInfo(),
                                                 generators[i]));
                    handleUsingSelectedGenerator(generators[i],
                                                 request,
                                                 response);
                    return;
                }
            }
            LOG.info(strings().getString(HJBStrings.NOT_MATCHED,
                                         request.getPathInfo()));
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (HJBException e) {
            new RuntimeExceptionHandler().sendHJBFault(response, e);
        } catch (RuntimeException e) {
            getServletContext().log(e.getMessage(), e);
            LOG.error(e);
            new RuntimeExceptionHandler().sendHJBFault(response, e);
        }
    }

    /**
     * Uses <code>generator</code> to generate a command, execute it, then
     * send the appropriate response.
     * 
     * <p>
     * The following steps occur:
     * <ul>
     * <li>The command and its assigned command runner are generated by the
     * generator</li>
     * <li>The command is scheduled to execute on the command runner</li>
     * <li>The method blocks until the command is executed, or the timeout
     * occurs, (the timeout period is a servlet configuration parameter)</li>
     * <li>When execution is complete, a response is sent using
     * {@link JMSCommandGenerator#sendResponse(HttpServletResponse)}
     * </ul>
     * </p>
     * 
     * @param generator
     *            the selected <code>JMSCommandGenerator</code>
     * @param request
     *            a <code>HttpServletRequest</code>
     * @param response
     *            a <code>HttpResponse</code>
     * @throws ServletException
     *             if there is a problem accessing the servlets
     *             <code>HJBRoot</code>
     * @throws IOException
     *             if a problem occurs while writing the response
     */
    protected void handleUsingSelectedGenerator(JMSCommandGenerator generator,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
            throws ServletException, IOException {

        Long commandTimeout = findCommandTimeout();
        Timer commandTimer = findTimer();
        generator.generateCommand(request, findHJBRoot());
        JMSCommand generatedCommand = generator.getGeneratedCommand();

        synchronized (generatedCommand) {
            CommandTimeoutTask timeIsUp = new CommandTimeoutTask();
            try {
                // Schedule the command with the command runner, then start a
                // timer to time it out
                generator.getAssignedCommandRunner().schedule(generatedCommand);
                commandTimer.schedule(timeIsUp, commandTimeout.longValue());
                while (!generatedCommand.isComplete()) {
                    // N.B. for this to work, JMSCommand#setComplete MUST be
                    // synchronized (and they all are! see
                    // BaseJMSCommand#completed)
                    generatedCommand.wait(commandTimeout.longValue());
                    if (timeIsUp.barIsClosed()) {
                        handleCommandExecutionWasTimedOut(generator,
                                                          response,
                                                          commandTimeout,
                                                          generatedCommand);
                        return;
                    }
                }
            } catch (InterruptedException e) {
                handleExecutionThreadWasInterrupted(response,
                                                    generatedCommand,
                                                    e);
                return;
            } finally {
                timeIsUp.cancel();
            }
        }
        if (!generatedCommand.isExecutedOK()) {
            getServletContext().log(generatedCommand.getStatusMessage(),
                                    generatedCommand.getFault());
        }
        generator.sendResponse(response);
    }

    protected void handleCommandExecutionWasTimedOut(JMSCommandGenerator generator,
                                                     HttpServletResponse response,
                                                     Long commandTimeout,
                                                     JMSCommand generatedCommand)
            throws IOException {
        // Instruct the command runner to ignore the command
        // The command runner **will** log the instruction
        // If the command has not run yet (i.e, the
        // timeout occurred while the command was waiting to be
        // run), the command runner will discard the ignored
        // command
        // Otherwise, at least both HJB and the HTTP user agent
        // will have a message showing that something went
        // wrong...
        generator.getAssignedCommandRunner().ignore(generatedCommand);
        String message = strings().getString(HJBStrings.COMMAND_EXECUTION_TIMED_OUT,
                                             generatedCommand.getDescription(),
                                             commandTimeout);
        getServletContext().log(message);
        LOG.error(message);
        HJBException fault = new HJBException(message);
        fault.fillInStackTrace();
        new RuntimeExceptionHandler().sendHJBFault(response, fault);
    }

    protected void handleExecutionThreadWasInterrupted(HttpServletResponse response,
                                                       JMSCommand generatedCommand,
                                                       InterruptedException e)
            throws IOException {
        String message = strings().getString(HJBStrings.COMMAND_EXECUTION_WAS_INTERRUPTED,
                                             generatedCommand);
        getServletContext().log(message, e);
        LOG.error(message, e);
        new RuntimeExceptionHandler().sendHJBFault(response,
                                                   new HJBException(message, e));
    }

    protected Timer findTimer() throws ServletException {
        Timer timeoutTimer = (Timer) getServletContext().getAttribute(HJBServletConstants.COMMAND_TIMEOUT_ATTRIBUTE);
        if (null == timeoutTimer) {
            throw new ServletException(strings().getString(strings().getString(HJBStrings.COMMAND_TIMEOUT_TIMER_IS_AWOL)));
        }
        return timeoutTimer;
    }

    protected Long findCommandTimeout() throws ServletException {
        Long commandTimeout = (Long) getServletContext().getAttribute(HJBServletConstants.COMMAND_TIMEOUT_CONFIG);
        if (null == commandTimeout) {
            throw new ServletException(strings().getString(strings().getString(HJBStrings.COMMAND_TIMEOUT_IS_AWOL)));
        }
        return commandTimeout;
    }

    protected HJBRoot findHJBRoot() throws ServletException {
        HJBRoot root = (HJBRoot) getServletContext().getAttribute(HJBServletConstants.HJB_ROOT_ATTRIBUTE);
        if (null == root) {
            throw new ServletException(strings().getString(strings().getString(HJBStrings.HJB_ROOT_IS_AWOL)));
        }
        return root;
    }

    protected JMSCommandGeneratorFactory getGeneratorFactory() {
        return GENERATOR_FACTORY;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private static class CommandTimeoutTask extends TimerTask {
        public void run() {
            lastOrders = true;
        }

        public boolean barIsClosed() {
            return lastOrders;
        }

        private boolean lastOrders;
    }

    private static final int MILLISECONDS = 1000;
    private static final JMSCommandGeneratorFactory GENERATOR_FACTORY = new JMSCommandGeneratorFactory();
    private static final HJBStrings STRINGS = new HJBStrings();
    private static Logger LOG = Logger.getLogger(HJBServlet.class);
    private static final long serialVersionUID = 9018271691826775148L;
}
