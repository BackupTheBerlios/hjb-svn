JMS Connections and Sessions
============================

JMS Connections
---------------

What are they?
++++++++++++++

A *JMS Connection* represents an instance of a transport endpoint that
a Provider can use for messaging.  JMS applications create
them using a ConnectionFactory (see([JMSSpec]_) for further information).

Connections in HJB
++++++++++++++++++

At runtime in HJB, each Connection

* is represented by a HTTP resource whose URL is a child URL of the
  connection-factory used to create the connection.

* is created by sending a HTTP POST message to the appropriate child URL
  of a connection-factory URL.

* can be supplied logon information on creation by including the
  connection information as parameters in the creating HTTP POST
  request.

* has a URL that it includes its creation index, i.e, the number of
  connections that have been created by the connection factory so far.

* can be stopped, started and removed by sending a HTTP POST/GET requests
  to the appropriate child URL of the Connection URL.

* returns its metadata on receiving a HTTP GET on the appropriate child
  URL.

* is configured with an ExceptionListener that writes exceptions on
  the connection to a log file that can be retrieved using HTTP GET on
  the appropriate child URL.


JMS Sessions
------------

What are they?
++++++++++++++

A JMS *Session* represents a single-threaded context in which messages
are sent and/or received. They are created from JMS Connections, which
serve as a factory for JMS Sessions (see [JMSSpec]_ for a full
description).

Sessions in HJB
+++++++++++++++

At runtime in HJB, each JMS Session

* is represented by a HTTP resource whose URL is a child URL of the
  connection used to create the session.

* is created by sending a HTTP POST request to the appropriate child
  URL of a connection URL.

* can be created transacted or with a specific acknowledgement mode by
  including these values as parameters as in the creating POST
  request; these default to not transacted with auto-acknowledgement.

* has a URL that it includes its creation index, i.e, the number of
  sessions that have been created by the connection so far.

* can be removed, rolled back or committed by sending a request to the
  appropriate child URL of the session URL.

* will terminate any open message processing request of a child
  MessageConsumer, DurableSubscriber, MessageProducer or QueueBrowser
  on being removed.

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_
