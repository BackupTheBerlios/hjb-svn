================
Send HJB Message
================

`back to commands`_

:URL-Pattern: *session-uri*/producer-*nnn*/send

:Parameters:

  (required) message-to-send

  (required) hjb_jms_message_interface (a message header)

  (optional) destination-url

  (optional) time-to-live

  (optional) priority

  (optional) delivery-mode

  (optional) multiple, used as jms message headers or application
  properties

:Returns: N/A

This **POST** request sends a single message through a vendor's
messaging system using the `JMS producer`_ identified by the URI_ at the
root of the request's URL_.  The message must be encoded as described
in `message translation`_ and included in the POST request as the
*message-to-send* parameter .  The command

* must include a parameter indicating what type of JMS message is
  being sent *(cf hjb_jms_message_interface)*.

* may optionally change the default 'send' behaviour of the producer,
  depending on the values of the *delivery-mode*, *priority*,
  *time-to-live* and *destination-url*.

* converts any recognised JMS header parameters in the request into
  attributes of the JMS message.

* converts any other parameters in the request into application
  properties of the JMS message.

Consult [JMSSpec]_ for further information on sending JMS messages,
particularly about the usage of the various optional arguments that
this command takes.

.. _URL: http://en.wikipedia.org/wiki/URL

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _JMS Producer: http://java.sun.com/products/jms/tutorial/1_3_1-fcs/doc/prog_model.html#1026466 

.. _back to commands: ./command-list.html

.. _message translation: ./message-translation.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola