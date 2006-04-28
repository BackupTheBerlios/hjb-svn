===============
Stop Connection
===============

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-key/connection-*nnn*/stop

:Parameters: None

:Returns: N/A

This GET request stops the connection with the specified URI.  This
command

* can be sent more than once to the same URI.  If the connection is
  already stopped, the request is ignored.

.. _back to commands: ./command-list.html
