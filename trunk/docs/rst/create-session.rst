==============
Create Session
==============

`back to commands`_

:URL-Pattern: *connection-uri*/create

:Parameters: 

  (optional) *transacted* 
  
  (optional *acknowledgement-mode*

:Returns:

  Location (a standard HTTP response header) : the URI_ of the created session

This **POST** request creates a new JMS Session.  The command

* can create Sessions that are either transacted or not transacted,
  with specific acknowledgement mode, depending on whether the
  necessary parameters are present in the request. See [JMSSpec]_ for
  details of the expected values of these parameters.

* includes the URI of the session in the standard HTTP 'Location'
  header of the response.

.. _URI: http://en.wikipedia.org/wiki/Uniform_Resource_Identifier

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola