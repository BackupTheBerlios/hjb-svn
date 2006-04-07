================
Rollback Session
================

`master command list`_

URL-Pattern

  CONNECTION_URI/session-*nnn*/rollback

Expected-Parameters 

  None

Returns 

  N/A

This GET request rolls back the transactional session at the specified
URI.  This command

* is idempotent, i.e, if sent to a a transactional session that is
  already rolled back, it is ignored.

* is ignored by sessions that are not transacted.

.. _master command list: ./master-command-list.html