==============
Commit Session
==============

`back to commands`_

:URL-Pattern: *connection-uri*/session-*nnn*/commit

:Parameters: None

:Returns: N/A

This GET request commits the transactional session at the specified
URI.  This command

* is idempotent, i.e, if sent to a a transactional session that is
  already committed, it is ignored.

* is ignored by sessions that are not transactional.

.. _back to commands: ./command-list.html
