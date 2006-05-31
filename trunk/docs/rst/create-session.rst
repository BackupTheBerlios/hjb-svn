==============
Create Session
==============

`back to commands`_

:URL-Pattern: CONNECTION_URI/create

:Parameters: 

  (optional) *transacted* 
  
  (optional *acknowledgement-mode*

:Returns:

  Location (HTTP header) : the URI of the created session

This **POST** request creates a new JMS Session.  The command

* can create Sessions that are either transacted or not transacted,
  and use a specific acknowledgement mode, depending on whether the
  necessary parameters are present in the request. See [JMSSpec]_ for
  details of the expected values of these parameters.

* returns the URI of the created session.

.. _back to commands: ./command-list.html

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. Copyright (C) 2006 Tim Emiola