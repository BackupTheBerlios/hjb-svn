====================
Register Destination
====================

`back to commands`_

:URL-Pattern: *provider-url*/destination/*destination-jndi-name*/register

:Parameters: N/A

:Returns: N/A

This **GET** request locates the destination identified by
destination-jndi-name in the provider's JNDI context and registers it
with the HJB runtime.  This command

* can be invoked multiples time with the exactly the same jndi-name;
  after the first successful attempt, HJB ignores subsequent
  invocations.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola