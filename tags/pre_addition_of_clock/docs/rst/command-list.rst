================
HJB JMS Commands
================

URL Patterns
------------

HJB recognises the JMS Commands in the table below, matching them to the
specified URLs and HTTP request methods.

  .. class:: display-items
  
+----------------------------------+--------------------------------------------------------+
|JMS Command (method)              |URL                                                     |
+==================================+========================================================+
|`Register Provider`_ (*P*)        |*hjb-root-uri*/*provider-name*/register                 |
+----------------------------------+--------------------------------------------------------+
|`Delete Provider`_ (*D*)          |*hjb-root-uri*/*provider-name*                          |
+----------------------------------+--------------------------------------------------------+
|`List Provider`_ (*G*)            |*hjb-root-uri*/*provider-name*/list                     |
+----------------------------------+--------------------------------------------------------+
|`Register Destination`_ (*G*)     |*provider-uri*/destination/*jndi-name*/register         |
+----------------------------------+--------------------------------------------------------+
|`Delete Destination`_ (*D*)       |*provider-uri*/destination/*jndi-name*                  |
+----------------------------------+--------------------------------------------------------+
|`Register Connection Factory`_    |*provider-uri*/*jndi-name*/register                     |
|(*G*)                             |                                                        |
+----------------------------------+--------------------------------------------------------+
|`Delete Connection Factory`_ (*D*)|*provider-uri*/*jndi-name*                              |
+----------------------------------+--------------------------------------------------------+
|`List Connection Factory`_ (*A*)  |*provider-uri*/*jndi-name*/list                         |
+----------------------------------+--------------------------------------------------------+
|`Create Connection`_ (*P*)        |*factory-uri*/create                                    |
+----------------------------------+--------------------------------------------------------+
|`Stop Connection`_ (*G*)          |*factory-uri*/connection-*nnn*/stop                     |
+----------------------------------+--------------------------------------------------------+
|`Start Connection`_ (*G*)         |*factory-uri*/connection-*nnn*/start                    |
+----------------------------------+--------------------------------------------------------+
|`Read Connection MetaData`_ (*G*) |*factory-uri*/connection-*nnn*/metadata                 |
+----------------------------------+--------------------------------------------------------+
|`Delete Connection`_ (*D*)        |*factory-uri*/connection-*nnn*                          |
+----------------------------------+--------------------------------------------------------+
|`List Connection`_ (*G*)          |*factory-uri*/connection-*nnn*/list                     |
+----------------------------------+--------------------------------------------------------+
|`Create Session`_ (*P*)           |*connection-uri*/create                                 |
+----------------------------------+--------------------------------------------------------+
|`Rollback Session`_ (*G*)         |*connection-uri*/session-*nnn*/rollback                 |
+----------------------------------+--------------------------------------------------------+
|`Unsubscribe Client Id`_ (*G*)    |*connection-uri*/session-*nnn*/unsubscribe              |
+----------------------------------+--------------------------------------------------------+
|`Commit Session`_ (*P*)           |*connection-uri*/session-*nnn*/commit                   |
+----------------------------------+--------------------------------------------------------+
|`Delete Session`_ (*D*)           |*connection-uri*/session-*nnn*                          |
+----------------------------------+--------------------------------------------------------+
|`List Session`_ (*G*)             |*connection-uri*/session-*nnn*/list                     |
+----------------------------------+--------------------------------------------------------+
|`Create Browser`_ (*P*)           |*session-uri*/create-browser                            |
+----------------------------------+--------------------------------------------------------+
|`View Queue`_ (*P*)               |*session-uri*/browser-*nnn*/view                        |
+----------------------------------+--------------------------------------------------------+
|`Create Consumer`_ (*P*)          |*session-uri*/create-consumer                           |
+----------------------------------+--------------------------------------------------------+
|`Receive From Consumer`_ (*P*)    |*session-uri*/consumer-*nnn*/receive                    |
+----------------------------------+--------------------------------------------------------+
|`Create Subscriber`_ (*P*)        |*session-uri*/create-durable-subscriber                 |
+----------------------------------+--------------------------------------------------------+
|`Receive From Subscriber`_ (*P*)  |*session-uri*/subscriber-*nnn*/receive                  |
+----------------------------------+--------------------------------------------------------+
|`Create Producer`_ (*P*)          |*session-uri*/create-producer                           |
+----------------------------------+--------------------------------------------------------+
|`Send HJB Message`_ (*P*)         |*session-uri*/producer-*nnn*/send                       |
+----------------------------------+--------------------------------------------------------+

------

Key
 
Methods

- *P* is POST 
- *G* is GET
- *D* is DELETE

URIs

- *hjb-root-uri*   is as configured on the servlet container
- *provider-uri*   is *hjb-root-uri*/*provider-name*
- *factory-uri*    is *provider-uri*/*factory-jndi-name*
- *connection-uri* is *factory-uri*/connection-*nnn*
- *session-uri*    is *connection-uri*/session-*nnn*

.. _Register Provider: ./register-provider.html
.. _Delete Provider: ./delete-provider.html
.. _List Provider: ./list-provider.html
.. _Register Destination: ./register-destination.html
.. _Delete Destination: ./delete-destination.html
.. _Register Connection Factory: ./register-connection-factory.html
.. _Delete Connection Factory: ./delete-connection-factory.html
.. _List Connection Factory: ./list-connection-factory.html
.. _Create Connection: ./create-connection.html
.. _Stop Connection: ./stop-connection.html
.. _Start Connection: ./start-connection.html
.. _Read Connection MetaData: ./read-connection-metadata.html
.. _Delete Connection: ./delete-connection.html
.. _List Connection: ./list-connection.html
.. _Create Session: ./create-session.html
.. _Unsubscribe Client Id: ./unsubscribe-client-id.html
.. _Rollback Session: ./rollback-session.html
.. _Commit Session: ./commit-session.html
.. _Delete Session: ./delete-session.html
.. _List Session: ./list-session.html
.. _Create Browser: ./create-browser.html
.. _View Queue: ./view-queue.html
.. _Create Consumer: ./create-consumer.html
.. _Receive From Consumer: ./receive-from-consumer.html
.. _Create Subscriber: ./create-subscriber.html
.. _Receive From Subscriber: ./receive-from-subscriber.html
.. _Create Producer: ./create-producer.html
.. _Send HJB Message: ./send-hjb-message.html

.. Copyright (C) 2006 Tim Emiola