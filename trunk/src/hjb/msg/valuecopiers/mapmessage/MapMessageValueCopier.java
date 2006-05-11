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

import javax.jms.MapMessage;
import javax.jms.Message;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.codec.TypedValueCodec;
import hjb.msg.valuecopiers.BaseEncodedValueCopier;

public abstract class MapMessageValueCopier extends BaseEncodedValueCopier {

    public MapMessageValueCopier(TypedValueCodec codec) {
        super(codec);
    }

    public MapMessage asAMapMessage(Message aMessage) {
        if (!(aMessage instanceof MapMessage)) {
            String errorMessage = strings().getString(HJBStrings.INCORRECT_JMS_MESSAGE_TYPE,
                                                      this,
                                                      null == aMessage ? "null"
                                                              : aMessage.getClass()
                                                                  .getName());
            LOG.error(errorMessage);
            throw new HJBException(errorMessage);
        }
        return (MapMessage) aMessage;
    }

    private static final Logger LOG = Logger.getLogger(MapMessageValueCopier.class);
}
