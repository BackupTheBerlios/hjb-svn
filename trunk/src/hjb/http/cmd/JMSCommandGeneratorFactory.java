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

/**
 * <code>JMSCommandGeneratorFactorys</code> are used to instantiate the various
 * {@link hjb.http.cmd.JMSCommandGenerator}s used by the service methods of
 * {@link hjb.http.HJBServlet}.
 * 
 * @author Tim Emiola
 */
public interface JMSCommandGeneratorFactory {

    /**
     * Returns a set of <code>JMSCommandGenerator</code> instances that
     * match expected HJB requests.
     * 
     * @return an array containing new instances of
     *         <code>JMSCommandGenerator</code> to be used to process HJB requests.
     */

    public JMSCommandGenerator[] getGenerators();   
}
