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

  Location (a standard HTTP response header) : the URI of the created durable subscriber

This **POST** request creates a new JMS Durable Subscriber,
initialising it with values derived from the request.  The command

* constructs a durable subscriber of messages from the supplied
  subscriber-name and destination-url.

* the destination-url *must* be a destination URI belonging to the
  same Provider as the one in which the subscriber is being created.

* the destination-url *must* refer to a JMS Topic, it must not be a
  JMS Queue.

* optionally creates the subscriber using the values of *no-local* and
  *message-selector* 

* includes the URI of the subscriber in the standard HTTP 'Location'
  header of the response.

See [JMSSpec]_ for further information about JMS Durable Subscribers.

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola