===========
HJB Servlet
===========

HJB Servlet is the access point for HJB requests - it acts as a HTTP
gateway server for JMS providers.

It plays several roles:

* `Command dispatch`_

* `Maintaining references to JMS objects`_

* `Optional storage of provider configuration`_


Command Dispatch
----------------

HJB Servlet handles POST, GET and DELETE requests if they match
specific URL patterns (see `the JMS command list`_ for all the URL
patterns).

* On receiving a matching request, if the request's URL does not map
  to a JMS object that is present in the JMS runtime, or if HJB
  servlet cannot handle it in another way, the servlet responds with
  *404 Not Found*.

* On receiving a request that can be handled, the HJB servlet will
  successfully create a JMS command and schedule its execution. See
  `JMS Commands`_ for further information.

Maintaining references to JMS Objects
-------------------------------------

HJB Servlet allows 

* registration of Provider supplied administrable JMS objects, such as
  connection factories and destinations.

* creation of JMS connections, sessions, consumers, subscribers,
  producers and consumers at runtime using the JMS API.

All of these objects are held in memory by the HJB runtime, which
provides access to them via URIs that have particular patterns.

* HJB servlet is accessible from the servlet container on a particular
  context path and root.  Its URI is denoted below as HJB_ROOT.

* Each object's path is below the path of the object responsible for
  registering or creating it.

* Where the an object's creator or registrar can create several
  instances of the object, the object's URI is suffixed with its
  creation index.  The creation index is the number of instances
  created/registered by its creator/registrar at the time it was
  created/register.

This leads to the URI space in the table below. Note that these URIs
denote the *conceptual* location of a JMS object in the the HJB
runtime.  They will not necessarily be URLs that respond to an HTTP
request - in general, HJB's behaviour is implemented by sending
requests to child paths of the these URIs (cf. `the JMS command list`_).

.. class:: display-items

+--------------------+----------------------------------------------+
|JMS Object          |URI                                           |
+====================+==============================================+
|Provider            |HJB_ROOT/provider-name                        |
+--------------------+----------------------------------------------+
|Destination         |HJB_ROOT/provider-name/destination/jndi-name  |
+--------------------+----------------------------------------------+
|Connection Factory  |HJB_ROOT/provider-name/factory-jndi-name      |
|(*FACTORY_URL*)     |                                              |
+--------------------+----------------------------------------------+
|Connection          |FACTORY_URL/connection-*nnn*                  |
|(*CONNECTION_URL*)  |                                              |
+--------------------+----------------------------------------------+
|Session             |FACTORY_URL/connection-*nnn*/session-*nnn*    |
+--------------------+----------------------------------------------+
|MessageProducer     |CONNECTION_URL/session-*nnn*/producer-*nnn*   |
+--------------------+----------------------------------------------+
|MessageConsumer     |CONNECTION_URL/session-*nnn*/consumer-*nnn*   |
+--------------------+----------------------------------------------+
|DurableSubscriber   |CONNECTION_URL/session-*nnn*/subscriber-*nnn* |
+--------------------+----------------------------------------------+
|QueueBrowser        |CONNECTION_URL/session-*nnn*/browser-*nnn*    |
+--------------------+----------------------------------------------+

Optional Storage of Provider configuration
------------------------------------------

It will be possible to configure the HJB servlet to remember any JMS
Providers that have been configured, so that they do not need to
re-registered each time the HJB servlet is restarted.  This feature is
not implemented yet.

.. _the JMS command list: ./command-list.html
.. _JMS commands: ./command-dispatch.html

.. Copyright (C) 2006 Tim Emiola