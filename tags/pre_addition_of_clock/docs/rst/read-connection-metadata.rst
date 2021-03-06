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
the connection whose identified by the root of URL.  This command

* is idempotent during the lifetime of a connection, i.e, it can be
  sent more than once to the same URI, and will return the same
  results.  If the connection is no longer present it responds with a
  *404 Not Found* response.

* returns a HTTP response containing the metadata as a list of
  key-value pairs, in the same format as is used to display HJB
  message headers.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola