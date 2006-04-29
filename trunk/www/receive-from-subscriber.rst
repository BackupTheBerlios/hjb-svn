=======================
Receive From Subscriber
=======================

`back to commands`_

:URL-Pattern: *session-uri*/subscriber-*nnn*/receive

:Parameters: (optional) timeout

:Returns: A single message from the subscriber.

This POST request obtains a single message from a vendor's messaging
system via a JMS subscriber.  It is received from HJB encoded as
described in `message translation`_.  The command

* times out after the specified number of seconds if the timeout
  parameter is supplied.

* times out after a HJB preset timeout period if  timeout is
  specified.

* on timing out, the response has a HTTP status of *404 NOT FOUND*

.. _back to commands: ./command-list.html
.. _message translation: ../message-translation.html
