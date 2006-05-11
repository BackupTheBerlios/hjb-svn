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
package hjb.msg;

import java.util.HashMap;
import java.util.Map;

import hjb.http.cmd.HJBMessageWriter;

/**
 * <code>HJBMessage</code> serves as an intermediate representation of a
 * <code>JMS</code> message.
 * 
 * <p />
 * <code>HJBMessage</code> is a container for the headers and entity body HJB
 * sends/receives over HTTP as a representation of a JMS message
 * 
 * @author Tim Emiola
 */
public class HJBMessage {

    public HJBMessage(Map headers, String messageAsText) {
        this.headers = new HashMap(headers);
        this.messageAsText = messageAsText;
    }

    public String getBody() {
        return messageAsText;
    }

    public void setEntityBody(String entityBody) {
        this.messageAsText = entityBody;
    }

    public Map getHeaders() {
        return new HashMap(headers);
    }

    public String getHeader(String name) {
        return (String) headers.get(name);
    }

    public String addHeader(String name, String value) {
        return (String) headers.put(name, value);
    }

    public String toString() {
        return new HJBMessageWriter().asText(this);
    }

    private Map headers;
    private String messageAsText;
}
