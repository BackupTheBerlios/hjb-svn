==========
View Queue
==========

`back to commands`_

:URL-Pattern: *session-uri*/browser-*nnn*/view

:Parameters: None

:Returns:

  A set of HJB encoded messages representing the messages currently on
  the queue.

This **POST** request returns the current set of messages on the `JMS
Queue`_ identified by the URI_ at the root of the request's URL_.  The
messages are encoded as described in `message translation`_.

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _URL: http://en.wikipedia.org/wiki/URL

.. _JMS Queue: http://java.sun.com/products/jms/tutorial/1_3_1-fcs/doc/basics.html#1023671

.. _back to commands: ./command-list.html

.. _message translation: ./message-translation.html

.. Copyright (C) 2006 Tim Emiola