================
Send HJB Message
================

`master command list`_

URL-Pattern

  SESSION_URI/producer-*nnn*/send

Expected-Parameters

  (required) message-to-send

  (required) hjb.core.jms-class (a message header)

  (optional) destination-url

  (optional) time-to-live

  (optional) priority

  (optional) delivery-mode

  (optional) multiple, used as jms message headers or application
  properties

Returns 

  N/A

This POST request sends a single message through a vendor's messaging
system via JMS producer.  On its way to the JMS producer the message
is encoded as described in `message translation`_.  The command

* should include a message, and a parameter indicating what type of
  JMS message is being sent.

* optionally changes the default 'send' behaviour of the producer,
  using the values of the *delivery-mode*, *priority*, *time-to-live*
  and *destination-url* parameters.

* converts any recognised JMS header parameters in the request into
  attributes of the JMS message.

* converts any other parameters in the request into application
  properties of the JMS message.

Consult [JMSSpec]_ for further information sending JMS messages,
particularly about the use of the various optional arguments to the
command.

.. _master command list: ./master-command-list.html
.. _message translation: ../detailed-design/message-translation.html