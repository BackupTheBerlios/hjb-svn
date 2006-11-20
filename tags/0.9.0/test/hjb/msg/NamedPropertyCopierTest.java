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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.stub.ThrowStub;

import hjb.misc.HJBException;

public class NamedPropertyCopierTest extends MockObjectTestCase {

    public void testAnyTwoInstancesAreEqual() {
        assertEquals("Any two instances were not equal",
                     new NamedPropertyCopier(),
                     new NamedPropertyCopier());
    }

    public void testEqualsWorksCorrectly() {
        assertNotSame("Cannot be distinguished from object!",
                      new NamedPropertyCopier(),
                      new Object());
    }

    public void testCopyToJMSMessageDoesNothingWhenHeadersAreAbsent() {
        Mock mockJms = mock(Message.class);
        for (int i = 0; i < PROPERTY_SETTERS.length; i++) {
            mockJms.expects(never()).method(PROPERTY_SETTERS[i]);
        }
        for (int i = 0; i < PROPERTY_GETTERS.length; i++) {
            mockJms.expects(never()).method(PROPERTY_GETTERS[i]);
        }

        Message testMessage = (Message) mockJms.proxy();
        HJBMessage mimeMessage = new HJBMessage(new HashMap(), "test");
        NamedPropertyCopier copier = new NamedPropertyCopier();
        copier.copyToJMSMessage(mimeMessage, testMessage);
    }

    public void testCopyToJMSMessageThrowsHJBExceptionOnJMSException() {
        Mock mockJms = mock(Message.class);

        Message testMessage = (Message) mockJms.proxy();
        HJBMessage mimeMessage = new HJBMessage(new HashMap(), "test");
        NamedPropertyCopier copier = new NamedPropertyCopier();
        try {
            copier.copyToJMSMessage(mimeMessage, testMessage);
        } catch (HJBException e) {}
    }

    public void testCopyToJMSMessageInvokesExpectedSetters() {
        Mock mockJms = mock(Message.class);
        for (int i = 0; i < PROPERTY_SETTERS.length; i++) {
            mockJms.expects(atLeastOnce())
                .method(PROPERTY_SETTERS[i])
                .withAnyArguments();
        }

        Message testMessage = (Message) mockJms.proxy();
        HJBMessage mimeMessage = new HJBMessage(getTestHeaders(), "test");
        NamedPropertyCopier copier = new NamedPropertyCopier();
        copier.copyToJMSMessage(mimeMessage, testMessage);
    }

    public void testCopyToHJBMessageDoesNothingWhenNoPropertiesArePresent() {
        Mock mockJms = mock(Message.class);
        mockJms.expects(once())
            .method("getPropertyNames")
            .will(returnValue(Collections.enumeration(new ArrayList())));
        for (int i = 0; i < PROPERTY_SETTERS.length; i++) {
            mockJms.expects(never()).method(PROPERTY_SETTERS[i]);
        }
        for (int i = 0; i < PROPERTY_GETTERS.length; i++) {
            mockJms.expects(never()).method(PROPERTY_GETTERS[i]);
        }

        Message testMessage = (Message) mockJms.proxy();
        HJBMessage mimeMessage = new HJBMessage(new HashMap(), "test");
        NamedPropertyCopier copier = new NamedPropertyCopier();
        copier.copyToHJBMessage(testMessage, mimeMessage);

        assertEquals("there should be no headers", 0, mimeMessage.getHeaders()
            .size());
    }

