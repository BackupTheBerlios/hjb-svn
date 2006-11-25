JMS Session Objects
===================

What are they?
--------------

JMS Session objects are the set of actual java class instances used to
send and receive messages by a JMS messaging application like HJB.
They are created by a JMS Session. The four distinct JMS types of
session objects are all supported by HJB; they are

* `Message Consumers`_

* `Durable Subscribers`_

* `Message Producers`_

* `Queue Browsers`_

In HJB, each session object

* is represented by a URI that has the URI of the session used to
  create it as its root.

* has a URI that includes its creation index. This is the same as
  number of session objects of that type that the session has created
  prior to creating it.

* is created by sending a HTTP POST message to the appropriate child
  URL of a session.

Message Consumers
-----------------

A *Message Consumer* is used to receive messages from a Destination.
Refer to [JMSSpec]_ for a detailed description of JMS Message
Consumers.

Message Consumers in HJB
++++++++++++++++++++++++

In HJB, each Message Consumer

* is configured to receive messages from a specific Destination. The
  HJB URI of the destination is one of the parameters of the POST
  request that creates it.

* can be configured to use a specific message selector to control
  which messages are returned.  This achieved by including the message
  selector as a parameter of the POST request that creates it. When it
  is appropriate (i.e, when the Message Consumer is a Topic
  Subscriber), they can also be configured to ignore messages
  broadcast by the same connection, by inclusion of another parameter
  containing the value of the 'non-local' flag.

* sends back an HJB-encoded JMS message in the HTTP response when a
  HTTP POST request is sent to its 'receive' child URL, or sends back
  a *404 Not Found* response if no message is available.

Message Producers
-----------------

A *Message Producer* is used to send messages to a Destination. Refer
to [JMSSpec]_ for a full description of Message Producers.

Message Producers in HJB
++++++++++++++++++++++++

In HJB, each Message Producer

* is configured to send messages to a specific Destination. The URI of
  the destination is one of the parameters of the creating POST
  request.  Other parameters are also recognised, including 'time to
  live', priority, delivery mode, 'disable timestamps' and 'disable
  messageIds'

* decodes an HJB-encoded message sent as a parameter within a POST
  request to its /send child URL.  It translates it into a JMS message
  and transmits it using the JMS Provider's messaging infrastructure.

Durable Subscribers
-------------------

A *Durable Subscriber* is used to receive messages from a Topic when
it is necessary for all messages sent to a Topic to be received, even
if they are sent whilst the subscriber is not currently running.  A
Topic is a Destination specific to the Publish/Subscribe messaging
domain; normally Topic Subscribers are Message Consumers that only
receive messages that are sent whilst they are running. Refer to
[JMSSpec]_ for a detailed description of the differences between the
two messaging domains.

Durable Subscribers in HJB
++++++++++++++++++++++++++

In HJB, each Durable Subscriber

* is configured to receive messages from a specific Destination. The
  URL of the destination is one of the parameters of the POST request
  that creates it.

* can be configured to use a specific message selector to control
  which messages are returned, by including the message selector as a
  parameter in the POST request that creates it.  Similarly, a
  'no-local' parameter can be used to control whether or not it
  retrieves messages broadcast by the same JMS connection.

* sends back an HJB-encoded JMS message in the response on receiving a
  HTTP POST request at its /receive child URL, or sends a *404 Not
  Found* response if no message is available.

Queue Browsers
--------------

A *Queue Browser* is used to look at messages on a Queue without
retrieving them.  A Queue is a Destination specific to the
Point-To-Point messaging domain; Queues always retain messages until a
client retrieves them.  Refer to [JMSSpec]_ for a detailed description of
the differences between the Point-To-Point and Publish-Subscribe
messaging domains.

Queue Browsers in HJB
+++++++++++++++++++++

In HJB, each Queue Browser

* is configured to retrieve messages from a specific JMS Queue. The
  URI of the Queue is included in the parameters of the POST request
  that creates it.

* can be configured to use a specific Message Selector to control
  which messages are returned, by including the message selector text
  as a parameter in the POST request.  Refer to [JMSSpec]_ for more
  information about message selectors.

* sends back a set of HJB-encoded JMS messages in the HTTP response on
  receiving a HTTP POST request to its /receive child URL.

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola