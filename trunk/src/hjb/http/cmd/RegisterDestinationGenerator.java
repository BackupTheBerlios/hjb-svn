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

import hjb.jms.HJBProvider;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.JMSCommand;
import hjb.jms.cmd.RegisterDestination;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>RegisterDestinationGenerator</code> is the
 * <code>JMSCommandGenerator</code> that produces {@link RegisterDestination}.
 * 
 * @see hjb.http.cmd.JMSCommandGenerator
 * @see hjb.jms.cmd.JMSCommand
 * 
 * @author Tim Emiola
 */
public class RegisterDestinationGenerator extends
        PatternMatchingCommandGenerator {

    public JMSCommand getGeneratedCommand() throws HJBException {
        if (null == generatedCommand)
            throw new IllegalStateException(strings().getString(HJBStrings.JMS_COMMAND_NOT_GENERATED));
        return generatedCommand;
    }

    protected void constructCommandFrom(HttpServletRequest request, HJBRoot root) {
        Matcher m = getPathPattern().matcher(request.getPathInfo());
        m.matches();
        String providerName = m.group(1);
        String destinationName = applyURLDecoding(m.group(2));

        HJBProvider provider = root.getProvider(providerName);
        if (null == provider)
            handleMissingComponent(request.getPathInfo(), providerName);

        this.generatedCommand = new RegisterDestination(provider,
                                                        destinationName);
    }

    protected Pattern getPathPattern() {
        return PATH_MATCHER;
    }

    private transient RegisterDestination generatedCommand;

    private static final Pattern PATH_MATCHER = Pattern.compile("^/(\\w+)/([^/]+)/register-destination$");
}
