==============================
Simple HTTP messaging with HJB
==============================

HJB allows communication with JMS_ messaging systems using nothing
more than HTTP_ requests.  This document demonstrates this capability
using a series of HTTP_ requests that are used to configure a JMS
provider, then send and receive a message, and then finally remove any
JMS objects that have been created.

*N.B.* This example assumes HJB is running on a servlet whose servlet
context URL is something like:

  `http://hjbserver.anywhere.net:8080/messaging/hjb01`  

However, to shorten the URLs used written in the remainder of the
document, this is replaced with the alias HJB_ROOT.

.. _JMS: http://java.sun.com/products/jms

.. _HTTP: http://en.wikipedia.org/wiki/HTTP

Transcript summary
------------------

This is a summary of all the requests executed in order.  The
following sections provide more detail.  Parameters to the request are
shown in the lines immediately following it.

* `Register the administered objects`_

  **POST** HJB_ROOT/transcript/register

    *java.naming.factory.initial*: **com.transcript.InitialContextFactoryObject**

    *java.naming.provider.url*:* **dummy://providers.anywhere.com:1000**

  **GET** HJB_ROOT/transcript/ConnectionFactory/register

  **GET** HJB_ROOT/transcript/destination/hjb_transcript_send/register

  **GET** HJB_ROOT/transcript/destination/hjb_transcript_receive/register

* `Create the runtime JMS objects`_

  **POST** HJB_ROOT/transcript/ConnectionFactory/create

    *username*: **aMessagingProviderUser**

    *password*: **----------------------**

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/create

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/create-consumer

    *destination-uri*: **HJB_ROOT/transcript/destination/hjb_transcript_receive**

    *message-selector*: **transcript-only=true**

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/create-producer

    *destination-uri*: **HJB_ROOT/transcript/destination/hjb_transcript_send**

    *priority*: **(integer 5)**

* `Send and receive messages`_

  **GET** HJB_ROOT/transcript/ConnectionFactory/connection-0/start

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/producer-0/send

    *message-to-send*: **Hello, this is a test message**

    *hjb_jms_message_interface*: **java.jms.TextMessage**

    *hjb_message_version*: **1.0**

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/consumer-0/receive

* `Tidy up after messaging is complete`_

  **GET** HJB_ROOT/transcript/ConnectionFactory/connection-0/stop

  **DELETE** HJB_ROOT/transcript

Register the administered objects
---------------------------------

The `administered objects`_ are provided by the JMS provider.  They
will have been created separately using the provider's administration
interface, and are made available to HJB via registration.

.. _administered objects: ./administered-objects.html

* Register the provider

  The provider is configured by sending a POST request containing the
  parameters used to initialize its JNDI context:

  **POST** HJB_ROOT/transcript/register

    *java.naming.factory.initial*: **com.transcript.InitialContextFactoryObject**

    *java.naming.provider.url*: **dummy://providers.anywhere.com:1000**

* Register a connection factory

  The connection factory is registered by sending a GET request to the
  /register child URL of its URI.  Its URI is consists of the provider
  URI followed by the JNDI name of the connection factory.  This may
  need to be urlencoded to preserve special characters:

  **GET** HJB_ROOT/transcript/ConnectionFactory/register

* Register the destinations used to access messages

  This example uses two destinations, sending from one and receiving
  from the other.  As with the connectionf factories, they are
  registered by sending GET requests to the /register child URL of
  their URIs:

  **GET** HJB_ROOT/transcript/destination/hjb_transcript_send/register

  **GET** HJB_ROOT/transcript/destination/hjb_transcript_receive/register

Create the runtime JMS objects
------------------------------

In this example, the JMS runtime objects are the connection_ and session_ which
respectively identify a particular communication endpoint and set of
active operations; and the consumer_ and producer_ that receive and send messages.

.. _connection: ./connections-sessions.html#jms-connections

.. _session: ./connections-sessions.html#jms-sessions

.. _consumer: ./session-objects.html#message-consumers

.. _producer: ./session-objects.html#message-producers

