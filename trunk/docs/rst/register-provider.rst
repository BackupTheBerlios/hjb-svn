=================
Register Provider
=================

`back to commands`_

:URL-Pattern: *hjb-root-uri*/provider-name/register

:Parameters: 

    multiple; used to fill the provider's JNDI Context environment

:Returns: N/A

This **POST** request registers a JMS Provider with HJB. This command

* uses the request parameters to populate a java Hashtable instance
  used to configure the Provider's JNDI Initial Context.

* is idempotent during the lifetime of a particular provider instance.
  I.e., it can be invoked multiple times with the exactly the same
  parameters; after the first successful attempt, HJB ignores
  subsequent invocations.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola