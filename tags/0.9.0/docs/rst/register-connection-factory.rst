===========================
Register Connection Factory
===========================

`back to commands`_

:URL-Pattern: *provider-uri*/*factory-jndi-name*/register

:Parameters: N/A

:Returns: N/A

This **GET** request locates the connection-factory identified by
*factory-jndi-name* in the provider's JNDI context and registers it
with the HJB runtime. This command

* is idempotent.  I.e., can be invoked multiple times with the exactly
  the same factory-jndi-name; after the first successful attempt, HJB
  ignores subsequent invocations.

* sends back a *404 Not Found* response if no connection factory is
  available in JNDI under the name *factory-jndi-name*.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola