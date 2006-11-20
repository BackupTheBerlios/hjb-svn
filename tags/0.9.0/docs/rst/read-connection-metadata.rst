========================
Read Connection MetaData
========================

`back to commands`_

:URL-Pattern: provider-uri/jndi-name/connection-*nnn*/metadata

:Parameters: None

:Returns: 

  A message of type 'text/plain' containing the metadata the Provider
  supplies about the connection.

This **GET** request retrieves the provider-supplied metadata about
the connection identified by the URI at the root of request's URL.
This command

* is idempotent during the lifetime of a connection. I.e, it can be
  sent more than once to the same URI, and will return the same
  results.  

* sends back a *404 Not Found* response if the connection is no longer
  available.

* when successful, it sends back a HTTP response containing the
  metadata as a list of key-value pairs in the same textual format
  used to transmit HJB message headers.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola