=========================
Delete Connection Factory
=========================

`master command list`_

URL-Pattern

  PROVIDER_URI/jndi-key

Expected-Parameters 

  None

Returns 

  N/A

This DELETE request removes the registration of the connection factory
with the specified URI from the HJB runtime, triggering removal of any
active connections and sessions that it created.

* removes all the connections and sessions created by the connection
  factory. First the sessions are closed, then the connections are
  stopped and finally the JMS objects are removed from the HJB
  runtime.  Session closure and connection termination are performed
  in accordance with the guidelines in [JMSSpec]_.

* can be invoked multiples times - if the connection factory is no
  longer registered, the command is ignored.

.. _master command list: ./master-command-list.html
.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

