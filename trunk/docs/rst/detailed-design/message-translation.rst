Message Translation
===================

JMS Message Types
-----------------

The JMS API supports 5 message types, all of which can be sent and
received by HJB.  They are:

* `Text Message`_

* `Object Message`_

* `Bytes Message`_

* `Map Message`_

* `Stream Message`_

Please refer to [JMSSpec]_ for full descriptions of each these message
types.

HJB clients can send or receive textual representations of any of
these message types.  When sent, the message is contained in the
parameters of a the sending POST request.  When received, the message
is part of the body of a HTTP response.

The following sections describe how the parts of each of these JMS
message types is transformed into text in a HTTP request or response.

Common features
---------------

All JMS messages have a fixed set of standard header fields, which JMS
represents as typed attributes of the JMS message class. In addition,
they may optionally have application-specific message properties (see
[JMSSpec]_ for a full description).  

HJB allows all these values to be transferred in a simple, consistent
fashion.

* The header fields are mapped to specific parameter names in a HTTP
  POST request and to field-assignment lines in a HTTP response.  The
  JMS attributes and their corresponding HJB field names are:
  
  - JMSType          == hjb.core.jms.type

  - JMSReplyTo       == hjb.core.jms.replyTo

  - JMSRedelivered   == hjb.core.jms.redelivered

  - JMSPriorty       == hjb.core.jms.priority

  - JMSMessageID     == hjb.core.jms.messageId

  - JMSDestination   == hjb.core.jms.destination

  - JMSExpiration    == hjb.core.jms.expiration

  - JMSTimestamp     == hjb.core.jms.timestamp

  - JMSDeliveryMode  == hjb.core.jms.deliveryMode

  - JMSCorrelationID == hjb.core.jms.correlationId

* The optional application-specific properties are mapped in the same
  way.  They are additional parameters in the HTTP POST request when
  sending a message, and field-assignment lines in a HTTP response.
  No special transformation is required of the parameter/field names.

* The Java type of the message attributes and properties is preserved
  by ensuring that the values are encoded using HJB's text codec
  rules.

* If a message attribute is encoded as the wrong type, it is ignored.

* When sending messages, HJB clients *must* put attribute values and
  application-specific properties in the HTTP POST request as
  form-encoded parameters. HJB decodes the parameters and places them
  into a real JMS message, then sends it.

* On receiving messages, the JMS message attributes, its optional
  properties and the mmessage body are all included in the response as
  text. The message attributes and properties are **not** mapped to
  HTTP response headers.  The message attributes, properties and the
  actual returned message are organised in a simple textual format
  viz:

  - The message is in two sections. The message attributes and
    properties form the first section of the output, the body of the
    message forms the other section. The two sections are separated by
    a

    + %<CR> 

    where <CR> is a platform specific line separator.

  - The message attributes and optional properties are placed on
    sequential lines. Each line consists of

    + name=value

    where 'name' is the property/attribute name and 'value' is its
    value.

  - The body of the message depends on the message type. This is
    described below.

  - On both sending and receiving, the HJB message **must** include a
    specific name parameter (or field) that contains the version of
    the HJB message.

    + the name of this required field is *hjb.core.version*

    + the value of this field for all HJB messages defined in this
      document is *1.0*

  - On both sending and receiving, the HJB message **must** include a
    specific named parameter (or field-assignment line) containing the
    JMS class the HJB message represents.

    + The name of this required field is *hjb.core.jms-message-class*

    + Its value **must** be the name of the JMS interface class that
    the message represents. I.e., it should be one of:

      - javax.jms.TextMessage

      - javax.jms.ObjectMessage

      - javax.jms.StreamMessage

      - javax.jms.MapMessage

      - javax.jmx.BytesMessage

* On receiving multiple messages, e.g., in the HTTP response of
  viewing a queue of message, each message is returned in the same
  format as described above, with once separated from the next by
   
  - %%<CR>

  where <CR> is a platform specific line separator.

* When sending messages, HJB clients also send the message body as a
  form-encoded parameter, with a specific name

  - *message-to-send*

The different message types have different textual representations for
their message bodys; these are described below. 

Text Message
------------

* The body of the message is sent as the raw text contained in the
  message.  This makes TextMessage the simplest message to process!

* The value of the field 'hjb.core.jms-message-class' is 

  - javax.jms.TextMessage

Object Message
++++++++++++++

* The body of the message is the text derived from encoding the byte
  array representation of the java object contained in the Object
  Message.  The byte array is encoded using Base64 encoding. The
  encoded message is represented using the S-Expression format HJB
  uses to represent byte arrays.

* The value of the field 'hjb.core.jms-message-class' is

  - javax.jms.ObjectMessage

Bytes Message
-------------

* The body of the message is the text derived from treating the entire
  Bytes message content as a single byte array, and encoding it using
  Base64 encoding.  The encoded message is represented using the
  S-Expression output format HJB uses to represent byte arrays.

* The value of the field 'hjb.core.jms-message-class' is

  - javax.jms.BytesMessage


Map Message
-----------

* The body of the message consists of a line for each name in the
  MapMessage. Each line is as follows:

  - name=value<CR>

  where <CR> is a platform specific line separator.  The map values
  are represented in exactly the same as the way message headers are
  written.

* The value of the field 'hjb.core.jms-message-class' is

  - javax.jms.MapMessage

Stream Message
--------------

* The body of the message consists of a line for each value read from
  or written to the Stream Message.  Each line contains an index and
  an encoded value.  The encoded value is some data that is an actual
  part of the Stream Message.  The index represents the order in which
  its corresponding value was read from the message (on receiving) or
  the order in which it should be written to the message (on
  sending). Each line is as follows:

  - index=value

* The value of the header field 'hjb.core.jms-message-class' is

  - javax.jms.StreamMessage


.. [#] `Base64 encoding <http://en.wikipedia.org/wiki/Base64>`_

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_ 
