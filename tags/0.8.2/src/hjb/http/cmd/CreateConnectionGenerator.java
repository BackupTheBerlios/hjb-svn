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

import java.text.MessageFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import hjb.http.HJBServletConstants;
import hjb.jms.HJBConnectionFactory;
import hjb.jms.HJBRoot;
import hjb.jms.cmd.CreateConnection;
import hjb.jms.cmd.JMSCommand;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>CreateConnectionGenerator</code> is the
 * <code>JMSCommandGenerator</code> that produces {@link CreateConnection}.
 * 
 * @see hjb.http.cmd.JMSCommandGenerator
 * @see hjb.jms.cmd.JMSCommand
 * 
 * @author Tim Emiola
 */
public class CreateConnectionGenerator extends PatternMatchingCommandGenerator {

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

        String formatterText = FORMATTER_GENERATOR.format(new Object[] {
                request.getContextPath(),
                request.getServletPath(),
                providerName,
                factoryName,
        });
        this.createdLocationFormat = new MessageFormat(formatterText);

        HJBTreeWalker walker = new HJBTreeWalker(root, pathInfo);
        HJBConnectionFactory factory = walker.findConnectionFactory(providerName,
                                                                    factoryName);

        // get the username and password from the decoded parameters, then
        // create the JMSCommand
        Map decodedParameters = getDecoder().decode(request.getParameterMap());
        Object rawUsername = decodedParameters.get(HJBServletConstants.CONNECTION_USERNAME);
        String username = null == rawUsername ? null : "" + rawUsername;
        Object rawPassword = decodedParameters.get(HJBServletConstants.CONNECTION_PASSWORD);
        String password = null == rawPassword ? null : "" + rawPassword;
        this.generatedCommand = new CreateConnection(factory,
                                                     username,
                                                     password,
                                                     getFinder().findClientId(decodedParameters));
    }

    protected JMSCommandResponder createResponder() {
        return new ResourceCreationResponder(getGeneratedCommand(),
                                             getCreatedLocationFormatter().format(new Object[] {
                                                 new Integer(generatedCommand.getConnectionIndex())
                                             }));
    }

    protected Pattern getPathPattern() {
        return PATH_MATCHER;
    }

    public MessageFormat getCreatedLocationFormatter() {
        return createdLocationFormat;
    }

    private transient MessageFormat createdLocationFormat;
    private transient CreateConnection generatedCommand;
    private static final Pattern PATH_MATCHER = Pattern.compile("^/(\\w+)/(?!.*?"
            + PathNaming.CONNECTION + "-\\d+)(.+)/create$");
    private static final MessageFormat FORMATTER_GENERATOR = new MessageFormat("{0}{1}/{2}/{3}/"
            + PathNaming.CONNECTION + "-'{0}'");
}
