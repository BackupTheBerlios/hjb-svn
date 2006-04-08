=================
Delete Connection
=================

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-key/connection-*nnn*

:Parameters: None

:Returns: N/A

This DELETE request stops and deletes the connection with the
specified URI.  This command

* removes all the sessions created by the connection.  

    - First the sessions are closed,
   
    - then the sessions JMS objects are removed from the HJB runtime,

    - finally the connection is closed, and removed.

    - Session closure and connection termination are performed in
      accordance with the guidelines in [JMSSpec]_.

.. _back to commands: ./index.html
.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_
