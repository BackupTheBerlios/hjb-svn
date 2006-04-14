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

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.misc.HJBException;

public class AttributeCopierAssistantTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new AttributeCopierAssistant(),
                     new AttributeCopierAssistant());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new AttributeCopierAssistant(),
                      new Object());
    }

    public void testIsBuiltinHeaderReturnsFalseForInvalidHeaders() {
        String[] invalidHeaders = new String[] {
                null, "foobar", "bazbutt", "hjb.notreally.header"
        };

        AttributeCopierAssistant a = new AttributeCopierAssistant();
        for (int i = 0; i < invalidHeaders.length; i++) {
            assertFalse(a.isBuiltinHeader(invalidHeaders[i]));
        }
    }

    public void testIsBuiltinHeaderReturnsTrueForValidHeaders() {
        AttributeCopierAssistant a = new AttributeCopierAssistant();
        for (int i = 0; i < AttributeCopierAssistant.BUILT_IN_HEADERS.length; i++) {
            assertTrue("should be true!",
                       a.isBuiltinHeader(AttributeCopierAssistant.BUILT_IN_HEADERS[i]));
        }
    }

    public void testIsDestinationTypeHeaderReturnsFalseForInvalidHeaders() {
        String[] invalidHeaders = new String[] {
                null, "foobar", "bazbutt", "hjb.notreally.header"
        };

        AttributeCopierAssistant a = new AttributeCopierAssistant();
        for (int i = 0; i < invalidHeaders.length; i++) {
            assertFalse(a.isBuiltinHeader(invalidHeaders[i]));
        }
    }

    public void testIsDestinationTypeHeaderReturnsTrueForDestinationTypeHeaders() {
        AttributeCopierAssistant a = new AttributeCopierAssistant();
        for (int i = 0; i < DESTINATION_ATTRIBUTES.length; i++) {
            assertTrue("should be true!",
                       a.isBuiltinHeader(DESTINATION_ATTRIBUTES[i][0]));
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnJMSException() {
        for (int j = 0; j < NON_DESTINATION_ATTRIBUTES.length; j++) {
            Mock mockMessage = mock(Message.class, "message" + j);
            mockMessage.stubs()
                .method(NON_DESTINATION_ATTRIBUTES[j][2])
                .will(throwException(new JMSException("thrown as test")));
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            try {
                a.addToMessage(NON_DESTINATION_ATTRIBUTES[j][0],
                               NON_DESTINATION_ATTRIBUTES[j][3],
                               testMessage);
                fail("should have thrown an exception");
            } catch (HJBException e) {}
        }
    }

    public void testAddToMessageThrowsHJBExceptionOnIncorrectEncodedHeader() {
        for (int j = 0; j < NON_DESTINATION_ATTRIBUTES.length; j++) {
            if (null == NON_DESTINATION_ATTRIBUTES[j][4]) continue;
            String[] incorrectEncodings = NON_DESTINATION_ATTRIBUTES[j][4].split(",");
            Mock mockMessage = mock(Message.class, "message" + j);
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            for (int i = 0; i < incorrectEncodings.length; i++) {
                try {
                    a.addToMessage(NON_DESTINATION_ATTRIBUTES[j][0],
                                   incorrectEncodings[i],
                                   testMessage);
                    fail("should have thrown an exception");

                } catch (HJBException e) {}
            }
        }
    }

    public void testAddToMessageAddsNothingForInvalidHJBHeaders() {
        String[] invalidHeaders = new String[] {
                null, "foobar", "bazbutt", "hjb.notreally.header"
        };
        Mock mockMessage = mock(Message.class);
        for (int j = 0; j < NON_DESTINATION_ATTRIBUTES.length; j++) {
            mockMessage.expects(never())
                .method(NON_DESTINATION_ATTRIBUTES[j][2]);
        }
        Message testMessage = (Message) mockMessage.proxy();

        AttributeCopierAssistant a = new AttributeCopierAssistant();
        for (int i = 0; i < invalidHeaders.length; i++) {
            a.addToMessage(invalidHeaders[i], "", testMessage);
        }
    }

    public void testAddToMessageAddsNothingForDestinationTypeHJBHeaders() {
        Mock mockMessage = mock(Message.class);
        String destinationTypeSetters[] = new String[] {
                "setJMSDestination", "setJMSReplyTo"
        };

        for (int j = 0; j < destinationTypeSetters.length; j++) {
            mockMessage.expects(never()).method(destinationTypeSetters[j]);
        }
        Message testMessage = (Message) mockMessage.proxy();

        AttributeCopierAssistant a = new AttributeCopierAssistant();
        for (int i = 0; i < DESTINATION_ATTRIBUTES.length; i++) {
            a.addToMessage(DESTINATION_ATTRIBUTES[i][0], "", testMessage);
        }
    }

    public void testAddToMessageAddsWithNoChangeForStringAttributeHJBHeaders() {
        for (int j = 0; j < JMS_STRING_ATTRIBUTES.length; j++) {
            Mock mockMessage = mock(Message.class, "message" + j);
            String testValue = "testValue" + j;

            mockMessage.expects(once())
                .method(JMS_STRING_ATTRIBUTES[j][2])
                .with(eq(testValue));
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            a.addToMessage(JMS_STRING_ATTRIBUTES[j][0], testValue, testMessage);
        }
    }

    public void testAddToMessageAddsRedeliveredAttributeCorrectly() {
        Mock mockMessage = mock(Message.class);
        boolean testValue = false;
        String encodedTestValue = "(boolean" + testValue + ")";

        mockMessage.expects(once())
            .method("setJMSRedelivered")
            .with(eq(testValue));
        Message testMessage = (Message) mockMessage.proxy();

        AttributeCopierAssistant a = new AttributeCopierAssistant();
        a.addToMessage(AttributeCopierAssistant.REDELIVERED,
                       encodedTestValue,
                       testMessage);
    }

    public void testAddToMessageAddsDecodedIntegerForIntegerAttributeHJBHeaders() {
        for (int j = 0; j < JMS_INT_ATTRIBUTES.length; j++) {
            Mock mockMessage = mock(Message.class, "message" + j);
            int testValue = j;
            String encodedTestValue = "(int " + j + ")";

            mockMessage.expects(once())
                .method(JMS_INT_ATTRIBUTES[j][2])
                .with(eq(testValue));
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            a.addToMessage(JMS_INT_ATTRIBUTES[j][0],
                           encodedTestValue,
                           testMessage);
        }
    }

    public void testAddToMessageAddsDecodedLongForLongAttributeHJBHeaders() {
        for (int j = 0; j < JMS_LONG_ATTRIBUTES.length; j++) {
            Mock mockMessage = mock(Message.class, "message" + j);
            long testValue = j;
            String encodedTestValue = "(long " + j + ")";

            mockMessage.expects(once())
                .method(JMS_LONG_ATTRIBUTES[j][2])
                .with(eq(testValue));
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            a.addToMessage(JMS_LONG_ATTRIBUTES[j][0],
                           encodedTestValue,
                           testMessage);
        }
    }

    public void testGetEncodedValueFromMessageThrowsHJBExceptionOnJMSException() {
        for (int j = 0; j < NON_DESTINATION_ATTRIBUTES.length; j++) {
            Mock mockMessage = mock(Message.class, "message" + j);
            mockMessage.stubs()
                .method(NON_DESTINATION_ATTRIBUTES[j][1])
                .will(throwException(new JMSException("thrown as test")));
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            try {
                a.getEncodedValueFromMessage(NON_DESTINATION_ATTRIBUTES[j][0],
                                             testMessage);
                fail("should have thrown an exception");

            } catch (HJBException e) {}
        }
    }

    public void testGetEncodedValueFromMessageReturnsToStringForDestinationTypeHJBHeaders() {
        String destinationTypeGetters[] = new String[] {
                "getJMSDestination", "getJMSReplyTo"
        };
        String destinationHJBHeaderNames[] = new String[] {
                AttributeCopierAssistant.DESTINATION,
                AttributeCopierAssistant.REPLY_TO
        };
        for (int j = 0; j < destinationTypeGetters.length; j++) {
            Mock mockMessage = mock(Message.class, "message" + j);
            Mock mockDestination = mock(Destination.class, "destination" + j);
            Destination testDestination = (Destination) mockDestination.proxy();

            mockMessage.stubs()
                .method(destinationTypeGetters[j])
                .will(returnValue(testDestination));
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            assertEquals("should have been the same",
                         testDestination.toString(),
                         a.getEncodedValueFromMessage(destinationHJBHeaderNames[j],
                                                      testMessage));
        }
    }

    public void testGetEncodedValueFromMessageReturnsForStringAttributeHJBHeaders() {
        for (int j = 0; j < JMS_STRING_ATTRIBUTES.length; j++) {
            Mock mockMessage = mock(Message.class, "message" + j);
            String testValue = "testValue" + j;

            mockMessage.stubs()
                .method(JMS_STRING_ATTRIBUTES[j][1])
                .will(returnValue(testValue));
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            assertEquals("should have been the same",
                         testValue,
                         a.getEncodedValueFromMessage(JMS_STRING_ATTRIBUTES[j][0],
                                                      testMessage));
        }
    }

    public void testGetEncodedValueFromMessageReturnsCorrectValueForRedelivered() {
        Mock mockMessage = mock(Message.class, "message");
        boolean testValue = false;

        mockMessage.stubs()
            .method("getJMSRedelivered")
            .will(returnValue(testValue));
        Message testMessage = (Message) mockMessage.proxy();

        AttributeCopierAssistant a = new AttributeCopierAssistant();
        assertEquals("should have been the same",
                     "(boolean " + testValue + ")",
                     a.getEncodedValueFromMessage(AttributeCopierAssistant.REDELIVERED,
                                                  testMessage));
    }

    public void testGetEncodedValueFromMessageReturnsEncodedLongForLongAttributeHJBHeaders() {
        for (int j = 0; j < JMS_LONG_ATTRIBUTES.length; j++) {
            Mock mockMessage = mock(Message.class, "message" + j);
            long testValue = 10 + j;

            mockMessage.stubs()
                .method(JMS_LONG_ATTRIBUTES[j][1])
                .will(returnValue(testValue));
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            assertEquals("should have been the same",
                         "(long " + testValue + ")",
                         a.getEncodedValueFromMessage(JMS_LONG_ATTRIBUTES[j][0],
                                                      testMessage));
        }
    }

    public void testGetEncodedValueFromMessageReturnsEncodedIntegerForIntegerAttributeHJBHeaders() {
        for (int j = 0; j < JMS_INT_ATTRIBUTES.length; j++) {
            Mock mockMessage = mock(Message.class, "message" + j);
            int testValue = 10 + j;

            mockMessage.stubs()
                .method(JMS_INT_ATTRIBUTES[j][1])
                .will(returnValue(testValue));
            Message testMessage = (Message) mockMessage.proxy();

            AttributeCopierAssistant a = new AttributeCopierAssistant();
            assertEquals("should have been the same",
                         "(int " + testValue + ")",
                         a.getEncodedValueFromMessage(JMS_INT_ATTRIBUTES[j][0],
                                                      testMessage));
        }
    }

    public void testGetEncodedValueFromMessageReturnsNullForInvalidHJBHeaders() {
        String[] invalidHeaders = new String[] {
                null, "foobar", "bazbutt", "hjb.notreally.header"
        };
        Mock mockMessage = mock(Message.class);
        for (int j = 0; j < NON_DESTINATION_ATTRIBUTES.length; j++) {
            mockMessage.expects(never())
                .method(NON_DESTINATION_ATTRIBUTES[j][1]);
        }
        Message testMessage = (Message) mockMessage.proxy();

        AttributeCopierAssistant a = new AttributeCopierAssistant();
        for (int i = 0; i < invalidHeaders.length; i++) {
            assertNull("should return null",
                       a.getEncodedValueFromMessage(invalidHeaders[i],
                                                    testMessage));
        }
    }

    public static final String[] ALL_SETTERS = new String[] {
            "setJMSDestination",
            "setJMSReplyTo",
            "setJMSCorrelationID",
            "setJMSDeliveryMode",
            "setJMSPriority",
            "setJMSExpiration",
            "setJMSMessageID",
            "setJMSRedelivered",
            "setJMSTimestamp",
            "setJMSType",
    };

    public static final String[][] NON_DESTINATION_ATTRIBUTES = new String[][] {

            new String[] {
                    AttributeCopierAssistant.REDELIVERED,
                    "getJMSRedelivered",
                    "setJMSRedelivered",
                    "(boolean false)",
                    "(int 3),foobar,(double 2)",
            },

            new String[] {
                    AttributeCopierAssistant.MESSAGE_ID,
                    "getJMSMessageID",
                    "setJMSMessageID",
                    "anyString",
                    null,
            },
            new String[] {
                    AttributeCopierAssistant.CORRELATION_ID,
                    "getJMSCorrelationID",
                    "setJMSCorrelationID",
                    "anyString",
                    null,
            },
            new String[] {
                    AttributeCopierAssistant.TYPE,
                    "getJMSType",
                    "setJMSType",
                    "anyString",
                    null,
            },

            new String[] {
                    AttributeCopierAssistant.EXPIRATION,
                    "getJMSExpiration",
                    "setJMSExpiration",
                    "(long 1)",
                    "(float 3),foobar,(boolean false)",
            },
            new String[] {
                    AttributeCopierAssistant.TIMESTAMP,
                    "getJMSTimestamp",
                    "setJMSTimestamp",
                    "(long 1)",
                    "(float 3),foobar,(boolean false)",
            },
            new String[] {
                    AttributeCopierAssistant.PRIORITY,
                    "getJMSPriority",
                    "setJMSPriority",
                    "(int 1)",
                    "(float 3),foobar,(boolean false)",
            },
            new String[] {
                    AttributeCopierAssistant.DELIVERY_MODE,
                    "getJMSDeliveryMode",
                    "setJMSDeliveryMode",
                    "(int 1)",
                    "(float 3),foobar,(boolean false)",
            },

    };

    public String[][] JMS_STRING_ATTRIBUTES = new String[][] {
            new String[] {
                    AttributeCopierAssistant.MESSAGE_ID,
                    "getJMSMessageID",
                    "setJMSMessageID"
            },
            new String[] {
                    AttributeCopierAssistant.CORRELATION_ID,
                    "getJMSCorrelationID",
                    "setJMSCorrelationID"
            },
            new String[] {
                    AttributeCopierAssistant.TYPE, "getJMSType", "setJMSType"
            },
    };

    public String[][] JMS_LONG_ATTRIBUTES = new String[][] {
            new String[] {
                    AttributeCopierAssistant.EXPIRATION,
                    "getJMSExpiration",
                    "setJMSExpiration",
            },
            new String[] {
                    AttributeCopierAssistant.TIMESTAMP,
                    "getJMSTimestamp",
                    "setJMSTimestamp",
            },
    };

    public String[][] JMS_INT_ATTRIBUTES = new String[][] {
            new String[] {
                    AttributeCopierAssistant.PRIORITY,
                    "getJMSPriority",
                    "setJMSPriority",
            },
            new String[] {
                    AttributeCopierAssistant.DELIVERY_MODE,
                    "getJMSDeliveryMode",
                    "setJMSDeliveryMode",
            },
    };

    public static String DESTINATION_ATTRIBUTES[][] = new String[][] {
            new String[] {
                    AttributeCopierAssistant.DESTINATION,
                    "getJMSDestination",
                    "setJMSDestination",
            },
            new String[] {
                    AttributeCopierAssistant.REPLY_TO,
                    "getJMSReplyTo",
                    "setJMSReplyTo",
            },
    };

}
