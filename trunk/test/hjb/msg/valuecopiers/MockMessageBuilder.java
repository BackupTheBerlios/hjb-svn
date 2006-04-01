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

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Stub;
import org.jmock.core.stub.ThrowStub;

import hjb.msg.valuecopiers.streammessage.UpdateByteArrayStub;

/**
 * <code>MockMessageBuilder</code> contains methods that create mock
 * <code>Messages</code> for use in various test cases.
 * 
 * @author Tim Emiola
 */
public class MockMessageBuilder {

    public MockMessageBuilder(MockObjectTestCase test, Class mockedClazz) {
        this.test = test;
        this.mockedClazz = mockedClazz;
    }

    public void throwsJMSExceptionOnMethods(Mock aMock, String[] methodNames) {
        for (int i = 0; i < methodNames.length; i++) {
            aMock.expects(test.atLeastOnce()).method(methodNames[i]).will(test.throwException(new JMSException("thrown as a test")));
        }
    }

    public Message nothingExpected() {
        Mock mockMessage = test.mock(mockedClazz);
        return (Message) mockMessage.proxy();
    }

    public Message throwsJMSMessageOnAnyMethod() {
        Mock mockMessage = test.mock(mockedClazz);
        mockMessage.stubs().will(new ThrowStub(new JMSException("thrown as a test")));
        return (Message) mockMessage.proxy();
    }

    public Message returnsExpectedValueFromNamedMethod(String methodName,
                                                       String mockName,
                                                       String arg1,
                                                       Object returnValue) {
        Mock mockMessage = test.mock(mockedClazz, mockName);
        mockMessage.expects(test.atLeastOnce()).method(methodName).with(test.eq(arg1)).will(test.returnValue(returnValue));
        return (Message) mockMessage.proxy();
    }

    public Message returnsExpectedValueFromNamedMethod(String methodName,
                                                       String mockName,
                                                       Object returnValue) {
        Mock mockMessage = test.mock(mockedClazz, mockName);
        mockMessage.expects(test.atLeastOnce()).method(methodName).will(test.returnValue(returnValue));
        return (Message) mockMessage.proxy();
    }

    public Message updatesMessageUsingByteArray(String methodName,
                                                String mockName,
                                                byte[] someBytes) {
        Mock mockMessage = test.mock(mockedClazz, mockName);
        mockMessage.expects(test.atLeastOnce()).method(methodName).will(updateByteArrayWith(someBytes));
        return (Message) mockMessage.proxy();
    }

    public Message updatesMessageUsingByteArray(String methodName,
                                                String mockName,
                                                byte[] someBytes,
                                                List toThrowOn) {
        Mock mockMessage = test.mock(mockedClazz, mockName);
        throwsJMSExceptionOnMethods(mockMessage, new ArrayList(toThrowOn));
        mockMessage.expects(test.atLeastOnce()).method(methodName).will(updateByteArrayWith(someBytes));
        return (Message) mockMessage.proxy();
    }

    public Stub updateByteArrayWith(byte[] someBytes) {
        return new UpdateByteArrayStub(someBytes);
    }

    public Message throwsOnSome(String methodName,
                                String mockName,
                                String arg1,
                                Object returnValue,
                                List toThrowOn) {
        Mock mockMessage = test.mock(mockedClazz, mockName);
        throwsJMSExceptionOnMethods(mockMessage, new ArrayList(toThrowOn));
        mockMessage.expects(test.atLeastOnce()).method(methodName).with(test.eq(arg1)).will(test.returnValue(returnValue));
        return (Message) mockMessage.proxy();
    }

    public Message returnsExpectedValueFromNamedMethodWithNoArgs(String methodName,
                                                                 String mockName,
                                                                 Object returnValue,
                                                                 List toThrowOn) {
        Mock mockMessage = test.mock(mockedClazz, mockName);
        throwsJMSExceptionOnMethods(mockMessage, new ArrayList(toThrowOn));
        mockMessage.expects(test.atLeastOnce()).method(methodName).will(test.returnValue(returnValue));
        return (Message) mockMessage.proxy();
    }

    public Message invokesNamedMethodAsExpected(String methodName,
                                                String mockName,
                                                String arg1,
                                                Object arg2) {
        Mock mockMessage = test.mock(mockedClazz, mockName);
        mockMessage.expects(test.once()).method(methodName).with(test.eq(arg1),
                                                                 test.eq(arg2));
        return (Message) mockMessage.proxy();
    }

    public Message invokesNamedMethodAsExpected(String methodName,
                                                String mockName,
                                                Object arg1) {
        Mock mockMessage = test.mock(mockedClazz, mockName);
        mockMessage.expects(test.once()).method(methodName).with(test.eq(arg1));
        return (Message) mockMessage.proxy();
    }

    public void throwsJMSExceptionOnMethods(Mock aMock, ArrayList methodNames) {
        throwsJMSExceptionOnMethods(aMock,
                                    (String[]) methodNames.toArray(new String[0]));
    }

    public Message throwsJMSMessageOnMethodNamed(String methodName) {
        if (null == methodName) return throwsJMSMessageOnAnyMethod();
        Mock mockMessage = test.mock(mockedClazz);
        mockMessage.stubs().method(methodName).will(test.throwException(new JMSException("thrown as a test")));
        return (Message) mockMessage.proxy();
    }

    private MockObjectTestCase test;
    private Class mockedClazz;
}
