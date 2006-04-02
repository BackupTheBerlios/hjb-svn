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

import hjb.misc.HJBException;
import hjb.msg.codec.ByteCodec;

public class MapMessageByteValueCopier extends MapMessageValueCopier {

    public MapMessageByteValueCopier() {
        super(new ByteCodec());
    }

    public void addToMessage(String name, String encodedValue, Message message)
            throws HJBException {
        try {
            asAMapMessage(message).setByte(name, decodeAsByte(encodedValue));
        } catch (JMSException e) {
            handleValueWriteFailure(name, encodedValue, e, message);
        }
    }

    public boolean canBeEncoded(String name, Message message)
            throws HJBException {
        try {
            asAMapMessage(message).getByte(name);
            return true;
        } catch (JMSException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getAsEncodedValue(String name, Message message)
            throws HJBException {
        try {
            return encode(new Byte(asAMapMessage(message).getByte(name)));
        } catch (JMSException e) {
            return handleValueReadFailure(name, e, message);
        } catch (NumberFormatException e) {
            return handleValueReadFailure(name, e, message);
        }
    }

    protected byte decodeAsByte(String value) throws HJBException {
        return ((Byte) decode(value)).byteValue();
    }

}
