=====================
HJB's Design Overview
=====================

So, how does HJB work?

* HJB is Java Servlet called the HJBServlet_.

* HJBServlet_ provides access to JMS objects, in particular

  - `JMS Administered Objects`_,

  - `JMS Connections and Sessions`_ and

  - `JMS Session Objects`_, from which messages can be sent and
    retrieved.

* HJBServlet_ processes the requests it receives by `dispatching`_
  them as `JMS commands`_.  Some of these requests are JMS
  administration commands, others send and receive `JMS messages`_.

* HJB achieves language-neutrality by `converting`_ between the Java
  native types that the JMS API uses and a simple textual
  representation that preserves the type information that is suitable
  for transmission via HTTP.

There is not much to it really ;-)

.. _dispatching: ./command-dispatch.html

.. _JMS commands: ./command-list.html

.. _JMS Administered Objects: ./administered-objects.html

.. _JMS Connections and Sessions: ./connections-sessions.html

.. _JMS Session Objects: ./session-objects.html

.. _converting: ./codec.html

.. _HJBServlet: ./hjb-servlet.html

.. _JMS messages: ./message-translation.html