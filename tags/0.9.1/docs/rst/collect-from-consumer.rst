=====================
Collect From Consumer
=====================

`back to commands`_

:URL-Pattern: *session-uri*/consumer-*nnn*/collect

:Parameters: 

  (optional) timeout

  (optional) number-to-collect

:Returns: Several messages from the consumer.

This **POST** request obtains several messages from a vendor's
messaging system via the `JMS consumer`_ identified by the URI at the
root of the request's URL.  The messages are returned in the response
encoded as described in `message translation`_.  The command

* sends back a response as soon as it collects the specified number of
  messages.

* times out after the specified number of seconds if the timeout
  parameter is supplied, and the required number of messages have not
  been collected.

* times out after the HJB preset timeout period if the timeout is not
  specified.

* sends back a response containing any messages it has collected if it
  times out. 

* sends back a *404 Not Found* response on timing out if *no* messages
  have been retrieved.

.. _JMS consumer: http://java.sun.com/products/jms/tutorial/1_3_1-fcs/doc/prog_model.html#1026102

.. _back to commands: ./command-list.html

.. _message translation: ./message-translation.html

.. Copyright (C) 2006 Tim Emiola