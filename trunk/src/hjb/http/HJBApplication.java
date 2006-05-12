package hjb.http;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
 * <code>HJBApplication</code> is the Facade through which the servlet
 * container interacts with HJB objects at runtime.
 * 
 * @author Tim Emiola
 */
public class HJBApplication {

    public HJBApplication(ServletConfig config, Timer timeoutTimer)
            throws ServletException {
        assertNotNull(config, timeoutTimer);
        this.timeoutTimer = timeoutTimer;
        constructRequiredObjectsFrom(config);
    }

    /**
     * Shut down <code>HJBRoot</code> and cancel the timeout timer.
     */
    public void shutdown() {
        getRoot().shutdown();
        getTimeoutTimer().cancel();
    }

    /**
     * Scans through <code>generators</code> and uses the first matching one
     * to process the request.
     * 
     * @param generatorFactory
     *            a <code>JMSCommandGeneratorFactory</code>
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
    public void handleUsingMatchingGenerator(JMSCommandGeneratorFactory generatorFactory,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(strings().getString(HJBStrings.REQUEST_METHOD_AND_PATH,
                                              request.getMethod(),
                                              request.getPathInfo()));
            }
            JMSCommandGenerator generators[] = generatorFactory.getGenerators();
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
            // HJB Exceptions have already been logged, that's
            // why there is a separate exception clause for them here
            new RuntimeExceptionHandler().sendHJBFault(response, e);
        } catch (RuntimeException e) {
            LOG.error(e);
            new RuntimeExceptionHandler().sendHJBFault(response, e);
        }
    }

    protected long getCommandTimeout() {
        return commandTimeout;
    }

    protected HJBRoot getRoot() {
        return root;
    }

    protected Timer getTimeoutTimer() {
        return timeoutTimer;
    }

    protected void assertNotNull(ServletConfig config, Timer timeoutTimer)
            throws IllegalArgumentException {
        if (null == config) {
            throw new IllegalArgumentException(strings().needsANonNull(ServletContext.class));
        }
        if (null == timeoutTimer) {
            throw new IllegalArgumentException(strings().needsANonNull(Timer.class));
        }
    }

    protected void constructRequiredObjectsFrom(ServletConfig config)
            throws ServletException {
        try {
            findCommandTimeout(config);
            findHJBRoot(config);
        } catch (RuntimeException e) {
            throw new ServletException(e);
        }
    }

    private void findHJBRoot(ServletConfig config) {
        String hjbRootPath = config.getInitParameter(HJBServletConstants.ROOT_PATH_CONFIG);
        root = new HJBRoot(new File(hjbRootPath));
    }

    private void findCommandTimeout(ServletConfig config)
            throws ServletException {
        try {
            this.commandTimeout = MILLISECONDS
                    * Long.parseLong(config.getInitParameter(HJBServletConstants.COMMAND_TIMEOUT_CONFIG));
        } catch (NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    protected HJBStrings strings() {
        return STRINGS;
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

        generator.generateCommand(request, getRoot());
        JMSCommand generatedCommand = generator.getGeneratedCommand();

        synchronized (generatedCommand) {
            HJBApplication.CommandTimeoutTask timeIsUp = new HJBApplication.CommandTimeoutTask();
            try {
                // Schedule the command with the command runner, then start a
                // timer to time it out
                generator.getAssignedCommandRunner().schedule(generatedCommand);
                getTimeoutTimer().schedule(timeIsUp, getCommandTimeout());
                while (!generatedCommand.isComplete()) {
                    // N.B. for this to work, JMSCommand#setComplete MUST be
                    // synchronized (and they all are! see
                    // BaseJMSCommand#completed)
                    generatedCommand.wait(getCommandTimeout());
                    if (timeIsUp.barIsClosed()) {
                        handleCommandExecutionWasTimedOut(generator,
                                                          response,
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
            LOG.error(generatedCommand.getStatusMessage(),
                      generatedCommand.getFault());
        }
        generator.sendResponse(response);
    }

    protected void handleCommandExecutionWasTimedOut(JMSCommandGenerator generator,
                                                     HttpServletResponse response,
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
                                             new Long(getCommandTimeout()));
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
        LOG.error(message, e);
        new RuntimeExceptionHandler().sendHJBFault(response,
                                                   new HJBException(message, e));
    }

    private long commandTimeout;
    private Timer timeoutTimer;
    private HJBRoot root;

    /**
     * Constant used to represent the number of milliseconds in a second
     */
    public static final int MILLISECONDS = 1000;

    private Logger LOG = Logger.getLogger(HJBApplication.class);
    private static final HJBStrings STRINGS = new HJBStrings();

    public static class CommandTimeoutTask extends TimerTask {
        public void run() {
            lastOrders = true;
        }

        public boolean barIsClosed() {
            return lastOrders;
        }

        private boolean lastOrders;
    }

}
