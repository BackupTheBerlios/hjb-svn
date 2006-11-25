================
Rollback Session
================

`back to commands`_

:URL-Pattern: *connection-uri*/session-*nnn*/rollback

:Parameters: None

:Returns: N/A

This **GET** request rolls back the transactional_ session identified
by the URI_ at the root of the request's URL_.  This command

* is idempotent during the lifetime of the session. I.e, if sent to a
  a transactional session that is already rolled back, it is ignored.
  
* sends back a *404 Not Found* response if the session is no longer
  present.

* is ignored by sessions that are not transactional.

.. _transactional: http://foldoc.org/index.cgi?query=transaction&action=Search

.. _URL: http://en.wikipedia.org/wiki/URL

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola