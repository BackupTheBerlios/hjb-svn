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
import hjb.msg.codec.ByteCodec;

public class StreamMessageByteValueCopier extends StreamMessageValueCopier {

    public StreamMessageByteValueCopier(LinkedList valuesRead) {
        super(new ByteCodec(), valuesRead);
    }

    public void addToMessage(String name, String encodedValue, Message message)
            throws HJBException {
        try {
            asAStreamMessage(message).writeByte(decodeAsByte(encodedValue));
        } catch (JMSException e) {
            handleValueWriteFailure(name, encodedValue, e, message);
        }
    }

    public boolean canBeEncoded(String name, Message message)
            throws HJBException {
        LinkedList values = getValuesRead();
        synchronized (values) {
            try {
                values.add(new Byte(asAStreamMessage(message).readByte()));
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
                    return encode(new Byte(asAStreamMessage(message).readByte()));
                } else {
                    verifyNextValueIsA(Byte.class);
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

    protected byte decodeAsByte(String value) throws HJBException {
        return ((Byte) decode(value)).byteValue();
    }

}
