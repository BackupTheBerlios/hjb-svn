=========================
Delete Connection Factory
=========================

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-key

:Parameters: None

:Returns: N/A

This DELETE request removes the registration of the connection factory
with the specified URI from the HJB runtime. This command

* removes all the connections and sessions created by the connection
  factory. 

    - First the sessions are closed, 

    - then the connections are stopped,

    - finally the JMS objects are removed from the HJB runtime.

    - Session closure and connection termination are performed in
      accordance with the guidelines in [JMSSpec]_.

* can be invoked multiples times - if the connection factory is no
  longer registered, the command is ignored.

.. _back to commands: ./index.html
.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

