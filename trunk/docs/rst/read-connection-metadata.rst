========================
Read Connection MetaData
========================

`back to commands`_

:URL-Pattern: provider-uri/jndi-key/connection-*nnn*/metadata

:Parameters: None

:Returns: 

  A message of type 'text/plain' containing the metadata the Provider
  supplies about the connection.

This GET request retrieves the provider-supplied metadata about the
connection with the specified URI.  This command

* is idempotent, i.e, it can be sent more than once to the same URI,
  and will return the same results.

* returns a HTTP response containing the metadata as a list of
  key-value pairs, in the same format as is used to display HJB
  message headers.

.. _back to commands: ./command-list.html
