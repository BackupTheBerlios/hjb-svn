====================
Register Destination
====================

`back to commands`_

:URL-Pattern: *provider-url*/jndi-key/register

:Parameters: N/A

:Returns: N/A

This GET Request looks up the destination identified by jndi-key 
in the Provider's JNDI context and registers it with the HJB runtime.
This command

* can be invoked multiples time with the exactly the same jndi-key;
  after the first successful attempt, HJB ignores subsequent
  invocations.

.. _back to commands: ./index.html
