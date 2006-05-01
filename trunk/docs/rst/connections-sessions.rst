============================
JMS Connections and Sessions
============================

JMS Connections
---------------

A *JMS Connection* represents an instance of a transport endpoint that
a Provider uses for messaging.  JMS applications create them using a
ConnectionFactory . They are subsequently used to create `JMS Sessions`_ (see
[JMSSpec]_ for further information).

Connections in HJB
++++++++++++++++++

In HJB, each Connection

* is represented by a HTTP resource whose URL is a child URL of the
  connection-factory used to create the connection.

* is created by sending a HTTP POST message to the appropriate child URL
  of a connection-factory URL.

* can accept logon credentials on creation as parameters in
  the creating HTTP POST request.

* has a URL that includes its creation index. This is the number of
  connections that had been created by the connection factory prior to this one.

* can be stopped, started and removed by sending HTTP POST/GET requests
  to the appropriate child URL of the Connection URL.

* returns its metadata on receiving a HTTP GET on the appropriate child
  URL.

* is configured with an ExceptionListener that writes any exceptions
  that occur on the connection to a log file.  The log file can be
  retrieved using HTTP GET on the appropriate child URL.


JMS Sessions
------------

A JMS *Session* represents a single-threaded context in which messages
are sent and/or received. They are created from `JMS Connections`_,
which serve as a factory for JMS Sessions .  Sessions themselves serve
a factory for `session objects`_: MessageProducers, MessageConsumers,
QueueBrowsers and DurableSubscribers, which are the objects that JMS
uses to perform the actual sending and receiving of messages (see
[JMSSpec]_ for a full description).

Sessions in HJB
+++++++++++++++

In HJB, each JMS Session

* is represented by a HTTP resource whose URL is a child URL of the
  connection used to create the session.

* is created by sending a HTTP POST request to the appropriate child
  URL of a connection URL.

* can be created either 'transacted' or not 'transacted', and with a
  specific acknowledgement mode by including these values as
  parameters in the creating POST request; these default to 'not
  transacted' with 'auto-acknowledgement'. ([JMSSpec]_ explains these
  terms in more detail)

* has a URL that it includes its creation index. This is the number of
  other sessions that had been created by the connection prior to this
  one.

* can be removed, rolled back or committed by sending a HTTP request
  to the appropriate child URL.

* on being removed, will terminate any request of a child
  MessageConsumer, DurableSubscriber, MessageProducer or QueueBrowser
  that is still being processed.

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_

.. _session objects: ./session-objects.rst
