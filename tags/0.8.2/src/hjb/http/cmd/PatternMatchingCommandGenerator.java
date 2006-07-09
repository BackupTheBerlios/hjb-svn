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
package hjb.http.cmd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hjb.http.HJBServletConstants;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.JMSCommandRunner;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>PatternMatchingCommandGenerator</code> is a base class for
 * <code>JMSCommandGenerator</code> implementations that should be invoked on
 * specific paths in the <code>HJBServlet</code> that match a particuler
 * {@link Pattern}.
 * 
 * <p>
 * It implements {@link #generateCommand(HttpServletRequest, HJBRoot)} as a
 * <em>Template Method</em>, and defines two abstract methods
 * {@link #getPathPattern()} and
 * {@link #constructCommandFrom(HttpServletRequest, HJBRoot)} that subclasses
 * implement to produce their required behaviour.
 * </p>
 * 
 * <p>
 * By default, the value of {@link HJBRoot#getCommandRunner()} is returned as
 * the value of {@link #getAssignedCommandRunner()}. Subclasses can change this
 * when they implement
 * {@link #constructCommandFrom(HttpServletRequest, HJBRoot)}. Should this be
 * done, the method {@link #useRootCommandRunner()} should also be overridden to
 * return <code>false</code>
 * </p>
 * 
 * @author Tim Emiola
 */
public abstract class PatternMatchingCommandGenerator implements
        JMSCommandGenerator {

    public void generateCommand(HttpServletRequest request, HJBRoot root)
            throws HJBException {
        assertRootIsNotNull(root);
        assertRequestIsNotNull(request);
        assertMatches(request);
        constructCommandFrom(request, root);
        if (useRootCommandRunner()) {
            setAssignedCommandRunner(root.getCommandRunner());
        }
    }

    public void sendResponse(HttpServletResponse response) throws IOException {
        createResponder().sendResponse(response);
    }

    public boolean matches(String path) throws HJBException {
        Matcher m = getPathPattern().matcher(path);
        return m.matches();
    }

    public JMSCommandRunner getAssignedCommandRunner() throws HJBException {
        if (null == assignedCommandRunner)
            throw new IllegalStateException(strings().getString(HJBStrings.JMS_COMMAND_RUNNER_NOT_ASSIGNED));
        return assignedCommandRunner;
    }

    public String toString() {
        String clazzName = this.getClass().getName();
        if (-1 == clazzName.lastIndexOf('.')) return "[" + clazzName + "]";
        return "[" + clazzName.substring(clazzName.lastIndexOf('.') + 1) + "]";
    }

    /**
     * Indicates whether the {@link HJBRoot#getCommandRunner()} should be used
     * as the value for {@link #getAssignedCommandRunner()}.
     * <p>
     * subclasses <em>must</em> override this method if they assign a
     * different <code>CommandRunner</code>, when they do, this method will
     * usually return <code>false</code>
     * </p>
     * 
     * @return <code>true<code> by default, <code>false</code> when overridden by a subclass
     */
    protected boolean useRootCommandRunner() {
        return true;
    }

    /**
     * Returns the <code>Pattern</code> that paths must match if they are to
     * be processed using this <code>JMSCommandGenerator</code>.
     * 
     * @return the <code>Pattern</code> that paths must match if they are to
     *         be processed using this <code>JMSCommandGenerator</code>
     */
    protected abstract Pattern getPathPattern();

    /**
     * Constructs the
     * <code>JMSCommand<code> for this <code>JMSCommandGenerator</code> from <code>request</code> and <code>root</code>.
     * 
     * <p /> 
     * This method constructs the <code>JMSCommand</code> and keeps a reference to it
     * for use in {@link JMSCommandGenerator#getGeneratedCommand()} and {@link JMSCommandGenerator#sendResponse(HttpServletResponse)}
     *
     * @param request a <code>HttpServletRequest</code>  
     * @param root a <code>HJBRoot</code>
     */
    protected abstract void constructCommandFrom(HttpServletRequest request,
                                                 HJBRoot root)
            throws HJBException;

    protected void setAssignedCommandRunner(JMSCommandRunner assignedCommandRunner) {
        this.assignedCommandRunner = assignedCommandRunner;
    }

    /**
     * Creates the <code>JMSCommandResponder</code> used to handle the
     * response.
     * 
     * @return a <code>JMSCommandResponder</code>
     */
    protected JMSCommandResponder createResponder() {
        return new StatusOnlyResponder(getGeneratedCommand());
    }

    protected void assertMatches(HttpServletRequest request) {
        if (!matches(request.getPathInfo()))
            throw new IllegalArgumentException(strings().getString(HJBStrings.REQUEST_PATH_IS_NOT_VALID,
                                                                   request.getPathInfo(),
                                                                   this));
    }

    protected void assertRootIsNotNull(HJBRoot root)
            throws IllegalArgumentException {
        if (null == root)
            throw new IllegalArgumentException(strings().needsANonNull(HJBRoot.class));
    }

    protected void assertRequestIsNotNull(HttpServletRequest request)
            throws IllegalArgumentException {
        if (null == request)
            throw new IllegalArgumentException(strings().needsANonNull(HttpServletRequest.class));
    }

    protected String applyURLDecoding(String s) {
        try {
            return URLDecoder.decode(s, HJBServletConstants.JAVA_CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            String message = strings().getString(HJBStrings.ENCODING_NOT_SUPPORTED,
                                                 HJBServletConstants.JAVA_CHARSET_UTF8);
            throw new HJBException(message);
        }
    }

    protected ParameterMapDecoder getDecoder() {
        return PARAMETER_DECODER;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    protected JMSArgumentFinder getFinder() {
        return FINDER;
    }

    private transient JMSCommandRunner assignedCommandRunner;

    private static final ParameterMapDecoder PARAMETER_DECODER = new ParameterMapDecoder();
    private static final HJBStrings STRINGS = new HJBStrings();
    private static final JMSArgumentFinder FINDER = new JMSArgumentFinder();
}
