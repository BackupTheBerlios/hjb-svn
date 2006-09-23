===============
Create Producer
===============

`back to commands`_

:URL-Pattern: *session-uri*/create-producer

:Parameters:

  (required) *destination-url*

  (optional) *time-to-live*

  (optional) *delivery-mode*

  (optional) *disable-timestamps*

  (optional) *distable-messageIds*

  (optional) *priority*
  
:Returns:

  Location (a standard HTTP response header) : the URI of the created producer

This **POST** request creates a new JMS Producer by invoking the JMS
API with the parameters derived from the request.  The command

* constructs a producer for sending messages to the supplied
  destination-url.

* the destination-url *must* be a destination URI belonging to the
  same Provider as the one in which the consumer is being created.

* the destination-url may refer to either a JMS Topic or a JMS Queue.

* optionally modifies the behaviour of the created producer as
  specified by the values of *delivery-mode*, *priority*,
  *time-to-live*, *disable-timestamps*, and *disable-messageIds*
  parameters.

* includes the URI of the producer in the standard HTTP 'Location'
  header of the response.

Consult [JMSSpec]_ for about JMS Producers and their creation,
especially about the various optional arguments to this command.

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola