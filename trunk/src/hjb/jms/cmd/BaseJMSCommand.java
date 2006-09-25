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

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>BaseJMSCommand</code> provides a default implementation of selected
 * methods in the <code>JMSCommand</code> interface.
 * 
 * @author Tim Emiola
 */
public abstract class BaseJMSCommand implements JMSCommand {

    public boolean isComplete() {
        return complete;
    }

    public boolean isExecutedOK() {
        return null == getFault();
    }

    public HJBException getFault() {
        return null == fault ? null
                : fault instanceof HJBException ? (HJBException) fault
                        : new HJBException(fault);
    }

    public String toString() {
        return getDescription();
    }

    /**
     * Use to signal that execution of this <code>JMSCommand</code> is
     * complete. After it is invoked, {@link #isComplete()} returns
     * <code>true</code>.
     * <p>
     * It is synchronized so that other threads can wait on a
     * <code>JMSCommand</code> instance's semaphore, and be woken up when this
     * method is executed.
     */
    protected synchronized void completed() {
        this.complete = true;
        notifyAll();
    }

    protected void recordFault(RuntimeException fault) {
        this.fault = fault;
        LOG.error(fault);
    }

    protected void recordFaultAsDebug(RuntimeException fault) {
        this.fault = fault;
        if (LOG.isDebugEnabled()) {
            LOG.debug(fault.getMessage());
        }
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected void assertNotCompleted() {
        if (isComplete()) {
            String message = strings().getString(HJBStrings.TRIED_TO_EXECUTE_COMMAND_TWICE,
                                                 getDescription());
            throw new HJBException(message);
        }
    }

    private RuntimeException fault;
    private boolean complete;
    private static final HJBStrings STRINGS = new HJBStrings();
    private static final Logger LOG = Logger.getLogger(BaseJMSCommand.class);
}
