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

This GET request retrieves the provider supplied metadata about the
connection with the specified URI.  This command

* can be sent more than once to the same URI.

* returns a message containing the metadata expresseds as key value pairs

.. _master command list: ./master-command-list.html