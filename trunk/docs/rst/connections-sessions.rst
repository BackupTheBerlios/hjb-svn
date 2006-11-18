============================
JMS Connections and Sessions
============================

JMS Connections
---------------

A *JMS Connection* represents an instance of a transport endpoint that
a JMS Provider uses for messaging.  JMS applications create them using
a connection factory. They are subsequently used to create `JMS
Sessions`_ [JMSSpec]_.

Connections in HJB
++++++++++++++++++

In HJB, each connection

* is represented by a HTTP resource whose URI has the URI of the
  connection factory that created it as its root.

* has a URI that includes its creation index. For a given connection,
  this is the number of connections that have been created by its
  connection factory prior to its creation.

* is created by sending a HTTP POST message to a specific child URI of
  the root connection factory URI.

* accepts authentication credentials as parameters in the HTTP POST
  request that is used to create it.

* can be stopped, started and removed by sending HTTP POST or GET
  requests to the appropriate child URL of its URI.

* sends its metadata as text on receiving a HTTP GET request to its
  /metadata child URL.

* lists its active sessions on receiving a HTTP GET request to its
  /list child URL.

* is configured with an ExceptionListener that writes any exceptions
  that occur on the connection to a log file.  The log file can be
  retrieved using HTTP GET on the appropriate child URL. 

JMS Sessions
------------

A JMS *Session* represents a single-threaded context in which messages
are sent and/or received. They are created from `JMS Connections`_,
which serve as a factory object for JMS Sessions.  Sessions themselves
serve as a factory object for `session objects`_: MessageProducers,
MessageConsumers, QueueBrowsers and DurableSubscribers, which are the
objects in JMS that perform the actual sending and receiving of
messages [JMSSpec]_.

Sessions in HJB
+++++++++++++++

In HJB, each JMS Session

* is represented by a HTTP resource whose URI has the URI of the
  connection used to create it as its root.

* has a URI that includes its creation index. For a given session,
  this is the number of other sessions that have been created by its
  the connection that created it prior its creation.

* is created by sending a HTTP POST request to the appropriate child
  URL of a connection URL.

* can be created either 'transacted' or not 'transacted', and with a
  specific acknowledgement mode by including these values as
  parameters in the creating POST request; these default to 'not
  transacted' with 'auto-acknowledgement'. [JMSSpec]_ explains these
  terms in more detail.

* can be removed, rolled back or committed by sending a HTTP request
  to the appropriate child URL.

* lists its session objects on receiving a HTTP GET request to its
  /list child URL.

* on being removed, will terminate any requests to its
  MessageConsumers, DurableSubscribers, MessageProducers or
  QueueBrowsers that are still being processed.

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_

.. _session objects: ./session-objects.html

.. Copyright (C) 2006 Tim Emiola