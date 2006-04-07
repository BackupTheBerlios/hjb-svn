===============
Delete Provider
===============

`master command list`_

URL-Pattern

  PROVIDER_URI/jndi-key/register-destination

Expected-Parameters 

  None

Returns 

  N/A

This DELETE request triggers shutdown of the provider with the
specified URI.  This

* removes all the connection-factories, destinations, connections and
  sessions supported by the Provider. First the sessions are closed,
  then the connections are stopped and finally the JMS objects are
  removed from the HJB runtime.  Session closure and connection
  termination are performed in accordance with the guidelines in
  [JMSSpec]_.

* can be invoked multiples times - if the provider is no longer
  present, the command is ignored.

.. _master command list: ./master-command-list.html
.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_
