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

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.log4j.Logger;

import hjb.misc.BufferSizeConfiguration;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.codec.ByteArrayCodec;

/**
 * <code>BytesMessageCopier</code> is the <code>MessageCopier</code> for
 * <code>BytesMessages</code>.
 * 
 * <p />
 * The message body is copied between the JMS and HJB representation by applying
 * Base64 encoding/decoding to the all bytes in the <code>BytesMessage</code>.
 * 
 * Encoding and decoding is performed using {@link hjb.msg.codec.ByteArrayCodec}.
 * It is an error if the body of the HJB message representing a
 * <code>BytesMessage</code> does not conform to the encoding output produced
 * by that class.
 * 
 * @author Tim Emiola
 */
public class BytesMessageCopier extends PayloadMessageCopier {

    public BytesMessageCopier() {
        this.codec = new ByteArrayCodec();
        setReadBufferSize(new BufferSizeConfiguration().getBufferSize());
    }

    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException {
        super.copyToJMSMessage(source, target);
        copyPayLoadToJMSMessage(source, (BytesMessage) target);
    }

    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException {
        super.copyToHJBMessage(source, target);
        copyPayLoadToHJBMessage((BytesMessage) source, target);
    }

    protected void copyPayLoadToJMSMessage(HJBMessage source,
                                           BytesMessage target)
            throws HJBException {
        try {
            target.writeBytes((byte[]) getCodec().decode(source.getBody()));
        } catch (JMSException e) {
            String message = strings().getString(HJBStrings.COULD_NOT_WRITE_DECODED_BYTES);
            LOG.error(message, e);
            throw new HJBException(message, e);
        }
    }

    protected void copyPayLoadToHJBMessage(BytesMessage source,
                                           HJBMessage target)
            throws HJBException {
        try {
            target.setEntityBody(getCodec().encode(readAllBytes(source)));
        } catch (JMSException e) {
            String message = strings().getString(HJBStrings.COULD_NOT_READ_ALL_BYTES);
            LOG.error(message, e);
            throw new HJBException(message, e);
        }
    }

    protected byte[] readAllBytes(BytesMessage message) throws JMSException {
        int bufferSize = getReadBufferSize();
        byte[] buffer = new byte[bufferSize];
        int numberRead = message.readBytes(buffer);
        byte[] result = new byte[0];
        while (-1 != numberRead) {
            byte[] newresult = new byte[result.length + numberRead];
            System.arraycopy(result, 0, newresult, 0, result.length);
            System.arraycopy(buffer, 0, newresult, result.length, numberRead);
            result = newresult;
            numberRead = message.readBytes(buffer);
        }
        return result;
    }

    protected boolean acceptJMSMessage(Message jmsMessage) throws HJBException {
        return jmsMessage instanceof BytesMessage;
    }

    protected boolean acceptHJBMessage(HJBMessage aMessage) throws HJBException {
        return BytesMessage.class.getName()
            .equals(aMessage.getHeader(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE));
    }

    protected ByteArrayCodec getCodec() {
        return codec;
    }

    protected int getReadBufferSize() {
        return readBufferSize;
    }

    protected void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    private ByteArrayCodec codec;
    private int readBufferSize;

    private static final Logger LOG = Logger.getLogger(BytesMessageCopier.class);
}
