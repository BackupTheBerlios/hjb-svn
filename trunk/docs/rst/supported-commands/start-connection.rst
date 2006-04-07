=================
Start Connection
=================

`master command list`_

URL-Pattern

  PROVIDER_URI/jndi-key/connection-*nnn*/start

Expected-Parameters 

  None

Returns 

  N/A

This GET request starts the connection with the specified URI.  This
command

* can be sent more than once to the same URI.  If the connection is
  already started, the request is ignored.

.. _master command list: ./master-command-list.html
