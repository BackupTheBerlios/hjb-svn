===============
Create Consumer 
===============

`back to commands`_

:URL-Pattern: *session-uri*/create-consumer

:Parameters:

  (required) *destination-url*

  (optional) *no-local* 

  (optional) *message-selector*
  
:Returns:

  Location (HTTP header) : the URI of the created durable subscriber

This POST request creates a new JMS Durable Subscriber by invoking the
JMS API with parameters derived from the request.  The command

* constructs a consumer of messages from the supplied destination-url.

* the destination-url *must* be a destination URI belonging to the
  same Provider as the one in which the consumer is being created.

* the destination-url may refer to either a JMS Topic or a JMS Queue.

* optionally creates the subscriber using the values of *no-local* and
  *message-selector* to modify the behaviour of the consumer.

* returns the URI of the created consumer.

See [JMSSpec]_ for further information on creating JMS Consumers.

.. _back to commands: ./index.html
.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_
