===============
Stop Connection
===============

`back to commands`_

:URL-Pattern: *provider-uri*/factory-jndi-name/connection-*nnn*/stop

:Parameters: None

:Returns: N/A

This **GET** request stops the connection identified by the URI at the
root of the request's URL.  This command

* is idempotent during the lifetime of the connection.  I.e, it can be
  sent more than once to the same URL, and is ignored if the connection
  is already stopped.

* sends back a *404 Not Found* response if the connection is no longer present.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola