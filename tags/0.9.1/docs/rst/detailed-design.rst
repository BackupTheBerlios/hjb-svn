=====================
HJB's Design Overview
=====================

So, how does HJB work?

* HJB resides in a `Java Servlet`_ called the HJBServlet_.

* HJBServlet_ provides access to JMS objects, in particular

  - `JMS Administered Objects`_,

  - `JMS Connections and Sessions`_ and

  - `JMS Session Objects`_, from which messages can be sent and
    retrieved.

* HJBServlet_ processes the requests it receives by `dispatching`_
  them as `JMS commands`_.  Some of these requests are JMS
  administration commands, others send and receive `JMS messages`_.

* HJB achieves language-neutrality by `converting`_ between the Java
  native types that the `JMS API`_ uses and a simple textual
  representation suitable for transmission via HTTP_ that preserves
  Java type information.

There is not much to it really ;-)

.. _HTTP: http://en.wikipedia.org/wiki/HTTP

.. _dispatching: ./command-dispatch.html

.. _Java Servlet: http://en.wikipedia.org/wiki/Java_Servlet

.. _JMS API: http://java.sun.com/products/jms/docs.html

.. _JMS commands: ./command-list.html

.. _JMS Administered Objects: ./administered-objects.html

.. _JMS Connections and Sessions: ./connections-sessions.html

.. _JMS Session Objects: ./session-objects.html

.. _converting: ./codec.html

.. _HJBServlet: ./hjb-servlet.html

.. _JMS messages: ./message-translation.html

.. Copyright (C) 2006 Tim Emiola