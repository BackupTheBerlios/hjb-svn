============
List Sesions
============

`back to commands`_

:URL-Pattern: *connection-uri*/session-*nnn*/list

:Parameters:

  (optional) *recurse* 

:Returns: a listing of the JMS session objects available in the session

This **GET** request generates a response whose body contains a list
of the URIs of the session objects created by the session identified
by URI_ at the root of the request's URL_.  The command

  - always returns the URIs of any producers, consumers, browsers and
    durable subscribers created by the session.

  - lists all the accessible properties of the session objects if the
    *recurse* parameter is provided.
  
.. _URL: http://en.wikipedia.org/wiki/URL

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola