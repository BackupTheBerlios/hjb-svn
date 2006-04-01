JMS Providers and Administered Objects
======================================

JMS Providers
-------------

What is a Provider?
+++++++++++++++++++

A *JMS Provider* is a messaging system that implements the JMS API
([JMSSpec]_).  A messaging system vendor provides access to the
Provider by means of a Java library, packaged as a set of one or more
library jars.  JMS programs make use of a JMS Provider's services to
create and process messages.

Often it is possible for JMS programs to access a Provider using
vendor-specific APIs included in a vendor's library jars. However,
this approach is not portable, and it is much more portable to access
all JMS objects via JNDI.  Vendors support this by including custom
JNDI context implementations in their library jars.  In programming
terms, these custom JNDI contexts *define* the different Providers, as
different providers have different custom JNDI contexts.

Providers in HJB
++++++++++++++++

HJB *only* accesses the Provider using JNDI. JNDI allows the
provider's initial context to be configured using a hashtable of
simple values; HJB allows these values to be supplied in a HTTP
requests, so the presence of vendor's library jars on the classpath is
HJB's only dependency on any given vendor.

At runtime in HJB, each Provider

* is represented by a HTTP resource.

* has a unique name (the name has the same constraints a valid java
  identifier).

* has a URL corresponding to the combination of the HJB runtime root
  URL and its name.

* is registered by sending a HTTP POST request to a registration
  URL. The registration URL is a child URL of the actual Provider URL
  The request's parameters are the values used to initialise the
  Provider's JNDI context.

* is uniquely identified by the combination of the values used to
  create its JNDI environment.

* can be removed from the HJB runtime by sending HTTP DELETE request
  to the Provider's resource URL.  This will have the effect of
  stopping any activity on the Provider's connections then removing
  the Provider and any Administered Objects is creates.

JMS Administered Objects
------------------------

What are they?
++++++++++++++

A JMS *Administered Object* represents the configurable resources
hosted by a JMS Provider that are use by a message application.
([JMSSpec]_).

There are two kinds of Administered Object:

Connection Factories
  These create connections that the Provider uses as a transport for
  sending messages.

Destinations
  These are sources and sinks from/to which messages are sent and
  received.

Administered objects are so called because they are set up by an
messaging system administrator.  Often, the library jars supplied by a
vendor will provide a custom API for gaining access to the these
objects. However, the portable way of doing this is using to lookup
the object using JNDI, which is how HJB does it.

Administered Objects in HJB
+++++++++++++++++++++++++++

At runtime in HJB, each Administered Object

* is represented by a HTTP resource.

* has a URL that is obtained by combining the Provider's resource
  name and the actual JNDI name of the object.

* is added to the HJB runtime by sending a HTTP POST request to the
  registration URL for the administered object. The different types
  administered objects have different registration URLs. The
  registration URL is a child URL of the actual objects URL.

* can be removed from the HJB runtime by sending a HTTP DELETE request
  to the object's URL.

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_
