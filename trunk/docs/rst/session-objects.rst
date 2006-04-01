JMS Session Objects
===================

What are they?
--------------

JMS Session objects are the actual java class instances used to send
and receive messages by a JMS messaging application.  They are created
using a given Session. The four distinct types supported by HJB are

* `Message Consumers`_

* `Durable Subscribers`_

* `Message Producers`_

* `Queue Browsers`_


Message Consumers
-----------------

What are they?
++++++++++++++

A *Message Consumer* is used to receive messages from a Destination
([JMSSpec]_).

Message Consumers in HJB
++++++++++++++++++++++++

At runtime in HJB, each Message Consumer

* is represented by a HTTP resource whose URL is a child URL of the
  the session used to create it.

* is created by sending a HTTP POST message to the appropriate child
  URL of a session.

* is configured to receive messages from a specific Destination. The
  URL of the destination is included in the parameters of the creating
  POST request.

* can be configured to use a specific Message Selector (see
  [JMSSpec]_) to control which messages are returned by including the
  message selector as a parameter in the POST request. When it is
  appropriate, they can also be configured to ignore messages
  broadcast by the same connection using a noLocal parameter.

* has a URL that includes its creation index, i.e, the number of
  Message Consumers that have been created by the session so far.

* returns an HJB-encoded JMS message on receiving a HTTP POST request
  on the appropriate child URL.

Message Producers
-----------------

What are they?
++++++++++++++

A *Message Producer* is used to send messages to a Destination
([JMSSpec]_).

Message Producers in HJB
++++++++++++++++++++++++

At runtime in HJB, each Message Producer

* is represented by a HTTP resource whose URL is a child URL of the
  the session used to create it.

* is created by sending a HTTP POST message to the appropriate child
  URL of a session.

* is configured to send messages to a specific Destination. The URL of
  the destination is included in the parameters of the creating POST
  request.  Other parameters are also recognised, including
  time to live, priority and delivery mode, disable timestamps and
  disable message Ids

* has a URL that includes its creation index, i.e, the number of
  Message Consumers that have been created by the session so far.

* decodes and sends HJB-encoded JMS message on receiving a HTTP POST
  request containing the message as a parameter on the appropriate
  child URL.

Durable Subscribers
-------------------

What are they?
++++++++++++++

A *Durable Subscriber* is used to receive messages from a Topic when
it is necessary for all messages received on that Topic to be
received.  A Topic is a Destination specific to the Publish/Subscribe
messaging domain; normally Topic Subscribers are Message Consumers
that only receive messages that are sent when they active. See
([JMSSpec]_) for a detailed description of the differences between the
two messaging domains.

Durable Subscribers in HJB
++++++++++++++++++++++++++

At runtime in HJB, each Durable Subscriber

* is represented by a HTTP resource URL whose URL is a child URL of
  the session used to create it.

* is created by sending a HTTP POST request to the appropriate child URL
  of a session URL.

* is configured to receive messages from a specific Destination. The URL
  of the destination is included in the parameters of the creating
  POST request.

* can be configured to use a specific Message Selector (see
  [JMSSpec]_) to control which messages are returned by including the
  message selector as a parameter in the POST request. When it is
  appropriate, they can also be configured to ignore messages
  broadcast by the same connection using a noLocal parameter.

* has a URL that includes its creation index, i.e, the number of
  Durable Subscribers that have been created by the connection so far.

* returns an HJB-encoded JMS message on receiving a HTTP POST request
  on the appropriate child URL.

Queue Browsers
--------------

What are they?
++++++++++++++

A *Queue Browser* is used to look at messages on a Queue without
retrieving them.  A Queue is a Destination specific to the
Point-To-Point messaging domain; Queues always retain messages until a
client retrieves them, even if the client is not active.  See
([JMSSpec]_) for a detailed description of the differences between the
two messaging domains.

Queue Browsers in HJB
+++++++++++++++++++++

At runtime in HJB, each Durable Subscriber

* is represented by a HTTP resource URL whose URL is a child URL of
  the session used to create it.

* is created by sending a HTTP POST request to the appropriate child URL
  of a session URL.

* is configured to receive messages from a specific Queue. The URL of
  the destination is included in the parameters of the creating POST
  request.

* can be configured to use a specific Message Selector (see
  [JMSSpec]_) to control which messages are returned by including the
  message selector as a parameter in the POST request.

* has a URL that includes its creation index, i.e, the number of
  Queue Browsers that have been created by the connection so far.

* returns a set of HJB-encoded JMS messages on receiving a HTTP POST
  request on the appropriate child URL.

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_
