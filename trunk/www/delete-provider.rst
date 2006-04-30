===============
Delete Provider
===============

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-key/register-destination

:Parameters: None

:Returns: N/A

This **DELETE** request triggers shutdown of the provider with the
specified URI.  This command

* removes all the connection-factories, destinations, connections and
  sessions supported by the Provider. 

  - First the sessions are closed;
  
  - then, the connections are stopped;

  - finally, all JMS objects associated with the provider are
    removed from the HJB runtime.

  - Session closure and connection termination are performed in	
    accordance with the guidelines in [JMSSpec]_.

* is idempotent and can be invoked multiple times - if the provider
  is no longer present, the command is ignored.

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_
