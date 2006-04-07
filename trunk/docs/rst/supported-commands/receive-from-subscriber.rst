=======================
Receive From Subscriber
=======================

`master command list`_

URL-Pattern

  SESSION_URI/subscriber-*nnn*/receive

Expected-Parameters 

  (optional) timeout

Returns 

  A single message from the subscriber.

This POST request obtains a single message from a vendor's messaging
system via JMS subscriber.  It is received from HJB encoded as
described in `message translation`_.  The command

* times out after the specified number of seconds if the timeout
  parameter is supplied.

* times out after a HJB preset timeout period if  timeout is
  specified.

* on timing out, the response has a HTTP status of *404 NOT FOUND*

.. _master command list: ./master-command-list.html
.. _message translation: ../detailed-design/message-translation.html