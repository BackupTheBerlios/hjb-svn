=========================
Create Durable Subscriber
=========================

`back to commands`_

:URL-Pattern: *session-uri*/create-durable-subscriber

:Parameters:

  (required) *subscriber-name*

  (required) *destination-url*

  (optional) *no-local* 

  (optional) *message-selector*
  
:Returns:

  Location (a standard HTTP response header) : the URI_ of the created durable subscriber

This **POST** request creates a new `JMS Durable Subscriber`_ by invoking
the JMS API with parameters it contains.  The command

* constructs a durable subscriber of messages to the topic in the
  Provider's messaging system.

* *must* contain a 'destination-url' parameter that is a destination
  URI_ belonging to the same Provider as the one in which the
  subscriber is being created. The destination-url *must* refer to a
  JMS Topic. It must not be a JMS Queue.

* optionally creates the subscriber using the values of the *no-local*
  and *message-selector* parameters.

* includes the URI_ of the subscriber in the standard HTTP 'Location'
  header of the response.

See [JMSSpec]_ for further information on creating JMS Durable Subscribers.

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _JMS Durable Subscriber: http://java.sun.com/products/jms/tutorial/1_3_1-fcs/doc/advanced.html#1024758

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola