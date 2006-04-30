================
Rollback Session
================

`back to commands`_

:URL-Pattern: *connection-uri*/session-*nnn*/rollback

:Parameters: None

:Returns: N/A

This **GET** request rolls back the transactional session at the specified
URI.  This command

* is idempotent, during the lifetime of the session, i.e, if sent to a a transactional session that is
  already rolled back, it is ignored.  If the session is no longer present, it responds with a *404 Not Found* response.

* is ignored by sessions that are not transacted.

.. _back to commands: ./command-list.html
