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

import hjb.misc.HJBException;

/**
 * <code>JMSCommand</code> declares the methods used to dispatch execution of
 * JMS API methods, determine when execution of those methods is completed, and
 * whether or not execution completed successfully.
 * 
 * <p />
 * The objects that use a <code>JMSCommand</code> (cf
 * <code>JMSCommandGenerators<code> are expected to collaborate
 * with a specific subclass; those specific implementation classes
 * implement this interface to allow them to be executed using a
 * <code>JMSCommandRunner</code>.
 * 
 * @see hjb.http.cmd.JMSCommandGenerator
 * 
 * @author Tim Emiola
 */
public interface JMSCommand {

    /**
     * Executes this <code>JMSCommand's</code> action.
     * 
     * <p />
     * This will involve invoking JMS API methods on configured JMS objects.
     * 
     * @throws HJBException
     *             if a problem occurs during execution
     */
    public void execute();

    /**
     * Indicates whether the processing of this <code>JMSCommand</code> is
     * completed.
     * 
     * <p>
     * This is necessary as the {@link #execute()} method is expected to run on
     * a different thread to one that created the <code>JMSCommand</code>.
     * Implementations of this method are required to ensure that this method
     * returns <code>false</code> if {@link #execute()} has not been called or
     * is still running, and <code>true</code> when it has finished.
     * </p>
     * 
     * @return <code>true</code> when {@link #execute()} has finished running,
     *         <code>false</code> otherwise.
     * @throws HJBException
     *             if a problem occurs determining this value
     */
    public boolean isComplete() throws HJBException;

    /**
     * Indicates whether the processing of this <code>JMSCommand</code> was
     * successful.
     * 
     * <p>
     * This only has meaning after the method {@link #execute()} has been
     * invoked. Prior to that, the results of invoking this method are not
     * defined.
     * 
     * @return <code>true</code> if the command completed execution without
     *         problems, <code>false</code> otherwise.
     * @throws HJBException
     *             if a problem occurs determining this value
     */
    public boolean isExecutedOK() throws HJBException;

    /**
     * Provides a description of this <code>JMSCommand</code>.
     * 
     * @return a description of this <code>JMSCommand</code>
     * @throws HJBException
     *             if a problem occurs while generating the description
     */
    public String getDescription() throws HJBException;

    /**
     * Provides a status message describing the results of executing this
     * <code>JMSCommand</code>.
     * 
     * <p>
     * The status only has meaning after the method {@link #execute()} has been
     * invoked. Prior to that, the result of invoking this method is not
     * defined.
     * 
     * @return a status message describing of the results of executing this
     *         <code>JMSCommand</code>
     * @throws HJBException
     *             if a problem occurs while generating the status
     */
    public String getStatusMessage() throws HJBException;

    /**
     * Obtains the fault describing the problem that caused execution of this
     * <code>JMSCommand</code> to fail.
     * 
     * <p>
     * This only has meaning after the method {@link #execute()} has been
     * invoked <strong>if</strong> {@link #isExecutedOK()} is
     * <code>false</code>. Implementations should return null in all other
     * circumstances.
     * 
     * @return the exception thrown that caused this <code>JMSCommand</code>
     *         to fail
     * @throws HJBException
     *             if a problem occurs while obtaining the fault exception
     */
    public HJBException getFault() throws HJBException;
}
