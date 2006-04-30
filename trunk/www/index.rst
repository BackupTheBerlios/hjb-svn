=========================
HJB - the HTTP JMS Bridge
=========================

Mission
-------

HJB provides access to JMS resources via HTTP.

Scope
-----

HJB

* is **language-neutral** : it will allow the writing of software
  libraries for communicating with JMS providers in any language
  that has libraries for communicating via HTTP.

* is **RESTful** : it provides a RESTful equivalent to all of the
  non-optional portions of the JMS API including

  - registration of JNDI configured provider resources

  - connection and session management

  - sending and receiving all types of JMS message

Implementation overview
-----------------------

HJB

* is deployed as a servlet (HJBServlet), that can run on any compliant
  Servlet specification 2.4 container.

* is JMS vendor agnostic.

* aims to do one thing well. Its role is to act as an HTTP gateway
  server for JMS resources.  Other potentially useful features are
  deliberately excluded e.g,

  - session management
  - authentication
  - security 

  These can be added by using other HTTP servers in the HTTP request
  processing chain, e.g, another servlet, Apache httpd, Zope, RubyOnRails.

* is extensively unit tested using `JUnit <http://www.junit.org>`_
  and `JMock <http://www.jmock.org>`_

* aspires to be thin and transparent, so that there is very little
  obtruding between HJB client code and JMS. E.g,

  - success for failure of request is indicated by the HTTP response code,

  - it provides detailed, clear messages that are fully internationalized, 

  - all faults are logged on the server,

  - a descriptive status of any request is returned to the HTTP user agent in
    the response headers.


Related Resources
-----------------

* `REST (Representational State Transfer) overview <http://en.wikipedia.org/wiki/REST>`_
  
* `REST vs SOAP <http://www.prescod.net/rest/rest_vs_soap_overview>`_

* `REST paper
  <http://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm>`_

* `REST design advice <http://www.prescod.net/rest/mistakes/>`_

* `Sun's JMS FAQ <http://java.sun.com/products/jms/faq.html>`_

* `JMS specification 1.1
  <http://java.sun.com/products/jms/docs.html>`_

* `JMS documentatation downloads
  <http://java.sun.com/products/jms/docs.html>`_

* `Java Servlet specification 2.4
  <http://java.sun.com/products/servlet/download.html#specs>`_
