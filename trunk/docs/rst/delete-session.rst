==============
Delete Session
==============

`back to commands`_

:URL-Pattern: *session-uri*/session-*nnn*

:Parameters: None

:Returns: N/A

This **DELETE** request deletes session with the specified URI.  This
command

* closes the Session and removes its JMS objects from the HJB runtime.

  - Session closure and connection termination are performed in
    accordance with the guidelines in [JMSSpec]_.

* is idempotent during the lifetime of the session's connection.  Once
  deleted further requests to the same url are ignored.  If the
  connection is no longer present, it returns a *404 Not Found*
  response.

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola