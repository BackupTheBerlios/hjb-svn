package hjb.testsupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmock.Mock;
import org.jmock.core.Constraint;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;

public class DecoraterMock {

    public DecoraterMock(Mock mockDecoratee, Object decorater) {
        this.mockDecoratee = mockDecoratee;
        this.decorater = decorater;
    }

    public void invokeAndExpectOnDecoratee(String methodName,
                                           Object[] parameters,
                                           Class[] parameterTypes,
                                           Object result) throws Exception {
        Constraint[] constraints = new Constraint[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            constraints[i] = new IsEqual(parameters[i]);
        }
        if (null == result) {
            mockDecoratee.expects(new InvokeOnceMatcher())
                .method(methodName)
                .with(constraints);
        } else {
            mockDecoratee.expects(new InvokeOnceMatcher())
                .method(methodName)
                .with(constraints)
                .will(new ReturnStub(result));
        }
        Method method = decorater.getClass().getMethod(methodName,
                                                       parameterTypes);
        method.invoke(decorater, parameters);
        mockDecoratee.verify();
    }

    public void invokeAndExpectOnDecoratee(String methodName,
                                           Object[] parameters,
                                           Class[] parameterTypes)
            throws Exception {
        invokeAndExpectOnDecoratee(methodName, parameters, parameterTypes, null);
    }

    public void invokeAndExpectOnDecoratee(String methodName, Object result)
            throws Exception {
        invokeAndExpectOnDecoratee(methodName,
                                   new Object[0],
                                   new Class[0],
                                   result);
    }

    public void invokeAndExpectOnDecoratee(String methodName) throws Exception {
        invokeAndExpectOnDecoratee(methodName, null);
    }

    public void invokeAndExpectDecorateeException(String methodName,
                                                  Object[] parameters,
                                                  Class[] parameterTypes,
                                                  Exception e) throws Throwable {
        Constraint[] constraints = new Constraint[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            constraints[i] = new IsEqual(parameters[i]);
        }
        mockDecoratee.expects(new InvokeOnceMatcher())
            .method(methodName)
            .with(constraints)
            .will(new ThrowStub(e));
        Method method = decorater.getClass().getMethod(methodName,
                                                       parameterTypes);
        try {
            method.invoke(decorater, parameters);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

    public void invokeAndExpectDecorateeException(String methodName, Exception e)
            throws Throwable {
        invokeAndExpectDecorateeException(methodName,
                                          new Object[0],
                                          new Class[0],
                                          e);
    }

    private final Mock mockDecoratee;
    private final Object decorater;
}
