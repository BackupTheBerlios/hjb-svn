==================
Delete Destination
==================

`back to commands`_

:URL-Pattern: *provider-uri*/destination/*destination-jndi-name*

:Parameters: None

:Returns: N/A

This **DELETE** request delects the destination identified by the
request's URI from the HJB runtime application.  This command

* is idempotent. I.e., it can be invoked multiples times - if the
  destination is no longer registered, the command is ignored.

.. _back to commands: ./command-list.html

.. Copyright (C) 2006 Tim Emiola