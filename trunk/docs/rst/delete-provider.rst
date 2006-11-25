===============
Delete Provider
===============

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-name

:Parameters: None

:Returns: N/A

This **DELETE** request triggers shutdown of the provider identified
by the request's URI_.  This command

* is idempotent.  I.e., it can be invoked multiple times - if the
  provider is no longer present, the command is ignored.

* removes all connection-factories, destinations, connections and
  sessions that have been registered or created while the provider has
  been registered, as follows:

  - The sessions are closed, after removing any session objects that
    have been created
  
  - Next, the connections are stopped;

  - finally, all JMS administered objects associated with the provider
    are removed, and the provider is deleted from the HJB runtime
    application

  - Session closure and connection termination are performed in	
    accordance with the guidelines in [JMSSpec]_.

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola