=======
Clients
=======

**2006/05/06** At the moment, HJB is still being tested and
improved. A python client for HJB, PyHJB_ is under development and is
used in the functional testing of HJB.  So far, PyHJB is the only HJB
client. However, many programming languages have good HTTP client
libraries, so watch this space for other HJB clients in the near
future.

PyHJB
-----

`PyHJB`_ is a pure python library for accessing JMS messaging providers
via HJB servers.  It provides a 'pythonic' API to JMS.  The PyHJB
distribution includes scripts that can be used to test a HJB server.

These scripts are designed to be run against an HJB using the JMS
libraries of a new messaging provider, and the results should provide
an indication of how well HJB works with the new provider.  The idea
is that these scripts will evolve into a regression test suite for
HJB.


.. _PyHJB: http://hjb.python-hosting.com

.. Copyright (C) 2006 Tim Emiola