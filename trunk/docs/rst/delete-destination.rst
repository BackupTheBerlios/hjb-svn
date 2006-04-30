==================
Delete Destination
==================

`back to commands`_

:URL-Pattern: *provider-uri*/jndi-key

:Parameters: None

:Returns: N/A

This **DELETE** request removes the registration of the destination with
specified jndi-key from the HJB runtime.  This command

* is idempotent, i.e., it can be invoked multiples times - if the
  destination is no longer registered, the command is ignored.

.. _back to commands: ./command-list.html
