================
Start Connection
================

`back to commands`_

:URL-Pattern: *provider-uri*/factory-jndi-name/connection-*nnn*/start

:Parameters: None

:Returns: N/A

This **GET** request starts the connection identified by the root of
the request's URL.  This command

* is idempotent during the lifetime of a connection, i.e, can be sent
  more than once to the same URL.  If the connection is already
  started, the request is ignored. If the connection is no longer
  present, the command results in a *404 Not Found* response.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola