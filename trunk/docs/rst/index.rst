=========================
HJB - the HTTP JMS Bridge
=========================

Mission
-------

HJB provides access to JMS_ resources via HTTP_. It provides a
REST_ API equivalent of the `JMS 1.1`_ API, making it possible to
perform JMS operations by accessing JMS_ objects as REST_
resources via HTTP_.

Scope
-----

HJB

* is **language-neutral**: it will allow the writing of software
  libraries for communicating with JMS providers in any language that
  has libraries for communicating over HTTP.
  
  - E.g., it allows `Python`_ programs to communicate with arbitrary `JMS`_
    providers via `PyHJB`_, the python client library for HJB

.. _`Python`: http://www.python.org

.. _`PyHJB`: http://cheeseshop.python.org/pypi/pyhjb


* is `RESTful`_: it provides a `RESTful`_ equivalent to all of the
  non-optional portions of the `JMS 1.1`_ API including

  - registration of resources administered by the messaging provider

  - connection and session management

  - sending and receipt of all types of JMS message

.. _`JMS`: http://java.sun.com/products/jms/

.. _`JMS 1.1`: http://java.sun.com/products/jms/docs.html

.. _`HTTP`: http://tools.ietf.org/html/rfc2616

.. _`REST`: http://en.wikipedia.org/wiki/REST

.. _`RESTful`: http://rest.blueoxen.net/cgi-bin/wiki.pl?RESTfulDesign


Implementation overview
-----------------------

HJB

* is deployed as a servlet (HJBServlet), that can run on any servlet
  container compliant with version 2.4 of the `Java Servlet
  specification`_.

* will work with any messaging vendor that provides a `JMS 1.1`_
  interface.

* aims to do one thing well. Its role is to act as an `HTTP`_ gateway
  server allowing access to JMS objects as `REST`_ resources. Other
  potentially useful features are deliberately excluded, e.g,

  - HTTP session management

  - authentication and authorization

  These can be added by using other HTTP servers in the HTTP request
  processing chain, e.g, another servlet, `Apache httpd`_, Zope_,
  RubyOnRails_, AddYourFavouriteWebServer here.

.. _`Apache httpd`: http://httpd.apache.org

.. _`Zope`: http://www.zope.org

.. _`RubyOnRails`: http://www.rubyonrails.org

* is very robust as its extensively unit tested using `JUnit
  <http://www.junit.org>`_ and `JMock <http://www.jmock.org>`_.
  Its Emma_ `coverage report`_ is available `online`_)

.. _Emma: http://emma.sourceforge.net/

.. _coverage report:

.. _online: ./instr/coverage.html

* aspires to make the best possible use of the HTTP protocol, and to
  be a thin, transparent layer so that there is very little obtruding
  between HJB client code and the JMS API. E.g,

  - success or failure of each request is indicated by the `HTTP
    response code`_,

  - a descriptive status of each request is returned to the HTTP user
    agent in the response header,

  - status messages are detailed, clear and fully `internationalized`_, 

  - all faults are logged on the server and match the status messages
    returned to the user agent.

.. _HTTP Response Code: http://en.wikipedia.org/wiki/List_of_HTTP_status_codes

.. _Java Servlet Specification : http://java.sun.com/products/servlet/download.html#specs

.. _internationalized: http://en.wikipedia.org/wiki/I18n

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

.. Copyright (C) 2006 Tim Emiola
