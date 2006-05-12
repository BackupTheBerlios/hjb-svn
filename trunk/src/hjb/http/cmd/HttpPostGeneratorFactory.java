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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <code>HttpPostGeneratorFactory</code> is used to instantiate the various
 * {@link hjb.http.cmd.JMSCommandGenerator}s used by
 * {@link hjb.http.HJBServlet#doPost(HttpServletRequest, HttpServletResponse)}.
 * 
 * @author Tim Emiola
 */
public class HttpPostGeneratorFactory implements JMSCommandGeneratorFactory {

    /**
     * Returns the set of <code>JMSCommandGenerator</code> instances that
     * match all expected POST requests.
     * 
     * <strong>N.B</strong> the order in which the generators are returned
     * <strong>is</strong> is significant as multiple generators may match the
     * same URL.
     * 
     * @return an array containing new instances of
     *         <code>JMSCommandGenerator</code> to be used to process HJB POST
     *         requests.
     */
    public JMSCommandGenerator[] getGenerators() {
        JMSCommandGenerator[] result = new JMSCommandGenerator[] {
                new RegisterProviderGenerator(),
                new CreateConsumerGenerator(),
                new ReceiveFromConsumerGenerator(),
                new CreateSubscriberGenerator(),
                new ReceiveFromSubscriberGenerator(),
                new CreateProducerGenerator(),
                new SendHJBMessageGenerator(),
                new CreateBrowserGenerator(),
                new ViewQueueGenerator(),
                new CreateSessionGenerator(),
                new CreateConnectionGenerator(),
        };
        return result;
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof HttpPostGeneratorFactory)) return false;
        return this.getClass().getName().equals(o.getClass().getName());
    }

    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    public String toString() {
        String clazzName = this.getClass().getName();
        if (-1 == clazzName.lastIndexOf('.')) return "[" + clazzName + "]";
        return "[" + clazzName.substring(clazzName.lastIndexOf('.') + 1) + "]";
    }

}
