=================
Delete Connection
=================

`master command list`_

URL-Pattern

  PROVIDER_URI/jndi-key/connection-*nnn*

Expected-Parameters 

  None

Returns 

  N/A

This DELETE request stops and deletes the connection with the
specified URI.  This command

* removes all the sessions created by the connection.  First the
  sessions are closed and then their JMS objects are removed from the
  HJB runtime.  Session closure and connection termination are
  performed in accordance with the guidelines in [JMSSpec]_.

.. _master command list: ./master-command-list.html
.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_