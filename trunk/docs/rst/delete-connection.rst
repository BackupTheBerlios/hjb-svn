=================
Delete Connection
=================

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-name/connection-*nnn*

:Parameters: None

:Returns: N/A

This **DELETE** request stops and deletes the connection identified by
the request's URI_.  This command

* removes all the sessions created by the connection.  

  - First, the sessions' JMS objects are deleted

  - Then the sessions themselves are closed;
   
  - Finally, the connection is closed and removed.

  - Session closure and connection termination are performed in
    accordance with the guidelines in [JMSSpec]_.

* sends backs a *404 Not Found* response if the connection is no
  longer present.

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola