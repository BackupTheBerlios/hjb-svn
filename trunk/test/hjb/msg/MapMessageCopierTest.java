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

import java.util.*;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.http.cmd.HJBMessageWriter;
import hjb.misc.HJBException;
import hjb.msg.codec.CodecTestValues;
import hjb.msg.valuecopiers.mapmessage.OrderedMapMessageValueCopierTest;
import hjb.testsupport.MessageAttributeInvoker;

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

    public void testCopyToJMSMessageThrowsHJBExceptionOnJMSException() {
        for (int i = 0; i < ORDERED_MAP_MESSAGE_METHODS.length; i++) {
            Mock mockJMSMessage = mock(MapMessage.class, "test" + i);
            mockJMSMessage.stubs()
                .method("setStringProperty")
                .with(eq("hjb_message_version"), eq("1.0"));
            String methodName = ORDERED_MAP_MESSAGE_METHODS[i][1];
            mockJMSMessage.stubs()
                .method(methodName)
                .will(throwException(new JMSException("thrown as a test")));
            for (int j = 0; j < ORDERED_MAP_MESSAGE_METHODS.length; j++) {
                if (j == i) continue;
                String okMethod = ORDERED_MAP_MESSAGE_METHODS[j][1];
                mockJMSMessage.stubs().method(okMethod);
            }

            Message testJMSMessage = (MapMessage) mockJMSMessage.proxy();
            HJBMessage testHJBMessage = new HJBMessage(createEmptyHJBMapMessageHeaders(),
                                                       "test text");
            MapMessageCopier c = new MapMessageCopier();
            try {
                c.copyToJMSMessage(testHJBMessage, testJMSMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testCopyToJMSMessagesCopiesMapValuesOK() {
        Mock mockJMSMessage = mock(MapMessage.class);
        mockJMSMessage.expects(once())
            .method("setStringProperty")
            .with(eq("hjb_message_version"), eq("1.0"));
        for (int i = 0; i < ORDERED_MAP_MESSAGE_METHODS.length; i++) {
            String methodName = ORDERED_MAP_MESSAGE_METHODS[i][1];
            Object expectedValue = ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][0];
            mockJMSMessage.expects(atLeastOnce())
                .method(methodName)
                .with(eq("test-" + methodName), eq(expectedValue));
        }

        Message testJMSMessage = (MapMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = createTestHJBMapMessage();
        System.err.println(testHJBMessage);
        MapMessageCopier c = new MapMessageCopier();
        c.copyToJMSMessage(testHJBMessage, testJMSMessage);

        verify();
    }

    public void testCopyToHJBMessageThrowsHJBExceptionOnJMSException() {
        Mock mockJMSMessage = mock(MapMessage.class);
        for (int i = 0; i < ORDERED_MAP_MESSAGE_METHODS.length; i++) {
            String methodName = ORDERED_MAP_MESSAGE_METHODS[i][0];
            mockJMSMessage.stubs()
                .method(methodName)
                .will(throwException(new JMSException("thrown as a test")));
        }
        Enumeration anyNameEnumeration = Collections.enumeration(Arrays.asList(new String[] {
            "testMapName"
        }));
        mockJMSMessage.stubs()
            .method("getMapNames")
            .will(returnValue(anyNameEnumeration));
        attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);

        try {
            MapMessage testJMSMessage = (MapMessage) mockJMSMessage.proxy();
            MapMessageCopier c = new MapMessageCopier();
            c.copyToHJBMessage(testJMSMessage,
                               new HJBMessage(createEmptyHJBMapMessageHeaders(),
                                              ""));
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    public void testCopyToHJBMessageCopiesMapValuesOK() throws Exception {
        Mock mockJMSMessage = mock(MapMessage.class);

        attributeInvoker.stubAllPropertyGettersFor(mockJMSMessage);
        Map expectedValues = new HashMap();
        for (int i = 0; i < ORDERED_MAP_MESSAGE_METHODS.length; i++) {
            String methodName = ORDERED_MAP_MESSAGE_METHODS[i][0];
            Object methodResult = ORDERED_EXPECTED_DECODED_VALUES_OBJECTS[i][0];
            String keyName = "test-" + methodName;
            mockJMSMessage.stubs()
                .method(methodName)
                .with(eq(keyName))
                .will(returnValue(methodResult));
            mockJMSMessage.stubs()
                .method(methodName)
                .with(not(eq(keyName)))
                .will(throwException(new JMSException("thrown as a test")));
            expectedValues.put(keyName,
                               ORDERED_OK_EXPECTED_DECODED_VALUES[i][0]);
        }
        mockJMSMessage.stubs()
            .method("getMapNames")
            .will(returnValue(Collections.enumeration(expectedValues.keySet())));

        MapMessage testJMSMessage = (MapMessage) mockJMSMessage.proxy();
        HJBMessage testHJBMessage = createTestHJBMapMessage();
        MapMessageCopier c = new MapMessageCopier();
        c.copyToHJBMessage(testJMSMessage, testHJBMessage);
        assertExpectedValuesAreEncodedInTheMessage(testHJBMessage,
                                                   expectedValues);
    }

    protected void assertExpectedValuesAreEncodedInTheMessage(HJBMessage testHJBMessage,
                                                              Map expectedValues)
            throws Exception {
        String mapText = testHJBMessage.getBody();
        assertEquals("should contain the same values",
                     expectedValues,
                     new HJBMessageWriter().asMap(mapText));
    }

    protected Map createEmptyHJBMapMessageHeaders() {
        Map headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE,
                    MapMessage.class.getName());
        return headers;
    }

    protected HJBMessage createTestHJBMapMessage() {
        Map headers = createEmptyHJBMapMessageHeaders();
        Map encodedValues = new HashMap();
        for (int i = 0; i < ORDERED_MAP_MESSAGE_METHODS.length; i++) {
            String methodName = ORDERED_MAP_MESSAGE_METHODS[i][1];
            Object methodResult = ORDERED_OK_EXPECTED_DECODED_VALUES[i][0];
            String keyName = "test-" + methodName;
            encodedValues.put(keyName, methodResult);
        }
        return new HJBMessage(headers,
                              new HJBMessageWriter().asText(encodedValues));
    }

    protected void setUp() {
        attributeInvoker = new MessageAttributeInvoker();
    }

    private MessageAttributeInvoker attributeInvoker;

    private static final Object[][] ORDERED_EXPECTED_DECODED_VALUES_OBJECTS = CodecTestValues.ORDERED_EXPECTED_DECODED_VALUES_OBJECTS;
    private static final String[][] ORDERED_OK_EXPECTED_DECODED_VALUES = CodecTestValues.ORDERED_OK_EXPECTED_DECODED_VALUES;
    public static final String[][] ORDERED_MAP_MESSAGE_METHODS = OrderedMapMessageValueCopierTest.ORDERED_MAP_MESSAGE_METHODS;
}
