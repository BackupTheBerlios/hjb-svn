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

import java.io.*;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.codec.ByteArrayCodec;

/**
 * <code>ObjectMessageCopier</code> is the <code>MessageCopier</code> for
 * <code>ObjectMessage</code>s.
 * 
 * <p />
 * The message body is copied between the JMS and HJB representation by applying
 * Base64 encoding/decoding to the byte representation of the serialized object
 * in <code>ObjectMessage</code>.
 * 
 * @author Tim Emiola
 */
public class ObjectMessageCopier extends PayloadMessageCopier {

    public ObjectMessageCopier() {
        this.codec = new ByteArrayCodec();
    }

    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException {
        super.copyToJMSMessage(source, target);
        copyPayLoadToJMSMessage(source, (ObjectMessage) target);
    }

    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException {
        super.copyToHJBMessage(source, target);
        copyPayLoadToHJBMessage((ObjectMessage) source, target);
    }

    protected void copyPayLoadToJMSMessage(HJBMessage source,
                                           ObjectMessage target)
            throws HJBException {
        try {
            byte[] decodedObject = (byte[]) getCodec().decode(source.getBody());
            target.setObject(asObject(decodedObject));
        } catch (JMSException e) {
            handleObjectCopyFailure(e);
        } catch (IOException e) {
            handleObjectCopyFailure(e);
        } catch (ClassNotFoundException e) {
            String message = strings().getString(HJBStrings.OBJECT_CLASS_MISSING);
            LOG.error(message, e);
            throw new HJBException(message, e);
        }
    }

    protected void copyPayLoadToHJBMessage(ObjectMessage source,
                                           HJBMessage target)
            throws HJBException {
        try {
            target.setEntityBody(getCodec().encode(asBytes(source.getObject())));
        } catch (IOException e) {
            handleObjectCopyFailure(e);
        } catch (JMSException e) {
            handleObjectCopyFailure(e);
        }
    }

    protected void handleObjectCopyFailure(Exception e) throws HJBException {
        String message = strings().getString(HJBStrings.COULD_NOT_COPY_OBJECT_MESSAGE);
        LOG.error(message, e);
        throw new HJBException(message, e);
    }

    protected boolean acceptJMSMessage(Message jmsMessage) throws HJBException {
        return jmsMessage instanceof ObjectMessage;
    }

    protected boolean acceptHJBMessage(HJBMessage aMessage) throws HJBException {
        return ObjectMessage.class.getName().equals(aMessage.getHeader(MessageCopierFactory.HJB_JMS_CLASS));
    }

    protected Serializable asObject(byte[] bytes) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream objectIn = new ObjectInputStream(in);
        return (Serializable) objectIn.readObject();
    }

    protected byte[] asBytes(Serializable object) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(out);
        objectOut.writeObject(object);
        return out.toByteArray();
    }

    protected ByteArrayCodec getCodec() {
        return codec;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private ByteArrayCodec codec;

    private static final Logger LOG = Logger.getLogger(ObjectMessageCopier.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
