==========
View Queue
==========

`master command list`_

URL-Pattern

  SESSION_URI/browser-*nnn*/view

Expected-Parameters 

  None

Returns 

  A set of HJB encoded messages representing the messages currently on
  the queue.

This POST request accesses a JMS Queue, returning the current set of
messages on it, encoded as described in `message translation`_.

.. _master command list: ./master-command-list.html
.. _message translation: ../detailed-design/message-translation.html