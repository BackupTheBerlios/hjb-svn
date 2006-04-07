===========================
Register Connection Factory
===========================

`master command list`_

URL-Pattern

  PROVIDER_URI/jndi-key/register-connection-factory

Expected-Parameters 

  N/A

Returns 

  N/A

Looks up the connection-factory identified by jndi-key in the
Provider's JNDI context and registers it with the HJB runtime. This
command

* can be invoked multiple times with the exactly the same jndi-key;
  after the first successful attempt, HJB ignores subsequent
  invocations.

.. _master command list: ./master-command-list.html