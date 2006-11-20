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
import javax.jms.StreamMessage;

import hjb.misc.HJBException;
import hjb.msg.codec.ByteArrayCodec;

public class StreamMessageByteArrayValueCopier extends StreamMessageValueCopier {

    public StreamMessageByteArrayValueCopier(LinkedList valuesRead) {
        super(new ByteArrayCodec(), valuesRead);
        setReadBufferSize(DEFAULT_READ_BUFFER_SIZE);
    }

    public void addToMessage(String name, String encodedValue, Message message)
            throws HJBException {
        try {
            asAStreamMessage(message).writeBytes(decodeAsByteArray(encodedValue));
        } catch (JMSException e) {
            handleValueWriteFailure(name, encodedValue, e, message);
        }
    }

    public boolean canBeEncoded(String name, Message message)
            throws HJBException {
        LinkedList values = getValuesRead();
        synchronized (values) {
            try {
                values.add(readAllBytes(asAStreamMessage(message)));
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
                    return encode(readAllBytes(asAStreamMessage(message)));
                } else {
                    verifyNextValueIsA(byte[].class);
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

    protected byte[] readAllBytes(StreamMessage message) throws JMSException {
        LinkedList values = getValuesRead();
        synchronized (values) {
            int bufferSize = getReadBufferSize();
            byte[] buffer = new byte[bufferSize];
            int numberRead = message.readBytes(buffer);
            byte[] result = new byte[0];
            while (-1 != numberRead) {
                byte[] newresult = new byte[result.length + numberRead];
                System.arraycopy(result, 0, newresult, 0, result.length);
                System.arraycopy(buffer,
                                 0,
                                 newresult,
                                 result.length,
                                 numberRead);
                result = newresult;
                numberRead = message.readBytes(buffer);
            }
            return result;
        }
    }

    protected char decodeAsChar(String value) throws HJBException {
        return ((Character) decode(value)).charValue();
    }

    protected byte[] decodeAsByteArray(String value) throws HJBException {
        return (byte[]) decode(value);
    }

    protected int getReadBufferSize() {
        return readBufferSize;
    }

    protected void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    private int readBufferSize;
    protected static final int DEFAULT_READ_BUFFER_SIZE = 8192;
}
