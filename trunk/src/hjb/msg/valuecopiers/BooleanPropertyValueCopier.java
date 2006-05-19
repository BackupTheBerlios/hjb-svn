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
package hjb.msg.valuecopiers;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.codec.BooleanCodec;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.log4j.Logger;

public class BooleanPropertyValueCopier extends BaseEncodedValueCopier {

    public BooleanPropertyValueCopier() {
        super(new BooleanCodec());
    }

    public void addToMessage(String name, String encodedValue, Message message)
            throws HJBException {
        try {
            message.setBooleanProperty(name, decodeAsBoolean(encodedValue));
        } catch (JMSException e) {
            handleValueWriteFailure(name, encodedValue, e, message);
        } catch (IllegalArgumentException e) {
            handleValueWriteFailure(name, encodedValue, e, message);
        }
    }

    public boolean canBeEncoded(String name, Message message)
            throws HJBException {
        try {
            String asString = message.getStringProperty(name);
            return isReallyABoolean(asString);
        } catch (JMSException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getAsEncodedValue(String name, Message message)
            throws HJBException {
        try {
            String asString = message.getStringProperty(name);
            if (isReallyABoolean(asString)) {
                return encode(new Boolean(message.getBooleanProperty(name)));
            } else {
                return handleInvalidBooleanFailure(name, asString);
            }
        } catch (JMSException e) {
            return handleValueReadFailure(name, e, message);
        } catch (NumberFormatException e) {
            return handleValueReadFailure(name, e, message);
        }
    }

    protected boolean decodeAsBoolean(String value) throws HJBException {
        return ((Boolean) decode(value)).booleanValue();
    }

    protected boolean isReallyABoolean(String asString) {
        return Boolean.FALSE.toString().equals(asString)
                || Boolean.TRUE.toString().equals(asString);
    }

    protected String handleInvalidBooleanFailure(String name, String value) {
        String errorMessage = strings().getString(HJBStrings.COULD_NOT_READ_BOOLEAN_VALUE_FROM_MESSAGE,
                                                  name,
                                                  value);
        LOG.error(errorMessage);
        throw new HJBException(errorMessage);
    }

    private static final Logger LOG = Logger.getLogger(BooleanPropertyValueCopier.class);

}
