Encoding/Decoding
=================

Why is it needed?
-----------------

The JMS API provides access to various pieces of data that HJB
clients may choose to read or create. These include

* JMS messages themselves, which consist of

  - the message headers

  - the message body

* the metadata associated with a open JMS connection

* the parameters used to configure a JMS Provider 

JMS is a java API, as such this data needs to be strongly-typed. One
of HJB's main goals is to be language-neutral, so it was necessary to
find a way to preserve the Java type information. The simplest way to
do this is to provide a simple textual mapping for each type in the
small set of fundamental types present in the JMS API. This approach
allows all strongly-typed data in the JMS API to be encoded as strings
in the HTTP requests and responses handled by HJB.

Mapped Types
------------

The table below shows how all the types present in the various JMS API
methods are encoded in HJB.

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

* Aside for the *null* encoding for raw text strings, the encodings
  are simple S-Expressions [#]_, which should be relatively simple to
  parse in any language that needs to interpret them.

* The encodings are listed in the order in which HJB attempts to
  decode objects at runtime when it does not know its type beforehand.

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. [#] `S-Expressions <http://en.wikipedia.org/wiki/S_expression>`_