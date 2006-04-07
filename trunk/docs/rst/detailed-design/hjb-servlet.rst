HJB Servlet
===========

HJB Servlet serves as the access point for HJB requests.  It acts as a
HTTP gateway server for JMS providers.

Its role can be split into several parts:

* `Command dispatch`_

* `Maintaining references to JMS objects`_

* `Optional storage of provider details`_


Command Dispatch
----------------

HJB Servlet handles POST, GET and DELETE requests if they match
specific URL patterns (see *elsewhere* for a list of the URL patterns).  

* On receiving a matching request, if the request URLs does not map to
  JMS objects that are present in the JMS runtime, or if the request
  cannot be otherwise handled, the servlet responds with *404 NOT
  FOUND*.

* On receiving a request that can be handled, the HJB servlet will
  successfully create a JMS command and schedule its execution. For
  further details on JMS commands see.

* TODO link to **elsewhere**.

Maintining references to JMS Objects
------------------------------------

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
  creation index.  The creation index is the number of instances at
  created/registered by its creator/registrar at the time it was
  created/registered.

This leads to the URI space in the table below. Note that these URIs
denote the *conceptual* location of a JMS object in the the HJB
runtime.  They will not necessarily be URLs that respond to an HTTP
request - in general, HJB's behaviour is implemented by sending
requests to child paths of the these URIs.  (see **elsewhere**)

+--------------------+----------------------------------------------+
|**JMS Object**      |**URL**                                       |
+--------------------+----------------------------------------------+
|Provider            |HJB_ROOT/provider-name                        |
+--------------------+----------------------------------------------+
|Destination         |HJB_ROOT/provider-name/destination-jndi-key   |
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

It will be possible to configure the HJB servlet to remember all the
JMS Providers that are configured, so that they do not need to
reregistered each time servlet starts up.  This feature is not
implemented yet
