=============
List Provider
=============

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-name/list

:Parameters:

  (optional) *recurse* 

:Returns: a listing of the JMS objects registered to the provider

This **GET** request generates a response whose body contains a
listing of the URIs of the JMS objects beneath the HJB provider
identified by the URI_ at the root of the request's URL_.  The command

* always returns the URIs of any connection factories and destinations
  registered with the provider in HJB.

* lists the connection factories recursively if the *recurse*
  parameter is provided.  The URIs of all current connections created
  by the factory, their sessions and the session's JMS objects are
  included in the body of the response
  
.. _URL: http://en.wikipedia.org/wiki/URL

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola