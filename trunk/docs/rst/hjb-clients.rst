===========================
Access from other languages
===========================

Allowing communication with JMS messaging systems over HTTP_ removes
the need to develop libraries in other programming languages that
understand the `JMS`_ wire protocol.  Instead, if in a particular
programming language, there is a suitable library for communication
via HTTP_, it can used to access JMS messaging systems.

.. _HTTP: http://tools.ietf.org/html/rfc2616

-----------------
Known HJB Clients
-----------------

**2006/11/17** A python client for HJB, PyHJB_ has been developed and
successfully accesses `JMS`_ messaging systems using HJB.  So far,
PyHJB_ is the only HJB client.  However, many programming languages
have good HTTP client libraries, so watch this space for other HJB
clients in the near future.

PyHJB
-----

`PyHJB`_ is a pure `Python`_ library for accessing JMS messaging
providers over `HTTP`_ using HJB's `RESTful`_ API.  It provides a
'pythonic' API to `JMS`_.  It is `built on top`_ of the python standard
library httplib_.  The `PyHJB distribution`_ includes scripts that can be
used to demonstrate the working with `JMS`_ systems from `Python`_.

.. _PyHJB distribution: http://cheeseshop.python.org/pypi/pyhjb

.. _built on top: http://hjb.python-hosting.com/wiki/BuiltOnHttpLib

.. _httplib: http://docs.python.org/lib/module-httplib.html

.. _RESTful: http://en.wikipedia.org/wiki/REST

.. _Python: http://www.python.org

.. _PyHJB: http://hjb.python-hosting.com

.. _JMS: http://java.sun.com/products/jms

.. Copyright (C) 2006 Tim Emiola