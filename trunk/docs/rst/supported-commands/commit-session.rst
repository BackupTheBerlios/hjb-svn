==============
Commit Session
==============

`master command list`_

URL-Pattern

  CONNECTION_URI/session-*nnn*/commit

Expected-Parameters 

  None

Returns 

  N/A

This GET request commits the transactional session at the specified
URI.  This command

* is idempotent, i.e, if sent to a a transactional session that is
  already committed, it is ignored.

* is ignored by sessions that are not transactional.

.. _master command list: ./master-command-list.html