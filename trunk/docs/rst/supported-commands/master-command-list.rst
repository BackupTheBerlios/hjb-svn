HJB JMS Commands
================

HJB supports these JMS Commands, matching them by using the specified URLs
and HTTP methods 
 
Methods
  P=POST, G=GET, D=DELETE
URIs
  **HJB_ROOT_URI**   is as configured on the servlet container

  **PROVIDER_URI**   is **HJB_ROOT_URI**/*provider-name*

  **FACTORY_URI**    is **PROVIDER_URI**/*factory-jndi-key*

  **CONNECTION_URI** is **FACTORY_URI**/connection-*nnn*

  **SESSION_URI**    is **CONNECTION_URI**/session-*nnn*

  
+----------------------------------+--------------------------------------------------------+
|**JMS Command** (method)          |**URL**                                                 |
+----------------------------------+--------------------------------------------------------+
|`Register Provider`_ (*P*)        |**HJB_ROOT_URI**/*provider-name*/register               |
+----------------------------------+--------------------------------------------------------+
|`Delete Provider`_ (*D*)          |**HJB_ROOT_URI**/*provider-name*                        |
+----------------------------------+--------------------------------------------------------+
|`Register Destination`_ (*G*)     |**PROVIDER_URI**/*jndi-key*/register-destination        |
+----------------------------------+--------------------------------------------------------+
|`Delete Destination`_ (*D*)       |**PROVIDER_URI**/*destination-jndi-key*                 |
+----------------------------------+--------------------------------------------------------+
|`Register Connection Factory`_    |**PROVIDER_URI**/*jndi-key*/register-connection-factory |
|(*G*)                             |                                                        |
+----------------------------------+--------------------------------------------------------+
|`Delete Connection Factory`_ (*D*)|**PROVIDER_URI**/*factory-jndi-key*                     |
+----------------------------------+--------------------------------------------------------+
|`Create Connection`_ (*P*)        |**FACTORY_URI**/create                                  |
+----------------------------------+--------------------------------------------------------+
|`Stop Connection`_ (*G*)          |**FACTORY_URI**/connection-*nnn*/stop                   |
+----------------------------------+--------------------------------------------------------+
|`Start Connection`_ (*G*)         |**FACTORY_URI**/connection-*nnn*/start                  |
+----------------------------------+--------------------------------------------------------+
|`Read Connection MetaData`_ (*G*) |**FACTORY_URI**/connection-*nnn*/metadata               |
+----------------------------------+--------------------------------------------------------+
|`Delete Connection`_ (*D*)        |**FACTORY_URI**/connection-*nnn*                        |
+----------------------------------+--------------------------------------------------------+
|`Create Session`_ (*P*)           |**CONNECTON_URI**/create                                |
+----------------------------------+--------------------------------------------------------+
|`Rollback Session`_ (*G*)         |**CONNECTION_URI**/session-*nnn*/rollback               |
+----------------------------------+--------------------------------------------------------+
|`Commit Session`_ (*P*)           |**CONNECTION_URI**/session-*nnn*/commit                 |
+----------------------------------+--------------------------------------------------------+
|`Delete Session`_ (*D*)           |**CONNECTION_URI**/session-*nnn*                        |
+----------------------------------+--------------------------------------------------------+
|`Create Browser`_ (*P*)           |**SESSION_URI**/create-browser                          |
+----------------------------------+--------------------------------------------------------+
|`View Queue`_ (*P*)               |**SESSION_URI**/browser-*nnn*/view                      |
+----------------------------------+--------------------------------------------------------+
|`Create Consumer`_ (*P*)          |**SESSION_URI**/create-consumer                         |
+----------------------------------+--------------------------------------------------------+
|`Receive From Consumer`_ (*P*)    |**SESSION_URI**/consumer-*nnn*/receive                  |
+----------------------------------+--------------------------------------------------------+
|`Create Subscriber`_ (*P*)        |**SESSION_URI**/create-durable-subscriber               |
+----------------------------------+--------------------------------------------------------+
|`Receive From Subscriber`_ (*P*)  |**SESSION_URI**/subscriber-*nnn*/receive                |
+----------------------------------+--------------------------------------------------------+
|`Create Producer`_ (*P*)          |**SESSION_URI**/create-producer                         |
+----------------------------------+--------------------------------------------------------+
|`Send HJB Message`_ (*P*)         |**SESSION_URI**/producer-*nnn*/send                     |
+----------------------------------+--------------------------------------------------------+

.. _Register Provider: ./register-provider.html
.. _Delete Provider: ./delete-provider.html
.. _Register Destination: ./register-destination.html
.. _Delete Destination: ./delete-destination.html
.. _Register Connection Factory: ./register-connection-factory.html
.. _Delete Connection Factory: ./delete-connection-factory.html
.. _Create Connection: ./create-connection.html
.. _Stop Connection: ./stop-connection.html
.. _Start Connection: ./start-connnection.html
.. _Read Connection MetaData: ./read-connection-metadata.html
.. _Delete Connection: ./delete-connection.html
.. _Create Session: ./create-session.html
.. _Rollback Session: ./rollback-session.html
.. _Commit Session: ./commit-session.html
.. _Delete Session: ./delete-session.html
.. _Create Browser: ./create-browser.html
.. _View Queue: ./view-queue.html
.. _Create Consumer: ./create-consumer.html
.. _Receive From Consumer: ./receive-from-consumer.html
.. _Create Subscriber: ./create-subscriber.html
.. _Receive From Subscriber: ./receive-from-subscriber.html
.. _Create Producer: ./create-producer.html
.. _Send HJB Message: ./send-hjb-message.html
