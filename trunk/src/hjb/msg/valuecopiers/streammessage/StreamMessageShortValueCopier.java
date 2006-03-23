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
package hjb.msg.valuecopiers.streammessage;

import java.util.LinkedList;

import javax.jms.JMSException;
import javax.jms.Message;

import hjb.misc.HJBException;
import hjb.msg.codec.ShortCodec;

public class StreamMessageShortValueCopier extends StreamMessageValueCopier {

    public StreamMessageShortValueCopier(LinkedList valuesRead) {
        super(new ShortCodec(), valuesRead);
    }

    public void addToMessage(String name, String encodedValue, Message message)
            throws HJBException {
        try {
            asAStreamMessage(message).writeShort(decodeAsShort(encodedValue));
        } catch (JMSException e) {
            handleValueWriteFailure(name, encodedValue, e);
        }
    }

    public boolean canBeEncoded(String name, Message message)
            throws HJBException {
        LinkedList values = getValuesRead();
        synchronized (values) {
            try {
                values.add(new Short(asAStreamMessage(message).readShort()));
                return true;
            } catch (JMSException e) {
                return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    public String getAsEncodedValue(String name, Message message)
            throws HJBException {
        LinkedList values = getValuesRead();
        synchronized (values) {
            try {
                if (0 == values.size()) {
                    return encode(new Short(asAStreamMessage(message).readShort()));
                } else {
                    verifyNextValueIsA(Short.class);
                    return encode(values.removeFirst());
                }
            } catch (JMSException e) {
                return handleValueReadFailure(name, e);
            } catch (NumberFormatException e) {
                return handleValueReadFailure(name, e);
            }
        }
    }

    protected short decodeAsShort(String value) throws HJBException {
        return ((Short) decode(value)).shortValue();
    }

}
