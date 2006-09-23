===============
Stop Connection
===============

`back to commands`_

:URL-Pattern: *provider-uri*/factory-jndi-name/connection-*nnn*/stop

:Parameters: None

:Returns: N/A

This **GET** request stops the connection with identified by the root
of request's URL.  This command

* is idempotent during the lifetime of the connection, i.e, it can be
  sent more than once to the same URL.  If the connection is already
  stopped, the request is ignored.  If the connection is no longer
  present, the command results in a *404 Not Found* response.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola