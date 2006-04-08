================
Rollback Session
================

`back to commands`_

:URL-Pattern: *connection-uri*/session-*nnn*/rollback

:Parameters: None

:Returns: N/A

This GET request rolls back the transactional session at the specified
URI.  This command

* is idempotent, i.e, if sent to a a transactional session that is
  already rolled back, it is ignored.

* is ignored by sessions that are not transacted.

.. _back to commands: ./index.html
