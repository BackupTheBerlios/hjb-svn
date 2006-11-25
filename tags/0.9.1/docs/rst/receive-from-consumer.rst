=====================
Receive From Consumer
=====================

`back to commands`_

:URL-Pattern: *session-uri*/consumer-*nnn*/receive

:Parameters: (optional) timeout

:Returns: A single message from the consumer.

This **POST** request obtains a single message from a vendor's
messaging system via the `JMS consumer`_ identified by the URI_ at the
root of the request's URL_.  It is returned in the response encoded as
described in `message translation`_.  The command

* times out after the specified number of seconds if the timeout
  parameter is supplied.

* times out after the HJB preset timeout period if the timeout is not
  specified.

* sends back a *404 Not Found* response on timing out.

.. _URL: http://en.wikipedia.org/wiki/URL

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _JMS consumer: http://java.sun.com/products/jms/tutorial/1_3_1-fcs/doc/prog_model.html#1026102

.. _back to commands: ./command-list.html

.. _message translation: ./message-translation.html

.. Copyright (C) 2006 Tim Emiola