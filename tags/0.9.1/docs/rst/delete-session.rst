==============
Delete Session
==============

`back to commands`_

:URL-Pattern: *session-uri*/session-*nnn*

:Parameters: None

:Returns: N/A

This **DELETE** request deletes the session identified by the
request's URI_.  This command

* closes the Session and removes its JMS objects from the HJB runtime
  application.

  - Session closure and connection termination are performed in
    accordance with the guidelines in [JMSSpec]_.

* sends back a *404 Not Found* response if the session is no longer
  present.

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola