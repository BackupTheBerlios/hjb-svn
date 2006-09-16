===========
HJB Servlet
===========

HJB Servlet is the access point for all HTTP requests to HJB - it acts
as a HTTP gateway server for JMS providers.

It plays several roles:

* `Command dispatch`_

* `Maintaining references to JMS objects`_

* `Optional storage of provider configuration`_


Command Dispatch
----------------

The HJB Servlet handles POST, GET and DELETE requests if they match
specific URL patterns (`the JMS command list`_ contains the full list
of all supported patterns).

* On receiving a matching request, if the request's URL does not map
  to a JMS object that is present in the JMS runtime, or if the HJB
  servlet cannot handle it in another way, the servlet responds with
  *404 Not Found*.

* However, if the matching request can be handled, the HJB servlet
  will create a JMS command and schedule its execution. See `JMS
  Commands`_ for further information.  The return response will be
  derived from the result of executing the command.

Maintaining references to JMS Objects
-------------------------------------

HJB Servlet allows 

* the registration of any administrable JMS objects supplied by the
  JMS provider, i.e., connection factories and destinations.

* the runtime creation of JMS connections, sessions, consumers,
  subscribers, producers and consumers using the JMS API.

Any JMS objects that are created are held in memory in the HJB runtime
application, and the servlet provides access to them via URIs that
match specific patterns.

* The HJB servlet itself is accessible via the hosting servlet
  container on a particular context path and root.  Its URI is denoted
  in rest of the text as HJB_ROOT.

* Each JMS object's path is below the path of the object responsible
  for registering or creating it.

* Where the an object's creator or registrar can create several
  instances of the object, the object's URI is suffixed with its
  creation index.  The creation index is the number of instances
  created/registered by its creator/registrar at the time the object
  itself was created/register.

This leads to the URI space in the table below. Note that these URIs
denote the *conceptual* location of a JMS object in the the HJB
runtime.  They will not necessarily be URLs that respond to an HTTP
request - in general, HJB's actions are implemented by sending
requests to child paths of these URIs (cf. `the JMS command list`_).

.. _the JMS command list: ./command-list.html
.. _JMS commands: ./command-dispatch.html

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
Providers that have been configured, so that they do not need to be
re-registered each time the servlet is restarted.  As of October 2006
(HJB version 0.8.2), this feature is yet to be implemented.

.. Copyright (C) 2006 Tim Emiola