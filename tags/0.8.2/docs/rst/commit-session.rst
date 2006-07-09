==============
Commit Session
==============

`back to commands`_

:URL-Pattern: *connection-uri*/session-*nnn*/commit

:Parameters: None

:Returns: N/A

This *GET* request commits a transactional session at the specified
URI.  This command

* is idempotent during the lifetime of the session, i.e, if sent to a a transactional session that is
  already committed, it is ignored. If the session is no longer present, it returns a *404 Not Found* response

* is ignored by sessions that are not transactional.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola