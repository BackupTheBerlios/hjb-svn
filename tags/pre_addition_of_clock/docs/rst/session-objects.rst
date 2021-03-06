JMS Session Objects
===================

What are they?
--------------

JMS Session objects are the set of the actual java class instances
used to send and receive messages by a JMS messaging application like
HJB.  They are created by a JMS Session. The four distinct JMS types
are all supported by HJB; they are

* `Message Consumers`_

* `Durable Subscribers`_

* `Message Producers`_

* `Queue Browsers`_


Message Consumers
-----------------

A *Message Consumer* is used to receive messages from a Destination
See [JMSSpec]_ for a detailed description of JMS Message Consumers.

Message Consumers in HJB
++++++++++++++++++++++++

In HJB, each Message Consumer

* is represented by a URI that has the URI of the session used to
  create it as its root.

* has a URI that includes its creation index, i.e, the number of
  Message Consumers that the session created prior to creating it.

* is created by sending a HTTP POST message to the appropriate child
  URL of a session.

* is configured to receive messages from a specific Destination. The
  HJB URL of the destination is included in the parameters of the
  creating POST request.

* can be configured to use a specific Message Selector to control
  which messages are returned by including the message selector as a
  parameter in the POST request. When it is appropriate (i.e, when the
  Message Consumer is a Topic Subscriber), they can also be configured
  to ignore messages broadcast by the same connection using a noLocal
  parameter.

* may return an HJB-encoded JMS message in the HTTP response when a
  HTTP POST request is made to its 'receive' child URL.

Message Producers
-----------------

A *Message Producer* is used to send messages to a Destination. See
[JMSSpec]_ for a full description of Message Producers.

Message Producers in HJB
++++++++++++++++++++++++

In HJB, each Message Producer

* is represented by a URI that has the URI of the the session used to
  create it as its root.

* has a URI that includes its creation index, i.e, the number of
  Message Producers the session created prior to creating it.

* is created by sending a HTTP POST message to the appropriate child
  URL of a session.

* is configured to send messages to a specific Destination. The URL of
  the destination is included in the parameters of the creating POST
  request.  Other parameters are also recognised, including
  time to live, priority and delivery mode, disable timestamps and
  disable message Ids

* decodes and sends HJB-encoded JMS message on receiving a HTTP POST
  request containing the message as a parameter on the 'send' child
  URL.

Durable Subscribers
-------------------

A *Durable Subscriber* is used to receive messages from a Topic when
it is necessary for any messages sent to a Topic to be received, even
when the subscriber is not currently running.  A Topic is a
Destination specific to the Publish/Subscribe messaging domain;
normally Topic Subscribers are Message Consumers that only receive
messages that are sent when they are active. See [JMSSpec]_ for a
detailed description of the differences between the two messaging
domains.

Durable Subscribers in HJB
++++++++++++++++++++++++++

In HJB, each Durable Subscriber

* is represented by a URI that has the URI of the the session used to
  create it as its root.

* has a URI that includes its creation index, i.e, the number of
  Durable Subscribers that the session created prior to creating it.

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

* returns an HJB-encoded JMS message in the response on receiving a
  HTTP POST request on its 'receive' child URL.

Queue Browsers
--------------

A *Queue Browser* is used to look at messages on a Queue without
retrieving them.  A Queue is a Destination specific to the
Point-To-Point messaging domain; Queues always retain messages until a
client retrieves them, even if the client is not active.  See
[JMSSpec]_ for a detailed description of the differences between the
Point-To-Point and Publish-Subscribe messaging domains.

Queue Browsers in HJB
+++++++++++++++++++++

In HJB, each Queue Browser

* is represented by a HTTP resource whose URI has the URI of the
  the session used to create it as its root.

* has a URI that includes its creation index, i.e, the number of Queue
  Browsers that the session created prior to creating it.

* is created by sending a HTTP POST request to the appropriate child URL
  of a session URL.

* is configured to receive messages from a specific Queue. The URL of
  the destination is included in the parameters of the creating POST
  request.

* can be configured to use a specific Message Selector to control
  which messages are returned by including the message selector text
  as a parameter in the POST request.  See [JMSSpec]_ for more
  information about message selectors.

* returns a set of HJB-encoded JMS messages in the HTTP response on
  receiving a HTTP POST request to its 'receive' URL.

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola