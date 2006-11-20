=======================
List Connection Factory
=======================

`back to commands`_

:URL-Pattern: *provider-uri*/factory-jndi-name/list

:Parameters:

  (optional) *recurse* 

:Returns: a listing of the JMS objects created by connection factory.

This **GET** request generates a response whose body contains a
listing of the URIs of the JMS objects beneath the connection factory
identified by the URI at the root of the request's URL.  The command

* always returns the URIs of any connections that have been created
  by the connection factory

* lists the connections recursively if the *recurse* parameter is
  provided.  The URIs of the current sessions of any connection, and
  of their session objects are included in the body of the response.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola