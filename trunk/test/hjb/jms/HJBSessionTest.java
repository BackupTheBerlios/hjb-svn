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
package hjb.jms;

import java.io.Serializable;
import java.util.Date;

import javax.jms.*;

import org.jmock.Mock;

import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.DecoraterMock;
import hjb.testsupport.MockSessionBuilder;

public class HJBSessionTest extends BaseHJBTestCase {

    public void testConstructionThrowsIllegalArgumentExceptionOnNullClock() {
        try {
            new HJBSession(((Session) mockSession.proxy()), 0, null);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

    }

    public void testConstructionThrowsIllegalArgumentExceptionOnNullSession() {
        try {
            new HJBSession(null, 0, defaultTestClock());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    public void testThatSimpleNoArgMethodsInvokeDecorateeInstance()
            throws Exception {
        for (int i = 0; i < SIMPLE_NO_ARG_SESSION_METHODS.length; i++) {
            decoraterMock.invokeAndExpectOnDecoratee(SIMPLE_NO_ARG_SESSION_METHODS[i]);
            mockSession.verify();
            mockSession.reset();
        }
    }

    public void testThatSimpleNoArgMethodsPropagateJMSException()
            throws Throwable {
        for (int i = 0; i < SIMPLE_NO_ARG_SESSION_METHODS.length; i++) {
            try {
                decoraterMock.invokeAndExpectDecorateeException(SIMPLE_NO_ARG_SESSION_METHODS[i],
                                                                new JMSException("thrown as a test"));
                fail("A JMSException should have been thrown");
            } catch (JMSException e) {}
            mockSession.reset();
        }
    }

    public void testThatGetTransactedInvokesDecorateeInstance()
            throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("getTransacted",
                                                 new Boolean(false));
        mockSession.verify();
    }

    public void testThatGetTransactedPropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("getTransacted",
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testThatGetAcknowledgeModeInvokesDecorateeInstance()
            throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("getAcknowledgeMode",
                                                 new Integer(1));
        mockSession.verify();
    }

    public void testThatRunInvokesDecorateeInstance() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("run");
        mockSession.verify();
    }

    public void testThatGetAcknowledgeModePropagatesJMSException()
            throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("getAcknowledgeMode",
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
        mockSession.reset();
    }

    public void testCreateTopicInvokesDecorateeInstance() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("createTopic", new Object[] {
            "test"
        }, new Class[] {
            String.class
        });
    }