    public void testCopyToHJBMessageAddsExpectedHeaders() {
        Mock mockJms = mock(Message.class);
        mockJms.expects(once())
            .method("getPropertyNames")
            .will(returnValue(Collections.enumeration(getTestHeaders().keySet())));
        mockJms.expects(atLeastOnce())
            .method("getBooleanProperty")
            .with(eq("testBoolean"))
            .will(returnValue(true));
        mockJms.expects(atLeastOnce())
            .method("getStringProperty")
            .with(eq("testBoolean"))
            .will(returnValue("" + true));
        mockJms.expects(atLeastOnce())
            .method("getByteProperty")
            .with(eq("testByte"))
            .will(returnValue((byte) 4));
        mockJms.expects(atLeastOnce())
            .method("getByteProperty")
            .with(not(eq("testByte")))
            .will(throwException(new JMSException("thrown as a test")));
        mockJms.expects(atLeastOnce())
            .method("getDoubleProperty")
            .with(eq("testDouble"))
            .will(returnValue(2.0d));
        mockJms.expects(atLeastOnce())
            .method("getDoubleProperty")
            .with(not(eq("testDouble")))
            .will(throwException(new JMSException("thrown as a test")));
        mockJms.expects(atLeastOnce())
            .method("getFloatProperty")
            .with(eq("testFloat"))
            .will(returnValue(1.0f));
        mockJms.expects(atLeastOnce())
            .method("getFloatProperty")
            .with(not(eq("testFloat")))
            .will(throwException(new JMSException("thrown as a test")));
        mockJms.expects(atLeastOnce())
            .method("getIntProperty")
            .with(eq("testInteger"))
            .will(returnValue(6));
        mockJms.expects(atLeastOnce())
            .method("getIntProperty")
            .with(not(eq("testInteger")))
            .will(throwException(new JMSException("thrown as a test")));
        mockJms.expects(atLeastOnce())
            .method("getLongProperty")
            .with(eq("testLong"))
            .will(returnValue(7l));
        mockJms.expects(atLeastOnce())
            .method("getLongProperty")
            .with(not(eq("testLong")))
            .will(throwException(new JMSException("thrown as a test")));
        mockJms.expects(atLeastOnce())
            .method("getShortProperty")
            .with(eq("testShort"))
            .will(returnValue((short) 5));
        mockJms.expects(atLeastOnce())
            .method("getShortProperty")
            .with(not(eq("testShort")))
            .will(throwException(new JMSException("thrown as a test")));
        mockJms.expects(atLeastOnce())
            .method("getStringProperty")
            .with(eq("testString"))
            .will(returnValue("3.0"));

        Message testMessage = (Message) mockJms.proxy();
        HJBMessage mimeMessage = new HJBMessage(new HashMap(), "test");
        NamedPropertyCopier copier = new NamedPropertyCopier();
        copier.copyToHJBMessage(testMessage, mimeMessage);
        assertEquals("there should be 8 headers", 8, mimeMessage.getHeaders()
            .size());
        assertEquals("header values are incorrect",
                     getTestHeaders(),
                     mimeMessage.getHeaders());
    }

    public void testCopyToHJBMessageThrowsHJBExceptionOnJMSException() {
        Mock mockJms = mock(Message.class);
        mockJms.setDefaultStub(new ThrowStub(new JMSException("thrown as a test")));

        Message testMessage = (Message) mockJms.proxy();
        HJBMessage mimeMessage = new HJBMessage(new HashMap(), "test");
        NamedPropertyCopier copier = new NamedPropertyCopier();
        try {
            copier.copyToHJBMessage(testMessage, mimeMessage);
        } catch (HJBException e) {}
    }

    protected Map getTestHeaders() {
        Map result = new HashMap();
        for (int i = 0; i < TEST_ENCODED_VALUES.length; i++) {
            result.put(TEST_ENCODED_VALUES[i][0], TEST_ENCODED_VALUES[i][1]);
        }
        return result;
    }

    private static final String[] PROPERTY_GETTERS = new String[] {
            "getBooleanProperty",
            "getByteProperty",
            "getDoubleProperty",
            "getFloatProperty",
            "getIntProperty",
            "getLongProperty",
            "getShortProperty",
            "getStringProperty",
    };

    private static final String[] PROPERTY_SETTERS = new String[] {
            "setBooleanProperty",
            "setByteProperty",
            "setDoubleProperty",
            "setFloatProperty",
            "setIntProperty",
            "setLongProperty",
            "setShortProperty",
            "setStringProperty",
    };

    private static final String[][] TEST_ENCODED_VALUES = new String[][] {
            new String[] {
                    "testFloat", "(float 1E0)"
            }, new String[] {
                    "testDouble", "(double 2E0)"
            }, new String[] {
                    "testString", "3.0"
            }, new String[] {
                    "testBoolean", "(boolean true)"
            }, new String[] {
                    "testByte", "(byte 4)"
            }, new String[] {
                    "testShort", "(short 5)"
            }, new String[] {
                    "testInteger", "(int 6)"
            }, new String[] {
                    "testLong", "(long 7)"
            }
    };

}
