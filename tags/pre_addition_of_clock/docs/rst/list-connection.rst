===============
List Connection
===============

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-name/connection-*nnn*/list

:Parameters:

  (optional) *recurse* 

:Returns: 

This **GET** request generates a response whose body contains a
listing of the URIs of the JMS objects beneath the connection whose
URI is the root of the request's URL. The command

* always returns the URIs of any current session of the JMS connection

* lists the sessions recursively if the *recurse* parameter is
  provided.  The URIs of all the session's child objects are included
  in the body of the response.

.. _back to commands: ./command-list.html
.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola