================
Start Connection
================

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-key/connection-*nnn*/start

:Parameters: None

:Returns: N/A

This **GET** request starts the connection with the specified URI.
This command

* is idempotent during the lifetime of a connection and can be sent
  more than once to the same URI.  If the connection is already
  started, the request is ignored. If the connection is no longer
  present, it results in a *404 Not Found* response.

.. _back to commands: ./command-list.html
