====================
Register Destination
====================

`master command list`_

URL-Pattern

  PROVIDER_URI/jndi-key/register

Expected-Parameters 

  N/A

Returns 

  N/A

Looks up the destination identified by jndi-key in providers JNDI
context and registers it with the HJB runtime. This command

* can be invoked multiples time with the exactly the same jndi-key;
  after the first successful attempt, HJB ignores subsequent
  invocations.

.. _master command list: ./master-command-list.html