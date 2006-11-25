=========================
Delete Connection Factory
=========================

`back to commands`_

:URL-Pattern: *provider-uri*/factory-jndi-name

:Parameters: None

:Returns: N/A

This **DELETE** request removes the connection factory identified by
the request's URI_ from the HJB runtime application. This command

* removes all the connections and sessions created by the connection
  factory. 

  - First the sessions are closed;

  - then, the connections are stopped;

  - finally, the connection factory and the other JMS objects are
    removed from the HJB runtime.

  - Session closure and connection termination are performed in
    accordance with the guidelines in [JMSSpec]_.

* sends back a *404 Not Found* response if the connection factory is
  no longer present.

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola