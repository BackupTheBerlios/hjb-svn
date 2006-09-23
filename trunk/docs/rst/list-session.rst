============
List Sesions
============

`back to commands`_

:URL-Pattern: *connection-uri*/session-*nnn*/list

:Parameters:

  (optional) *recurse* 

:Returns: a listing of the JMS session objects available in the session

This **GET** request generates a response whose body contains a list
of the URIs of the session objects created by the session whose URI is
the root of the request's URL.  The command

  - always returns the URIs of any producers, consumers, browsers and
    durable subscribers created by the session.

  - lists all the accessible properties of the session objects if the
  *recurse* parameter is provided.
  
.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola