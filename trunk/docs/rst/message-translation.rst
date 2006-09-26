===================
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
these message types.  On sending, the message is one parameters of a
POST request.  On receiving, the message is part of the body of a HTTP
response.

The following sections describe how the parts of each of these types
of JMS message are transformed into text in a HTTP request or
response.

Common features
---------------

All JMS messages have a fixed set of standard header fields, which JMS
represents as typed attributes on the JMS message class. In addition,
a message may have application-specific message properties (see
[JMSSpec]_ for a full description).

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_ 

HJB allows the standard headers and message properties to be
transferred in a simple, consistent fashion:

* The header fields are mapped to specific parameter names in a HTTP
  POST request and to field-names in the header section of the HJB
  message within a HTTP response.  The JMS standard headers and their
  corresponding HJB field names are:

  .. class:: display-items

  +----------------+--------------------------+
  |Attribute Name  |HJB field name            |
  +================+==========================+
  |JMSType         |hjb.core.jms.type         |
  +----------------+--------------------------+
  |JMSReplyTo      |hjb.core.jms.replyTo      |
  +----------------+--------------------------+
  |JMSRedelivered  |hjb.core.jms.redelivered  |
  +----------------+--------------------------+
  |JMSPriority     |hjb.core.jms.priority     |
  +----------------+--------------------------+
  |JMSMessageID    |hjb.core.jms.messageId    |
  +----------------+--------------------------+
  |JMSDestination  |hjb.core.jms.destination  |
  +----------------+--------------------------+
  |JMSExpiration   |hjb.core.jms.expiration   |
  +----------------+--------------------------+
  |JMSTimestamp    |hjb.core.jms.timestamp    |
  +----------------+--------------------------+
  |JMSDeliveryMode |hjb.core.jms.deliveryMode |
  +----------------+--------------------------+
  |JMSCorrelationID|hjb.core.jms.correlationId|
  +----------------+--------------------------+
  
* Application-specific properties are mapped in the same way.  They
  becomed additional parameters in the HTTP POST request when sending
  a message, and extra field names in a header section of the HJB
  message within a HTTP response.  The field names are used as
  supplied by the application.

* The Java type of the message standard headers and properties is
  preserved by making the values `HJB-encoded`_.

* If a message attribute is encoded as the wrong type, HJB will ignore
  it.

* When sending messages, HJB clients *must* put standard header and
  application-specific properties in the HTTP POST request as HTTP
  form-encoded parameters whose values are `HJB-encoded`_. HJB decodes
  the parameters, places them into a real JMS message, then sends it.

* On receiving messages, the JMS message attributes, its optional
  properties and the message body are all included in the response as
  text. Note that the message attributes and properties are **not**
  mapped to HTTP response headers.  The message attributes, properties
  and the actual returned message are organised in a simple textual
  format, as follows:

  - The message is in two sections. There is a header section, that
    contains the standard headers and application-specific properties,
    and a body section that contains the message body.  The two
    sections are separated by a

    %<CR> 

    where <CR> is the platform specific line separator.

  - Each standard headers and application-specific property is placed on
    a separate line. Each line consists of

    name=value

    where 'name' is the header/property name and 'value' is its
    value.

  - The body of the message depends on the message type. These are
    described in following sections.

* On receiving multiple messages, e.g., in the HTTP response of
  viewing the messages currently held on a queue, each message is
  returned in the same format as described above, with each one
  separated from the next by
   
  %%<CR>

* When sending messages, HJB clients also send the message body as a
  form-encoded parameter, named

  *message-to-send*

* On both sending and receiving, the HJB message **must** include a
  specific named parameter containing the JMS class the HJB message
  represents.

  - The name of this required field is *hjb_jms_message_interface*

  - Its value **must** be the name of the JMS interface class that the
    message represents. I.e., it should be one of:

    + javax.jms.TextMessage

    + javax.jms.ObjectMessage

    + javax.jms.StreamMessage

    + javax.jms.MapMessage

    + javax.jmx.BytesMessage

* On both sending and receiving, the HJB message **must** include the
  version parameter and value HJB message.

  - the version parameter name is *hjb_message_version*

  - the version parameter value for HJB messages as defined in this
    document is *1.0*

.. _HJB-encoded: ./codec.html

Message Bodies
--------------

The different message types have different textual representations for
their message bodies; these are described in the following subsections.

.. class:: message_desc

Text Message
------------

* The body of the message is sent as the raw text contained in the
  message.  This makes TextMessage the simplest message to process!
  N.B., TextMessages are probably the most widely used JMS message as
  they allow transmission of XML.

* The value of the field 'hjb_jms_message_interface' is 

  - javax.jms.TextMessage

.. class:: message_desc

Object Message
--------------

* The body of the message is the text derived from encoding the byte
  array representation of the java object contained in the Object
  Message.  The byte array is encoded using Base64 encoding. The
  resulting encoded message is in the S-Expression form HJB uses to
  represent byte arrays.

* The value of the field 'hjb_jms_message_interface' is

  - javax.jms.ObjectMessage

.. class:: message_desc

Bytes Message
-------------

* The body of the message is the text derived from treating the entire
  Bytes message content as a single byte array, and encoding it using
  Base64 encoding.  The resulting encoded message text is in the
  S-Expression form HJB uses to represent byte arrays.

* The value of the field 'hjb_jms_message_interface' is

  - javax.jms.BytesMessage

.. class:: message_desc

Map Message
-----------

* The body of the message consists of a line for each name in the
  MapMessage. Each line is as follows:

  name=value<CR>

  where <CR> is the platform specific line separator.  The map
  values are represented in exactly the same as the way message
  headers are written.

* The value of the field 'hjb_jms_message_interface' is

  - javax.jms.MapMessage


.. class:: message_desc

Stream Message
--------------

* The body of the message consists of a line for each value read from
  or written to the Stream Message.  Each line contains an index and
  an encoded value.  The encoded value is some data read from the
  Stream Message.  The index represents the order in which its
  corresponding value was read from the message (on receiving) or the
  order in which it should be written to the message (on
  sending). Each line is as follows:

  index=value<CR>

* The value of the header field 'hjb_jms_message_interface' is

  - javax.jms.StreamMessage

Links
-----

.. [#] `Base64 encoding <http://en.wikipedia.org/wiki/Base64>`_

.. Copyright (C) 2006 Tim Emiola