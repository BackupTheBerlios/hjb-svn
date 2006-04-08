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

  Location (HTTP header) : the URI of the created durable subscriber

This POST request creates a new JMS Durable Subscriber by invoking the
JMS API with parameters derived from the request.  The command

* constructs a durable subscriber of messages from the Provider's
  messaging system.

* the destination-url *must* be a destination URI belonging to the
  same Provider as the one in which the subscriber is being created.

* the destination-url *must* refer to a JMS Topic, it must not be a
  JMS Queue.

* optionally creates the subscriber using the values of *no-local* and
  *message-selector* 

* returns the URI of the created connection.

See [JMSSpec]_ for further information on creating JMS Durable Subscribers.

.. _back to commands: ./index.html
.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_
