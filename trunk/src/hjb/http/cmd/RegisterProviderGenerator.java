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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import hjb.jms.HJBRoot;
import hjb.jms.cmd.JMSCommand;
import hjb.jms.cmd.RegisterProvider;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>RegisterProviderGenerator</code> is the
 * <code>JMSCommandGenerator</code> that produces {@link RegisterProvider}.
 * 
 * @see hjb.http.cmd.JMSCommandGenerator
 * @see hjb.jms.cmd.JMSCommand
 * 
 * @author Tim Emiola
 */
public class RegisterProviderGenerator extends PatternMatchingCommandGenerator {

    public JMSCommand getGeneratedCommand() throws HJBException {
        if (null == generatedCommand)
            throw new IllegalStateException(strings().getString(HJBStrings.JMS_COMMAND_NOT_GENERATED));
        return generatedCommand;
    }

    protected void constructCommandFrom(HttpServletRequest request, HJBRoot root) {
        Matcher m = getPathPattern().matcher(request.getPathInfo());
        m.matches();
        String providerName = m.group(1);

        Map decodedParameters = getDecoder().decode(request.getParameterMap());
        this.generatedCommand = new RegisterProvider(root,
                                                     getFinder().findEnvironment(decodedParameters,
                                                                                 providerName));
    }

    protected Pattern getPathPattern() {
        return PATH_MATCHER;
    }

    private transient RegisterProvider generatedCommand;

    private static final Pattern PATH_MATCHER = Pattern.compile("^/(\\w+)/register");
}
