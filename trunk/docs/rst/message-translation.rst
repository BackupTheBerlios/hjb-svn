===================
Message Translation
===================

JMS Message Types
-----------------

The JMS API supports 5 message types.  All of them can be sent and
received by HJB.  They are:

* `Text Message`_

* `Object Message`_

* `Bytes Message`_

* `Map Message`_

* `Stream Message`_

Please refer to [JMSSpec]_ for full descriptions of these message
types.

HJB clients can send or receive textual representations of any of
these message types.  On sending, the message is encoded and sent in
one parameter of a POST request.  On receiving, the message is part of
the body of a HTTP response.

The following sections describe the different types of JMS message are
transformed into text in a HTTP request or response.

Common features
---------------

All JMS messages have a fixed set of standard header fields, which JMS
represents as typed attributes of the JMS message class. In addition,
a message may have application-specific message properties (see
[JMSSpec]_ for a full description).

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_ 

HJB allows both the standard header values and any application
property values to be represented textually in a simple, consistent
fashion:

* On sending, the JMS header fields are mapped to specific parameter
  names in a HTTP POST request.  On receiving, the header fields are
  are represented using the same specific parameters in the header
  section of the HJB message sent back within HJB's HTTP response.
  The JMS standard headers and their corresponding HJB field names
  are:

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
  
* Application-specific properties are mapped in the same way.  On
  sending a message, they are represented by additional parameters in
  the HTTP POST request; on receiving one, they are any extra fields
  in a header section of the HJB message in the a HTTP response sent
  back by HJB.

* The Java type of the message's standard headers and application
  properties is preserved by making the values `HJB-encoded`_.

* If a JMS standard header is encoded as the wrong Java type, HJB will
  ignore it.

* When sending messages, HJB clients *must* put standard header and
  application-specific properties in the HTTP POST request as HTTP
  form-encoded parameters whose values are `HJB-encoded`_. HJB decodes
  the parameters, places them into a real JMS message, then gives it
  the JMS provider's messaging infrastructure.

* On receiving messages, the JMS message attributes, their optional
  properties, and the message body are all included in the response as
  text. **N.B.** The JMS standard headers and properties are **not**
  mapped to HTTP response headers - this is carefully considered
  design consideration, that makes it easier to extend the HJB message
  format in the future.  The standard headers, properties and the
  actual returned message are organised in a simple textual format, as
  follows:

  - The message is in two sections. There is a header section
    containing the standard headers and application-specific
    properties, and a body section that contains the message body.
    The two sections are separated by a

    %<CR> 

    where <CR> is the platform specific line separator.

  - Each standard header and application-specific property is placed on
    a separate line. Each line consists of

    name=value

    where 'name' is the header/property name and 'value' is its
    value.

  - The body of the message depends on the message type. The different
    way of translating the body of the message are described in the
    following sections.

* On receiving multiple JMS messages in a single response, e.g., in
  the HTTP response sent back by HJB on viewing the messages currently
  held on a JMS queue, each message is returned in the same format as
  described above, with each one separated from the next by
   
  %%<CR>

* When sending messages, HJB clients also send the message body as a
  form-encoded parameter, named

  *message-to-send*

* On both sending and receiving, the HJB message **must** include a
  specific field containing the JMS message type of the HJB message.

  - The name of this required field is *hjb_jms_message_interface*

  - Its value **must** be the name of the JMS interface class for the
    message's JMS type. I.e., it should be one of:

    + javax.jms.TextMessage

    + javax.jms.ObjectMessage

    + javax.jms.StreamMessage

    + javax.jms.MapMessage

    + javax.jmx.BytesMessage

* On both sending and receiving, the HJB message **must** include a
  field containing the version of the HJB message.

  - the version field name is *hjb_message_version*

  - the version value for HJB messages as defined in this
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

  name=value

  The map values are represented in exactly the same as the way
  JMS message headers are written.

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

  index=value

* The value of the header field 'hjb_jms_message_interface' is

  - javax.jms.StreamMessage

Links
-----

.. [#] `Base64 encoding <http://en.wikipedia.org/wiki/Base64>`_

.. Copyright (C) 2006 Tim Emiola