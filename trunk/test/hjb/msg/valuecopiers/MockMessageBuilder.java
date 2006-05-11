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
import org.jmock.core.Stub;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;

import hjb.msg.valuecopiers.streammessage.UpdateByteArrayStub;

/**
 * <code>MockMessageBuilder</code> contains methods that create mock
 * <code>Messages</code> for use in various test cases.
 * 
 * @author Tim Emiola
 */
public class MockMessageBuilder {

    public MockMessageBuilder(Class mockedClazz) {
        this.mockedClazz = mockedClazz;
    }

    public void throwsJMSExceptionOnMethods(Mock aMock, String[] methodNames) {
        for (int i = 0; i < methodNames.length; i++) {
            aMock.expects(new InvokeAtLeastOnceMatcher())
                .method(methodNames[i])
                .will(new ThrowStub(new JMSException("thrown as a test")));
        }
    }

    public Message nothingExpected() {
        Mock mockMessage = new Mock(mockedClazz);
        return (Message) mockMessage.proxy();
    }

    public Message throwsJMSMessageOnAnyMethod() {
        Mock mockMessage = new Mock(mockedClazz);
        mockMessage.stubs()
            .will(new ThrowStub(new JMSException("thrown as a test")));
        return (Message) mockMessage.proxy();
    }

    public Message returnsExpectedValueFromNamedMethod(String methodName,
                                                       String mockName,
                                                       String arg1,
                                                       Object returnValue) {
        Mock mockMessage = new Mock(mockedClazz, mockName);
        mockMessage.expects(new InvokeAtLeastOnceMatcher())
            .method(methodName)
            .with(new IsEqual(arg1))
            .will(new ReturnStub(returnValue));
        return (Message) mockMessage.proxy();
    }

    public Message returnsExpectedValueFromNamedMethod(String methodName,
                                                       String mockName,
                                                       Object returnValue) {
        Mock mockMessage = new Mock(mockedClazz, mockName);
        mockMessage.expects(new InvokeAtLeastOnceMatcher())
            .method(methodName)
            .will(new ReturnStub(returnValue));
        return (Message) mockMessage.proxy();
    }

    public Message updatesMessageUsingByteArray(String methodName,
                                                String mockName,
                                                byte[] someBytes) {
        Mock mockMessage = new Mock(mockedClazz, mockName);
        mockMessage.expects(new InvokeAtLeastOnceMatcher())
            .method(methodName)
            .will(updateByteArrayWith(someBytes));
        return (Message) mockMessage.proxy();
    }

    public Message updatesMessageUsingByteArray(String methodName,
                                                String mockName,
                                                byte[] someBytes,
                                                List toThrowOn) {
        Mock mockMessage = new Mock(mockedClazz, mockName);
        throwsJMSExceptionOnMethods(mockMessage, new ArrayList(toThrowOn));
        mockMessage.expects(new InvokeAtLeastOnceMatcher())
            .method(methodName)
            .will(updateByteArrayWith(someBytes));
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
        Mock mockMessage = new Mock(mockedClazz, mockName);
        throwsJMSExceptionOnMethods(mockMessage, new ArrayList(toThrowOn));
        mockMessage.expects(new InvokeAtLeastOnceMatcher())
            .method(methodName)
            .with(new IsEqual(arg1))
            .will(new ReturnStub(returnValue));
        return (Message) mockMessage.proxy();
    }

    public Message returnsExpectedValueFromNamedMethodWithNoArgs(String methodName,
                                                                 String mockName,
                                                                 Object returnValue,
                                                                 List toThrowOn) {
        Mock mockMessage = new Mock(mockedClazz, mockName);
        throwsJMSExceptionOnMethods(mockMessage, new ArrayList(toThrowOn));
        mockMessage.expects(new InvokeAtLeastOnceMatcher())
            .method(methodName)
            .will(new ReturnStub(returnValue));
        return (Message) mockMessage.proxy();
    }

    public Message invokesNamedMethodAsExpected(String methodName,
                                                String mockName,
                                                String arg1,
                                                Object arg2) {
        Mock mockMessage = new Mock(mockedClazz, mockName);
        mockMessage.expects(new InvokeOnceMatcher())
            .method(methodName)
            .with(new IsEqual(arg1), new IsEqual(arg2));
        return (Message) mockMessage.proxy();
    }

    public Message invokesNamedMethodAsExpected(String methodName,
                                                String mockName,
                                                Object arg1) {
        Mock mockMessage = new Mock(mockedClazz, mockName);
        mockMessage.expects(new InvokeOnceMatcher())
            .method(methodName)
            .with(new IsEqual(arg1));
        return (Message) mockMessage.proxy();
    }

    public void throwsJMSExceptionOnMethods(Mock aMock, ArrayList methodNames) {
        throwsJMSExceptionOnMethods(aMock,
                                    (String[]) methodNames.toArray(new String[0]));
    }

    public Message throwsJMSMessageOnMethodNamed(String methodName) {
        return throwsExceptionOnMethodNamed(methodName, new JMSException("thrown as a test"));
    }

    public Message throwsExceptionOnMethodNamed(String methodName, Exception e) {
        if (null == methodName) return throwsJMSMessageOnAnyMethod();
        Mock mockMessage = new Mock(mockedClazz);
        mockMessage.stubs()
            .method(methodName)
            .will(new ThrowStub(e));
        return (Message) mockMessage.proxy();
    }

    private Class mockedClazz;
}
