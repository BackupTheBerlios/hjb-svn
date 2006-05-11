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
import javax.jms.MessageEOFException;

import hjb.misc.HJBException;
import hjb.msg.codec.LongCodec;

public class StreamMessageLongValueCopier extends StreamMessageValueCopier {

    public StreamMessageLongValueCopier(LinkedList valuesRead) {
        super(new LongCodec(), valuesRead);
    }

    public void addToMessage(String name, String encodedValue, Message message)
            throws HJBException {
        try {
            asAStreamMessage(message).writeLong(decodeAsLong(encodedValue));
        } catch (JMSException e) {
            handleValueWriteFailure(name, encodedValue, e, message);
        }
    }

    public boolean canBeEncoded(String name, Message message)
            throws HJBException {
        LinkedList values = getValuesRead();
        synchronized (values) {
            try {
                values.add(new Long(asAStreamMessage(message).readLong()));
                return true;
            } catch (MessageEOFException e) {
                throw new IllegalStateException();
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
                    return encode(new Long(asAStreamMessage(message).readLong()));
                } else {
                    verifyNextValueIsA(Long.class);
                    return encode(values.removeFirst());
                }
            } catch (MessageEOFException e) {
                throw new IllegalStateException();
            } catch (JMSException e) {
                return handleValueReadFailure(name, e, message);
            } catch (NumberFormatException e) {
                return handleValueReadFailure(name, e, message);
            }
        }
    }

    protected long decodeAsLong(String value) throws HJBException {
        return ((Long) decode(value)).longValue();
    }

}