    public void testCreateTopicPropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("createTopic",
                                                            new Object[] {
                                                                "test"
                                                            },
                                                            new Class[] {
                                                                String.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testUnsubscribeInvokesDecorateeInstance() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("unsubscribe", new Object[] {
            "test"
        }, new Class[] {
            String.class
        });
    }

    public void testUnsubscribePropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("unsubscribe",
                                                            new Object[] {
                                                                "test"
                                                            },
                                                            new Class[] {
                                                                String.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCreateObjectMessageWithSerializableInvokesDecorateeInstance()
            throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("createObjectMessage",
                                                 new Object[] {
                                                     new Date(),
                                                 },
                                                 new Class[] {
                                                     Serializable.class
                                                 });
    }

    public void testCreateObjectMessageWithSerializablePropagatesJMSException()
            throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("createObjectMessage",
                                                            new Object[] {
                                                                new Date(),
                                                            },
                                                            new Class[] {
                                                                Serializable.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testSetMessageListenerInvokesDecorateeInstance()
            throws Exception {
        Mock mockMessageListener = mock(MessageListener.class);
        decoraterMock.invokeAndExpectOnDecoratee("setMessageListener",
                                                 new Object[] {
                                                     ((MessageListener) mockMessageListener.proxy()),
                                                 },
                                                 new Class[] {
                                                     MessageListener.class
                                                 });
    }

    public void testSetMessageListenerPropagatesJMSException() throws Throwable {
        Mock mockMessageListener = mock(MessageListener.class);
        try {
            decoraterMock.invokeAndExpectDecorateeException("setMessageListener",
                                                            new Object[] {
                                                                ((MessageListener) mockMessageListener.proxy()),
                                                            },
                                                            new Class[] {
                                                                MessageListener.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCreateTextMessageWithStringInvokesDecorateeInstance()
            throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("createTextMessage",
                                                 new Object[] {
                                                     "test",
                                                 },
                                                 new Class[] {
                                                     String.class
                                                 });
    }

    public void testCreateTextMessageWithStringPropagatesJMSException()
            throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("createTextMessage",
                                                            new Object[] {
                                                                "test",
                                                            },
                                                            new Class[] {
                                                                String.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCreateQueueInvokesDecorateeInstance() throws Exception {
        decoraterMock.invokeAndExpectOnDecoratee("createQueue", new Object[] {
            "test"
        }, new Class[] {
            String.class
        });
    }

    public void testCreateQueuePropagatesJMSException() throws Throwable {
        try {
            decoraterMock.invokeAndExpectDecorateeException("createQueue",
                                                            new Object[] {
                                                                "test"
                                                            },
                                                            new Class[] {
                                                                String.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCreateProducerInvokesDecorateeInstance() throws Exception {
        Mock mockDestination = mock(Destination.class);
        decoraterMock.invokeAndExpectOnDecoratee("createProducer",
                                                 new Object[] {
                                                     ((Destination) mockDestination.proxy()),
                                                 },
                                                 new Class[] {
                                                     Destination.class
                                                 });
    }

    public void testCreateProducerPropagatesJMSException() throws Throwable {
        Mock mockDestination = mock(Destination.class);
        try {
            decoraterMock.invokeAndExpectDecorateeException("createProducer",
                                                            new Object[] {
                                                                ((Destination) mockDestination.proxy()),
                                                            },
                                                            new Class[] {
                                                                Destination.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCreateConsumerInvokesDecorateeInstance() throws Exception {
        Mock mockDestination = mock(Destination.class);
        decoraterMock.invokeAndExpectOnDecoratee("createConsumer",
                                                 new Object[] {
                                                     ((Destination) mockDestination.proxy()),
                                                 },
                                                 new Class[] {
                                                     Destination.class
                                                 });
        mockSession.verify();
        mockSession.reset();
        decoraterMock.invokeAndExpectOnDecoratee("createConsumer",
                                                 new Object[] {
                                                         ((Destination) mockDestination.proxy()),
                                                         "test",
                                                 },
                                                 new Class[] {
                                                         Destination.class,
                                                         String.class,
                                                 });
        mockSession.verify();
        mockSession.reset();
        decoraterMock.invokeAndExpectOnDecoratee("createConsumer",
                                                 new Object[] {
                                                         ((Destination) mockDestination.proxy()),
                                                         "test",
                                                         new Boolean(false),
                                                 },
                                                 new Class[] {
                                                         Destination.class,
                                                         String.class,
                                                         boolean.class,
                                                 });
    }

    public void testCreateConsumerPropagatesJMSException() throws Throwable {
        Mock mockDestination = mock(Destination.class);
        try {
            decoraterMock.invokeAndExpectDecorateeException("createConsumer",
                                                            new Object[] {
                                                                ((Destination) mockDestination.proxy()),
                                                            },
                                                            new Class[] {
                                                                Destination.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
        mockSession.verify();
        mockSession.reset();
        try {
            decoraterMock.invokeAndExpectDecorateeException("createConsumer",
                                                            new Object[] {
                                                                    ((Destination) mockDestination.proxy()),
                                                                    "test",
                                                            },
                                                            new Class[] {
                                                                    Destination.class,
                                                                    String.class,
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
        mockSession.verify();
        mockSession.reset();
        try {
            decoraterMock.invokeAndExpectDecorateeException("createConsumer",
                                                            new Object[] {
                                                                    ((Destination) mockDestination.proxy()),
                                                                    "test",
                                                                    new Boolean(false),
                                                            },
                                                            new Class[] {
                                                                    Destination.class,
                                                                    String.class,
                                                                    boolean.class,
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCreateDurableSubscriberInvokesDecorateeInstance()
            throws Exception {
        Mock mockTopic = mock(Topic.class);
        decoraterMock.invokeAndExpectOnDecoratee("createDurableSubscriber",
                                                 new Object[] {
                                                         ((Topic) mockTopic.proxy()),
                                                         "test",
                                                 },
                                                 new Class[] {
                                                         Topic.class,
                                                         String.class,
                                                 });
        mockSession.verify();
        mockSession.reset();
        decoraterMock.invokeAndExpectOnDecoratee("createDurableSubscriber",
                                                 new Object[] {
                                                         ((Topic) mockTopic.proxy()),
                                                         "test",
                                                         "test",
                                                         new Boolean(false),
                                                 },
                                                 new Class[] {
                                                         Topic.class,
                                                         String.class,
                                                         String.class,
                                                         boolean.class,
                                                 });
    }

    public void testCreateDurableSubscriberPropagatesJMSException()
            throws Throwable {
        Mock mockTopic = mock(Topic.class);
        try {
            decoraterMock.invokeAndExpectDecorateeException("createDurableSubscriber",
                                                            new Object[] {
                                                                    ((Topic) mockTopic.proxy()),
                                                                    "test",
                                                            },
                                                            new Class[] {
                                                                    Topic.class,
                                                                    String.class,
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
        mockSession.verify();
        mockSession.reset();
        try {

            decoraterMock.invokeAndExpectDecorateeException("createDurableSubscriber",
                                                            new Object[] {
                                                                    ((Topic) mockTopic.proxy()),
                                                                    "test",
                                                                    "test",
                                                                    new Boolean(false),
                                                            },
                                                            new Class[] {
                                                                    Topic.class,
                                                                    String.class,
                                                                    String.class,
                                                                    boolean.class,
                                                            },
                                                            new JMSException("thrown as a test"));

            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    public void testCreateBrowserInvokesDecorateeInstance() throws Exception {
        Mock mockQueue = mock(Queue.class);
        decoraterMock.invokeAndExpectOnDecoratee("createBrowser", new Object[] {
            ((Queue) mockQueue.proxy()),
        }, new Class[] {
            Queue.class
        });
        mockSession.verify();
        mockSession.reset();
        decoraterMock.invokeAndExpectOnDecoratee("createBrowser", new Object[] {
                ((Queue) mockQueue.proxy()), "test",
        }, new Class[] {
                Queue.class, String.class,
        });
    }

    public void testCreateBrowserPropagatesJMSException() throws Throwable {
        Mock mockQueue = mock(Queue.class);
        try {
            decoraterMock.invokeAndExpectDecorateeException("createBrowser",
                                                            new Object[] {
                                                                ((Queue) mockQueue.proxy()),
                                                            },
                                                            new Class[] {
                                                                Queue.class
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
        mockSession.verify();
        mockSession.reset();
        try {
            decoraterMock.invokeAndExpectDecorateeException("createBrowser",
                                                            new Object[] {
                                                                    ((Queue) mockQueue.proxy()),
                                                                    "test",
                                                            },
                                                            new Class[] {
                                                                    Queue.class,
                                                                    String.class,
                                                            },
                                                            new JMSException("thrown as a test"));
            fail("A JMSException should have been thrown");
        } catch (JMSException e) {}
    }

    protected void setUp() throws Exception {
        super.setUp();
        sessionBuilder = new MockSessionBuilder();
        mockSession = sessionBuilder.createMockSession();
        registerToVerify(mockSession);
        testSession = new HJBSession(((Session) mockSession.proxy()),
                                     0,
                                     defaultTestClock());
        decoraterMock = new DecoraterMock(mockSession, testSession);
    }

    private MockSessionBuilder sessionBuilder;
    private Mock mockSession;
    private HJBSession testSession;
    private DecoraterMock decoraterMock;
    private static final String SIMPLE_NO_ARG_SESSION_METHODS[] = new String[] {
            "createTemporaryQueue",
            "createTemporaryTopic",
            "createMessage",
            "createObjectMessage",
            "createTextMessage",
            "createBytesMessage",
            "createStreamMessage",
            "createMapMessage",
            "commit",
            "rollback",
            "close",
            "recover",
            "getMessageListener",
    };
}
