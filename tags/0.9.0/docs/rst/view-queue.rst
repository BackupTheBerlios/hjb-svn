==========
View Queue
==========

`back to commands`_

:URL-Pattern: *session-uri*/browser-*nnn*/view

:Parameters: None

:Returns:

  A set of HJB encoded messages representing the messages currently on
  the queue.

This **POST** request returns the current set of messages on the JMS
Queue identified by the URI at the root of the request's URL.  The
messages are encoded as described in `message translation`_.

.. _back to commands: ./command-list.html

.. _message translation: ../message-translation.html

.. Copyright (C) 2006 Tim Emiola