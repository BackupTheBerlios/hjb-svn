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
package hjb.msg.valuecopiers.mapmessage;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.codec.StringCodec;

public class MapMessageStringValueCopier extends MapMessageValueCopier {

    public MapMessageStringValueCopier() {
        super(new StringCodec());
    }

    public void addToMessage(String name, String encodedValue, Message message)
            throws HJBException {
        try {
            asAMapMessage(message).setString(name, decodeAsString(encodedValue));
        } catch (JMSException e) {
            handleValueWriteFailure(name, encodedValue, e, message);
        } catch (IllegalArgumentException e) {
            handleValueWriteFailure(name, encodedValue, e, message);
	}
    }

    public boolean canBeEncoded(String name, Message message)
            throws HJBException {
        try {
            return null != asAMapMessage(message).getString(name);
        } catch (JMSException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getAsEncodedValue(String name, Message message)
            throws HJBException {
        try {
            String result = asAMapMessage(message).getString(name);
            if (null == result) {
                String errorMessage = strings().getString(HJBStrings.STRING_VALUE_WAS_NULL,
                                                          name);
                LOG.error(errorMessage);
                throw new HJBException(errorMessage);
            }
            return encode(result);
        } catch (JMSException e) {
            return handleValueReadFailure(name, e, message);
        } catch (NumberFormatException e) {
            return handleValueReadFailure(name, e, message);
        }
    }

    protected String decodeAsString(String value) throws HJBException {
        return (String) decode(value);
    }

    private static final Logger LOG = Logger.getLogger(MapMessageStringValueCopier.class);
}
