=====================
Receive From Consumer
=====================

`back to commands`_

:URL-Pattern: *session-uri*/consumer-*nnn*/receive

:Parameters: (optional) timeout

:Returns: A single message from the consumer.

This **POST** request obtains a single message from a vendor's
messaging system via a JMS consumer.  It is returned in the response
encoded as described in `message translation`_.  The command

* times out after the specified number of seconds if the timeout
  parameter is supplied.

* times out after the HJB Servlet timeout period if the timeout is not
  specified.

* on timing out, the response has a HTTP status of *404 Not Found*

.. _back to commands: ./command-list.html

.. _message translation: ../message-translation.html

.. Copyright (C) 2006 Tim Emiola