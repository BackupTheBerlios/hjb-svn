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

HJB clients will send/receive textual representations of these
messages.  When sending the message is a parameter of a POST request,
when receiving the message is part of the content of the response.

The following sections describe how each component part of JMS message
is mapped to text in a HJB message.

Common features
+++++++++++++++

All JMS messages have a fixed set of standard header fields, and a set
of optional properties.

* The fixed header fields are mapped to specific parameter/field names
  in HJB responses.
  
  -

  -

  -

* The optional properties are mapped to parameter/fields using the
  property name.

* The Java type of the fixed headers and properties is preserved during
  transmission using HJB's textual encoding.

* If a fixed header is encoded as the wrong type, it is ignored.

* When sending messages, HJB clients should put header fields and
  application-specific properties in the POST request as form-encoded
  parameters. The parameters are decoded and placed in the actual JMS
  message constructed by HJB.

* On receiving messages, the JMS header fields, its properties and the
  body are all included in the response. The headers and properties
  and **not** mapped to response headers.  The headers are properties
  are organised in a simple textual format viz:

  - The headers/properties and body of the message are in two sections
    separated by a 

    + % <CR> 

    where <CR> is a platform specific line separator. The
    headers and properties are placed before the message body.

  - The headers/properties are placed on sequential lines. Each line
    consists of 

    + name '=' value

    where 'name' is the property name and 'value' is its value. The
    spaces between the name the equals sign and value are optional. 

  - The body of the message depends on the message type, and is
    included as is.

  - On both sending and receiving, the HJB message must include a
    special header that states what JMS class the HJB message
    represents.

    + The name of the header is 'hjb.header.jmsclass' 

    + On sending and receiving, its value **must** be the name of the
    JMS interface class that the message represents. I.e., it should
    be one of:

       o javax.jms.TextMessage

       o javax.jms.ObjectMessage

       o javax.jms.StreamMessage

       o javax.jms.MapMessage

       o javax.jmx.BytesMessage

* On receiving multiple messages, e.g., returning the response of
  viewing a queue of message, each message is returned in the same
  format as described above, with each message being separated by a 
   
  - %% <CR>

* When sending messages, HJB clients also send the message body as a
  form-encoded parameter, with a specific name

  - *message-to-send*

  The different message types have different textual representations,
  which are described below.
 

Text Message
++++++++++++

* The body of the message is sent as the raw text in the message.

* The value of the header field 'hjb.header.jmsclass' is 

  - javax.jms.TextMessage

Object Message
++++++++++++++

* The body of the message is the text derived from encoding the
  byte array representation of the java object using Base64
  encoding. The encoded output format is S-Expression format HJB uses
  to represent byte arrays.

* The value of the header field 'hjb.header.jmsclass' is

 - javax.jms.ObjectMessage

Bytes Message
+++++++++++++

* The body of the message is the text derived from treating the entire Bytes
  message a single byte array, and encoding it to the S-Expression
  output format HJB uses to represent byte arrays.


Map Message
+++++++++++


Stream Message
++++++++++++++

.. [#] `Base64 encoding <http://en.wikipedia.org/wiki/Base64>`_

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_