* Create a JMS connection

  A connection is created by sending a POST request to the /create
  child URL of the connection-factory URI.  In this example,
  authentication parameters are included as they are required to
  enable to access the messaging provider:

  **POST** HJB_ROOT/transcript/ConnectionFactory/create

    *username*: **aMessagingProviderUser**

    *password*: **----------------------**

  The Location header of the response will contain the URI of the
  created JMS connection:

  *Location:* HJB_ROOT/transcript/ConnectionFactory/connection-0

* Create a JMS session

  A session is created by sending a POST request to the /create child
  URL of the connection:

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/create

  The Location header of the response will contain the URI of the
  created JMS session:

  *Location:* HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0

* Create a JMS consumer

  A consumer is created by sending a POST reqeust to the
  /create-consumer child URL of the session.  It includes

  - a message-selector parameter, to restrict messages to those that
    have contain a custom application parameter.
   
  - a destination-uri parameter, to specify which HJB-registered
    destination to from which messages are to be retrieved.

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/create-consumer

    *destination-uri*: **HJB_ROOT/transcript/destination/hjb_transcript_receive**

    *message-selector*: **transcript-only=true**

  The Location header of the response will contain the URI of the
  created JMS consumer:

  *Location:* HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/producer-0

* Create a JMS producer

  A producer is created by sending a POST request to the
  /create-producer child URL of the session. It includes

  - a destination-uri parameter, to specify the HJB-registered
    destination to which messages will be sent.

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/create-consumer
  
    *destination-uri*: **HJB_ROOT/transcript/destination/hjb_transcript_send**

    *priority*: **(integer 5)**

  The Location header of the response will contain the URI of the
  created JMS producer:

  *Location:* HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/producer-0

Send and receive messages
-------------------------

* Start the JMS connection

  The connection is started, by sending a message a GET request to the
  /start child URL of the connection.  This allows the producer and
  consumer to be used send and receive messages respectively.

  **GET** HJB_ROOT/transcript/ConnectionFactory/connection-0/start

* Send a message to the producer

  Sending messages is achieved by posting to the /send child URL of
  the producer.  The message must be formatted according the rules in
  `message translation`_, and included as the parameter
  *message-to-send*.

  In this example, a text message is being sent. In typical messaging
  applications, steps like this may repeated multiple times during the
  lifetime of the application before the application stops.  This
  transcript only performs this step once.

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/producer-0/send

    *message-to-send*: **Hello, this is a test message**

    *hjb_jms_message_interface*: **java.jms.TextMessage**

    *hjb_message_version*: **1.0**

* Receive a message from the consumer

  Receiving messages is achieved by posting to the /receive child URL
  of the consumer.  In this example, no timeout parameter is
  specified, so the HTTP request will wait for the default timeout
  period configured on the HJB server before responding with *404 Not
  Found*.

  **POST** HJB_ROOT/transcript/ConnectionFactory/connection-0/session-0/consumer-0/receive

  As with the sending a message to the producer, in typical messaging
  applications, steps like this may repeated multiple times during the
  lifetime of the application before the application stops.  This
  transcript only performs this step once.

Tidy up after messaging is complete
-----------------------------------

* Stop the connection

  Stopping the connection prevents the producer and consumer from
  being used.  It is achieved by sending a GET request to the /stop
  child URL of the connection. Connections can be restarted after they
  have been stopped. In this example, stopping the connection is
  actually unnecessary, as it will be automatically be stopped in the
  next step

  **GET** HJB_ROOT/transcript/ConnectionFactory/connection-0/stop

* Stop all processing by deleting the provider

  The provider is deleted by sending a DELETE request to the provider
  URI.  In this example it has the effect of 

  - closing the session;
  
  - removing the consumer, producer and session from the HJB runtime application;
  
  - removing the connection from the HJB runtime application; 

  - unregistering both destinations and the connnection factory from
    the HJB runtime application;
  
  - and finally removing the reference to the provider from HJB
    runtime application

  **DELETE** HJB_ROOT/transcript

  It's pretty powerful! Each one of those deletions can actually be
  performed on their own using more specific DELETE URIs.  In real
  applications, it is more likely that the created JMS resources will
  be managed in that way than in this example.

.. _message translation: ./message-translation.html
   
.. Copyright (C) 2006 Tim Emiola