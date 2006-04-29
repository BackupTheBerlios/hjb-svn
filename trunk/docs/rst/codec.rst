=================
Encoding/Decoding
=================

Why encode/decode?
------------------

The JMS API gives access to various pieces of data that HJB clients
might want to use. These include

* JMS messages themselves, which consist of

  - the message headers

  - the message body

* the metadata associated with a open JMS connection

* the parameters used to configure a JMS Provider 

HJB uses HTTP, the text-based protocol of the WWW, to ensure that
it is language-neutral. 

The JMS API is defined in terms of methods that accept Java types, so
any system that interacts with it needs to handle the data in manner
that preserves this type information. 

Actually, many JMS API methods depend on Java's small set of native
types, rather than more sophisticated Java object types, so the task
or preserving type information does not require the need of a complex
object serialization or marshalling protocol.

The simplest way to preserve the Java type information in HTTP's
request and response messages is to provide a textual mapping for each
type in the set of native types present in the JMS API.  This allows
all typed data in the JMS API to be transmitted as text in the HTTP
requests and responses.

Mapped Types
------------

The table below shows how all the native Java types used in the
various JMS API methods are encoded in HJB.

.. class:: display-items

+-------------+---------------------------------------+-------------------+
|Java type    | HJB Encoded form                      | Example           |
+=============+=======================================+===================+
|Byte         |(byte *like java:Byte.toString()*)     |(byte 20)          |
+-------------+---------------------------------------+-------------------+
|Short        |(short *like java:Short.toString()*)   |(short 23456)      |
+-------------+---------------------------------------+-------------------+
|Character    |(char *\\uXXXX*)                       |(char \\u0061)     |
+-------------+---------------------------------------+-------------------+
|Integer      |(int *like java:Integer.toString()*)   |(int 65536)        |
+-------------+---------------------------------------+-------------------+
|Long         |(long *like java:Long.toString()*)     |(long 200000l)     |
+-------------+---------------------------------------+-------------------+
|Float        |(float *like java:Float.toString()*)   |(float 1.567E01)   |
+-------------+---------------------------------------+-------------------+
|Double       |(double *like java:Double.toString()*) |(double 2678.8704) |
+-------------+---------------------------------------+-------------------+
|byte[]       |(base64 *as base64 encoded bytes*)     |(base64 VEVTVA==)  |
+-------------+---------------------------------------+-------------------+
|String       |as is                                  |foobarbaz          |
+-------------+---------------------------------------+-------------------+

Note that aside from the *null* encoding of Strings, the encodings are
simple S-Expressions [#]_.  These should be relatively simple to parse
in any language.

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. [#] `S-Expressions <http://en.wikipedia.org/wiki/S_expression>`_
