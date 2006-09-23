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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import hjb.jms.HJBConnection;
import hjb.jms.HJBMessenger;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.JMSCommand;
import hjb.jms.cmd.ViewQueue;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.misc.PathNaming;

/**
 * <code>ViewQueueGenerator</code> is the <code>JMSCommandGenerator</code>
 * that produces {@link ViewQueue}.
 * 
 * @see hjb.http.cmd.JMSCommandGenerator
 * @see hjb.jms.cmd.JMSCommand
 * 
 * @author Tim Emiola
 */
public class ViewQueueGenerator extends PatternMatchingCommandGenerator {

    public JMSCommand getGeneratedCommand() throws HJBException {
        if (null == generatedCommand)
            throw new IllegalStateException(strings().getString(HJBStrings.JMS_COMMAND_NOT_GENERATED));
        return generatedCommand;
    }

    protected void constructCommandFrom(HttpServletRequest request, HJBRoot root)
            throws HJBException {
        String pathInfo = request.getPathInfo();
        Matcher m = getPathPattern().matcher(pathInfo);
        m.matches();
        String providerName = m.group(1);
        String factoryName = applyURLDecoding(m.group(2));
        int connectionIndex = Integer.parseInt(m.group(3));
        int sessionIndex = Integer.parseInt(m.group(4));
        int browserIndex = Integer.parseInt(m.group(5));

        HJBTreeWalker walker = new HJBTreeWalker(root, pathInfo);
        HJBConnection connection = walker.findConnection(providerName,
                                                         factoryName,
                                                         connectionIndex);
        this.generatedCommand = new ViewQueue(new HJBMessenger(connection,
                                                               sessionIndex),
                                              browserIndex);
        setAssignedCommandRunner(connection.getSessionCommandRunner(sessionIndex));
    }

    protected JMSCommandResponder createResponder() {
        return new ContentWritingResponder(getGeneratedCommand(),
                                           new HJBMessageWriter().asText(generatedCommand.getMessagesOnQueue()));
    }

    protected Pattern getPathPattern() {
        return PATH_MATCHER;
    }

    protected boolean useRootCommandRunner() {
        return false;
    }

    private transient ViewQueue generatedCommand;

    private static final Pattern PATH_MATCHER = Pattern.compile("^/(\\w+)/(.+)/"
            + PathNaming.CONNECTION
            + "-(\\d+)/"
            + PathNaming.SESSION
            + "-(\\d+)/" + PathNaming.BROWSER + "-(\\d+)/view$");
}
