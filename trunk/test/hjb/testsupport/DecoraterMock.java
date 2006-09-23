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

    public void expectDecorateeToBeInvoked(String methodName,
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
    }

    public void expectDecorateeToBeInvoked(String methodName,
                                           Object[] parameters,
                                           Class[] parameterTypes)
            throws Exception {
        expectDecorateeToBeInvoked(methodName, parameters, parameterTypes, null);
    }

    public void expectDecorateeToBeInvoked(String methodName, Object result)
            throws Exception {
        expectDecorateeToBeInvoked(methodName,
                                   new Object[0],
                                   new Class[0],
                                   result);
    }

    public void expectDecorateeToBeInvoked(String methodName) throws Exception {
        expectDecorateeToBeInvoked(methodName, null);
    }

    public void expectDecorateeToThrow(String methodName,
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

    public void expectDecorateeToThrow(String methodName, Exception e)
            throws Throwable {
        expectDecorateeToThrow(methodName, new Object[0], new Class[0], e);
    }

    private final Mock mockDecoratee;
    private final Object decorater;
}
