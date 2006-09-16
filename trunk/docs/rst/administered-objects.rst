JMS Providers and Administered Objects
======================================

JMS Providers
-------------

A *JMS Provider* is a messaging system that implements the JMS API
[JMSSpec]_.  From java programs, the provider is accessed by means of
a java library supplied by the messaging system's vendor.  This java
library is typically packaged in one or more library jars.  JMS
programs make use of a JMS provider's services to create and process
messages.

Often, it is possible for java programs to access a provider's
messaging services using vendor-specific APIs supplied by the
vendor. However, this approach is not portable, and is not used by
HJB.

The portable approach is to access all provider objects via JNDI.
Vendors support this by including custom JNDI context implementations
in their library jars.  In fact, from the perspective of a JMS
application, these custom JNDI contexts *define* the different
providers, as different providers have different custom JNDI contexts.

JMS Providers in HJB
++++++++++++++++++++

HJB *always* accesses the provider using JNDI.  JNDI allows the
provider's initial context to be configured using a hashtable of
native type values; HJB supplies these values via HTTP, so the
presence of vendor's library jars on the classpath is the only
dependency HJB has on any given vendor.

In HJB, each registered provider

* has a unique name within HJB. This name must be a valid java
  identifier.

* is represented by a HTTP resource whose URI corresponds to the
  combination of the HJB servlet's root URL and the provider name.

* is registered by sending a HTTP POST request to a registration
  URL. The registration URL is a child URL of the provider URI.  The
  request's parameters are the configuration values needed to
  initialise the provider's JNDI context.

* is uniquely identified by the combination of the values used to
  initialise its JNDI environment.

* can be removed from the HJB runtime by sending a HTTP DELETE request
  to the provider URI.  This will have the effect of stopping any
  activity on the provider's connections, followed by removal of the
  provider and any administered objects that have been registered.

JMS Administered Objects
------------------------

A JMS *Administered Object* represents the configurable resources
hosted by a JMS Provider that are used by a messaging application
[JMSSpec]_.

There are two kinds of administered object:

Connection Factories
  These create connections that a provider uses as a transport for
  sending messages.

Destinations
  These are sources and sinks from/to which messages are sent and
  received.

Administered objects are so called because they are assigned outside
of JMS using the messaging system's administrative interface.  As has
been mentioned earlier, the library jars supplied by a vendor may
provide a vendor-specific API for accessing administered objects, but
the portable way of doing this to locate them using JNDI.

Administered Objects in HJB
+++++++++++++++++++++++++++

In HJB, each Administered Object

* is represented by a HTTP resource whose URI is obtained by combining
  the provider's URI and the actual JNDI name of the object.

* is added to the HJB runtime by sending a HTTP POST request to the
  registration URL for the administered object. Although the two
  different types of administered objects have different registration
  URLs, in both cases it is a child URL of the object's URI.

* can be removed from the HJB runtime by sending a HTTP DELETE request
  to their URI.

.. [JMSSpec] `Java Message Service specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola