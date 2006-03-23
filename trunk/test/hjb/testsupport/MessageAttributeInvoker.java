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
package hjb.testsupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.msg.AttributeCopierAssistant;
import hjb.msg.AttributeCopierAssistantTest;
import hjb.msg.codec.OrderedTypedValueCodec;

public class MessageAttributeInvoker {

    public void willInvokeGetPropertyNamesAndReturnEmptyHanded(Mock mockMessage,
                                           MockObjectTestCase test) {
        mockMessage.expects(test.atLeastOnce()).method("getPropertyNames").will(test.returnValue(Collections.enumeration(new ArrayList())));
    }

    public Map willInvokeAllAttributeAccessors(Mock mockMessage,
                                               MockObjectTestCase test) {
        for (int j = 0; j < NON_DESTINATION_ATTRIBUTES.length; j++) {
            mockMessage.expects(test.once()).method(NON_DESTINATION_ATTRIBUTES[j][1]).will(test.returnValue(new OrderedTypedValueCodec().decode(NON_DESTINATION_ATTRIBUTES[j][3])));
        }

        Map expectedHeaderValues = getOKEncodedPropertyValues();
        for (int k = 0; k < DESTINATION_ATTRIBUTES.length; k++) {
            Mock destinationMock = test.mock(Destination.class, "destination"
                    + k);
            Destination testDestination = (Destination) destinationMock.proxy();
            mockMessage.expects(test.once()).method(DESTINATION_ATTRIBUTES[k][1]).will(test.returnValue(testDestination));
            expectedHeaderValues.put(DESTINATION_ATTRIBUTES[k][0],
                                     "destination" + k);
        }
        return expectedHeaderValues;
    }

    public void willInvokeAttributeAccessorsInOrderUntilNthFails(Mock mockJms,
                                                                 int n,
                                                                 MockObjectTestCase test) {
        for (int l = 0; l < AttributeCopierAssistant.BUILT_IN_HEADERS.length; l++) {
            OrderedTypedValueCodec orderedCodec = new OrderedTypedValueCodec();

            if (AttributeCopierAssistant.BUILT_IN_HEADERS[l].equals(NON_DESTINATION_ATTRIBUTES[n][0])) {
                break;
            }

            for (int j = 0; j < NON_DESTINATION_ATTRIBUTES.length; j++) {
                if (j == n) continue;

                if (AttributeCopierAssistant.BUILT_IN_HEADERS[l].equals(NON_DESTINATION_ATTRIBUTES[j][0])) {
                    mockJms.expects(test.once()).method(NON_DESTINATION_ATTRIBUTES[j][1]).will(test.returnValue(orderedCodec.decode(NON_DESTINATION_ATTRIBUTES[j][3])));
                    break;
                }
            }

            for (int k = 0; k < DESTINATION_ATTRIBUTES.length; k++) {
                if (AttributeCopierAssistant.BUILT_IN_HEADERS[l].equals(DESTINATION_ATTRIBUTES[k][0])) {
                    Mock destinationMock = test.mock(Destination.class,
                                                     "destination"
                                                             + (n * 100 + k));
                    Destination testDestination = (Destination) destinationMock.proxy();
                    mockJms.expects(test.once()).method(DESTINATION_ATTRIBUTES[k][1]).will(test.returnValue(testDestination));
                    break;
                }
            }
        }
    }

    public Map getOKEncodedPropertyValues() {
        Map result = new HashMap();
        for (int i = 0; i < NON_DESTINATION_ATTRIBUTES.length; i++) {
            result.put(NON_DESTINATION_ATTRIBUTES[i][0],
                       NON_DESTINATION_ATTRIBUTES[i][3]);
        }
        for (int k = 0; k < DESTINATION_ATTRIBUTES.length; k++) {
            result.put(DESTINATION_ATTRIBUTES[k][0], "destination" + k);
        }
        return result;
    }

    public static final String[][] NON_DESTINATION_ATTRIBUTES = AttributeCopierAssistantTest.NON_DESTINATION_ATTRIBUTES;
    public static final String[][] DESTINATION_ATTRIBUTES = AttributeCopierAssistantTest.DESTINATION_ATTRIBUTES;
}
