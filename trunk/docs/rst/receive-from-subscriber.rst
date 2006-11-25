=======================
Receive From Subscriber
=======================

`back to commands`_

:URL-Pattern: *session-uri*/subscriber-*nnn*/receive

:Parameters: (optional) timeout

:Returns: A single message from the subscriber.

This **POST** request obtains a single message from a vendor's
messaging system via the `JMS subscriber`_ identified by the URI_ at the
root of the request's URL_.  It is returned in the response encoded as
described in `message translation`_.  The command

* times out after the specified number of milliseconds if the timeout
  parameter is supplied.

* times out after a HJB preset timeout period if no timeout is
  specified.

* sends back a *404 Not Found* response on timing out.

.. _URL: http://en.wikipedia.org/wiki/URL

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _JMS subscriber: http://java.sun.com/products/jms/tutorial/1_3_1-fcs/doc/advanced.html#1024758

.. _back to commands: ./command-list.html

.. _message translation: ./message-translation.html

.. Copyright (C) 2006 Tim Emiola