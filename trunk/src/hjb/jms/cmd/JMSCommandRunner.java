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
package hjb.jms.cmd;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>JMSCommandRunner</code> monitors a FIFO list of
 * <code>JMSCommand</code>s and executes them in turn.
 * 
 * @author Tim Emiola
 */
public class JMSCommandRunner implements Runnable {

    public JMSCommandRunner() {
        commands = Collections.synchronizedList(new LinkedList());
        ignoredCommands = Collections.synchronizedList(new LinkedList());
        setRunning(false);
    }

    public void run() {
        setRunning(true);
        LOG.info(strings().getString(HJBStrings.HJB_RUNNER_STARTED,
                                     getThreadName()));
        synchronized (commands) {
            try {
                while (!isTerminated()) {
                    while (hasCommands()) {
                        processNextCommand();
                    }
                    commands.wait();
                }
            } catch (InterruptedException e) {
                String message = strings().getString(HJBStrings.HJB_RUNNER_DIED,
                                                     getThreadName());
                LOG.error(message, e);
            }
        }
        setRunning(false);
        LOG.info(strings().getString(HJBStrings.HJB_RUNNER_FINISHED,
                                     getThreadName()));
    }

    public void schedule(JMSCommand aCommand) {
        if (null == aCommand) {
            LOG.warn(strings().getString(HJBStrings.HJB_RUNNER_IGNORED_NULL_COMMAND));
            return;
        }
        if (isTerminated()) {
            String message = strings().getString(HJBStrings.HJB_RUNNER_REFUSED_COMMAND,
                                                 aCommand.getDescription());
            LOG.error(message);
            throw new HJBException(message);
        }
        synchronized (commands) {
            commands.add(aCommand);
            LOG.info(strings().getString(HJBStrings.JUST_SCHEDULED_A_COMMAND, aCommand));
            commands.notifyAll();
        }
    }

    public void ignore(JMSCommand aCommand) {
        synchronized (ignoredCommands) {
            ignoredCommands.add(aCommand);
        }
    }

    public boolean isTerminated() {
        return terminated;
    }

    protected void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }

    public void terminate() {
        setTerminated(true);
    }

    protected void processNextCommand() {
        try {
            if (!hasCommands()) return;
            JMSCommand aCommand = (JMSCommand) commands.remove(0);

            // See if the command was ignored while the commands before it were
            // executing in the queue
            synchronized (ignoredCommands) {
                if (ignoredCommands.contains(aCommand)) {
                    String message = strings().getString(HJBStrings.HJB_RUNNER_IGNORED_A_COMMAND,
                                                         aCommand.getDescription(),
                                                         getThreadName());
                    LOG.warn(message);
                    ignoredCommands.remove(aCommand);
                    return;
                }
            }

            // Execute the command if this command runner is not terminated
            if (isTerminated()) {
                String message = strings().getString(HJBStrings.HJB_RUNNER_WAS_TERMINATED,
                                                     aCommand.getDescription(),
                                                     getThreadName());
                LOG.warn(message);
            } else {
                aCommand.execute();
                LOG.info(strings().getString(HJBStrings.JUST_RAN_COMMAND, aCommand));
            }

            // See if the command was ignored while it was being executed
            synchronized (ignoredCommands) {
                if (ignoredCommands.contains(aCommand)) {
                    String message = strings().getString(HJBStrings.HJB_RUNNER_FINISHED_AN_IGNORED_COMMAND,
                                                         aCommand.getDescription(),
                                                         getThreadName());
                    LOG.error(message);
                    ignoredCommands.remove(aCommand);
                }
            }
        } catch (RuntimeException e) {
            LOG.error(e);
        }
    }

    protected boolean isRunning() {
        return running;
    }

    protected void setRunning(boolean isRunning) {
        this.running = isRunning;
    }

    protected boolean hasCommands() {
        return 0 != commands.size();
    }

    protected List getCommands() {
        return commands;
    }

    protected String getThreadName() {
        return Thread.currentThread().getName();
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private List commands;
    private List ignoredCommands;
    private boolean running;
    private volatile boolean terminated;

    private static final Logger LOG = Logger.getLogger(JMSCommandRunner.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
