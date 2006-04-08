==============
Create Browser
==============

`back to commands`_

:URL-Pattern:

  *session-url*/create-browser

:Parameters: 

  (required) *queue-url*

  (optional) *message-selector*
  
:Returns:

  Location (HTTP header) : the URI of the created queue browser

This POST request creates a new JMS Queue Browser by invoking the
JMS API with parameters derived from the request.  The command

* constructs a queue browser that access the queue described by
  destination-url.

* the destination-url *must* be a destination URI belonging to the
  same Provider as the one in which the subscriber is being created.

* the destination-url *must* refer to a JMS Queue, it must not be a
  JMS Topic.

* optionally creates the browser using the value of
  message-selector

* returns the URI of the created browser.

See [JMSSpec]_ for further information on creating JMS Queue Browsers.

.. _back to commands: ./index.html
.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_
