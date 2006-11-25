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

  Location (HTTP header) : the URI_ of the created message consumer

This **POST** request creates a new `JMS Message Consumer`_, initialising
it with values derived from the request.  The command

* constructs a consumer of messages from the supplied destination-url.

* the destination-url *must* be a destination URI_ belonging to the
  same Provider as the one in which the consumer is being created.

* the destination-url may refer to either a JMS Topic or a JMS Queue.

* optionally creates the subscriber using the values of *no-local* and
  *message-selector* to modify the behaviour of the consumer.

* returns the URI_ of the created consumer.

See [JMSSpec]_ for further information on creating JMS Consumers.

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _JMS Message Consumer: http://java.sun.com/products/jms/tutorial/1_3_1-fcs/doc/prog_model.html#1026102

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola