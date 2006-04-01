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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jms.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;
import hjb.msg.codec.OrderedTypedValueCodec;

public class MapMessageCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new MapMessageCopier(),
                     new MapMessageCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new MapMessageCopier(),
                      new Object());
    }

    public void testCopyToHJBMessageThrowsHJBExceptionOnJMSException() {
/*        Mock mockMapMessage = mock(MapMessage.class);
        mockMapMessage.expects(once()).method("getText").will(throwException(new JMSException("thrown as a test")));
        expectsJmsGetAttributes(mockMapMessage);
        Message testMessage = (MapMessage) mockMapMessage.proxy();

        HJBMessage testMimeMessage = new HJBMessage(createEmptyTextHJBMessage(),
                                                    "test text");
        MapMessageCopier c = new MapMessageCopier();
        try {
            c.copyToHJBMessage(testMessage, testMimeMessage);
            fail("should have thrown an exception");
        } catch (HJBException e) {}
*/    }

    protected Map createEmptyTextHJBMessage() {
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_CLASS,
                    MapMessage.class.getName());
        return headers;
    }

    protected void expectsJmsGetAttributes(Mock aMock) {
        OrderedTypedValueCodec codec = new OrderedTypedValueCodec();
        for (int i = 0; i < NON_DESTINATION_ATTRIBUTES.length; i++) {
            aMock.expects(atLeastOnce()).method(NON_DESTINATION_ATTRIBUTES[i][1]).will(returnValue(codec.decode(NON_DESTINATION_ATTRIBUTES[i][3])));
        }
        Mock destinationMock = mock(Destination.class);
        Destination testDestination = (Destination) destinationMock.proxy();
        for (int i = 0; i < DESTINATION_ATTRIBUTES.length; i++) {
            aMock.expects(atLeastOnce()).method(DESTINATION_ATTRIBUTES[i][1]).will(returnValue(testDestination));
        }
        aMock.expects(atLeastOnce()).method("getPropertyNames").will(returnValue(Collections.enumeration(Collections.EMPTY_LIST)));
    }

    public static final String[][] NON_DESTINATION_ATTRIBUTES = AttributeCopierAssistantTest.NON_DESTINATION_ATTRIBUTES;
    public static final String[][] DESTINATION_ATTRIBUTES = AttributeCopierAssistantTest.DESTINATION_ATTRIBUTES;
}
