===========================
Register Connection Factory
===========================

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-key/register-connection-factory

:Parameters: N/A

:Returns: N/A

This **GET** request looks up the connection-factory identified by 
jndi-key in the Provider's JNDI context and registers it with 
the HJB runtime. This command

* can be invoked multiple times with the exactly the same jndi-key;
  after the first successful attempt, HJB ignores subsequent
  invocations.

.. _back to commands: ./command-list.html
