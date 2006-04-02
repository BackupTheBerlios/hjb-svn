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

In Java, much of this data is strong-typed. One of HJB's main goals is
to be language-neutral, so it was necessary to find a way to preserve
the Java type information. The simplest way to do this is to provide a
simple textual mapping for each type in the small set of fundamental
types present in the JMS API. This approach allows all strongly-typed
data in the JMS API to be encoded as strings in the HTTP requests and
responses handled by HJB.

Mapped Types
------------

The table below shows how all the types present in the various JMS API
methods are encoded in HJB.

+------------+------------------------------------+
|Java Type   | Encoded form	                  |
+------------+------------------------------------+
|Byte	     | (byte *= java:Byte.toString()*)    |
+------------+------------------------------------+
|Short       | (short *= java:Short.toString()*)  |
+------------+------------------------------------+
|Char        | (char *\uXXXX*)                    |
+------------+------------------------------------+
|Integer     | (int *= java:Integer.toString()*)  |
+------------+------------------------------------+
|Long        | (long *= java:Long.toString()*)	  |
+------------+------------------------------------+
|Float       | (float *= java:Float.toString()*)  |
+------------+------------------------------------+
|Double      | (double *= java:Double.toString()*)|
+------------+------------------------------------+
|Byte Array  | (base64 *base64 encoded bytes*)	  |
+------------+------------------------------------+
|String      | as is				  |
+------------+------------------------------------+

* All the encodings are simple S-Expressions[#]_, and should be
  relatively simple to parse in any language that needs to interpret
  them.

* The encodings are listed in the order in which HJB attempts to
  decode data at runtime when it does not know its type beforehand.

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_

.. [#] `S-Expressions <http://en.wikipedia.org/wiki/S_expression>`_