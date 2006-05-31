=================
Create Connection
=================

`back to commands`_

:URL-Pattern: *provider-uri*/factory-jndi-name/create

:Parameters:

  (optional) *username* (with *password*)

  (optional) *password* (with *username*)

  (optional) *clientId*
  
:Returns:

  Location (HTTP header) : the URI of the created connection

This **POST** request creates a new, stopped JMS Connection.  The
command

* uses the security credentials to create the connection if they are
  supplied in the request parameters.

* assigns the connection's client Id *if* one is supplied *and* the provider
  has not already configured the connection with a clientId

* returns the URI of the created connection.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola