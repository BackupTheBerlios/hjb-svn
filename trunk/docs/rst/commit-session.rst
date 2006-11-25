==============
Commit Session
==============

`back to commands`_

:URL-Pattern: *connection-uri*/session-*nnn*/commit

:Parameters: None

:Returns: N/A

This *GET* request commits a transactional_ session at the specified
URI_.  This command

* is idempotent during the lifetime of the session. I.e, if sent to a
  a transactional session that is already committed, it is ignored. 

* sends back a *404 Not Found* response if the session is no longer
  present.

* is ignored by sessions that are not transactional.

.. _transactional: http://foldoc.org/index.cgi?query=transaction&action=Search

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola