========================
Read Connection MetaData
========================

`master command list`_

URL-Pattern

  PROVIDER_URI/jndi-key/connection-*nnn*/metadata

Expected-Parameters 

  None

Returns 

  A message of type 'text/plain' containing the metadata the Provider
  supplies about the connection.

This GET request retrieves the provider-supplied metadata about the
connection with the specified URI.  This command

* is idempotent, i.e, it can be sent more than once to the same URI,
  and will return the same results.

* returns a message containing the metadata as a list of key-value
  pairs, in the same format as is used in HJB message headers.

.. _master command list: ./master-command-list.html