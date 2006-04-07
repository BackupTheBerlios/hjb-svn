===============
Stop Connection
===============

`master command list`_

URL-Pattern

  PROVIDER_URI/jndi-key/connection-*nnn*/stop

Expected-Parameters 

  None

Returns 

  N/A

This GET request stops the connection with the specified URI.  This
command

* can be sent more than once to the same URI.  If the connection is
  already stopped, the request is ignored.

.. _master command list: ./master-command-list.html
