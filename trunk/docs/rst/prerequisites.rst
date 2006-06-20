=============
Prerequisites
=============

HJB is a Java Servlet.  It runs in a servlet container; and to develop
or extend it requires 

 * a Java SDK, 

 * a copy of Sun's JMS 1.1 library (jms.jar) and

 * a number of well-known open-source Java tools.

Running the HJB servlet requires 

 * a Java runtime environment, 

 * a servlet container, 

 * jms.jar and 

 * the JMS jars specific to the messaging provider you want to access.

In addition to these items, all of which are required_, there are some
useful optional_.

Required
--------

Java SDK 1.4 or later
*********************

HJB is coded using the Java SDK 1.4.  If you are thinking of hacking
HJB, get the SDK for your platform.

**N.B.** At the moment, the project purposefully used Java 1.4.  It
does **not** use any features of Java 5, but is 'future compatible'
with it. This is to allow HJB to be extended entirely within
development teams that are constrained to using Java 1.4, a situation
which is still quite common.


Ant 1.6.5 or later
******************

The HJB project is built, tested and deployed using Ant_. Ant should
be downloaded and installed as described on the `ant website
installation page`_. Note that Ant is required for both the source and
binary HJB installations of HJB.  This is because it is used
post-installation task of building war files specific to messaging
providers.

The Ant build scripts used the JUnit_ task for executing test cases.  As such,
the JUnit library file, junit.jar, *must* be present in the Ant classpath when
running the build scripts.  The easiest way to ensure that this is true is to
copy the junit.jar to the ANT_HOME/lib directory of the Ant installation. For
full details, see `installing Ant Optional Tasks`_ on the Ant website.

Library Jars
************

Most of the jars required for developing/extending HJB are included in
the source distribution or are available via anonymous SVN.  The only
one not present is jms.jar, which cannot be distributed with HJB.  It
can be downloaded from the `Sun website`_.

For HJB to communicate with a particular vendors's JMS provider
requires the vendor's JMS library jars.  The vendor's documentataion
should indicate how to obtain these.

Once obtained, the vendor jars will need to be deployed with the HJB
servlet.  Both the HJB source and binary distributions include a build
file that contains an Ant task that simplifies this procedure.


A Servlet Container
*******************

HJB can be deployed against any servlet container, and there are a
quite of few good ones available! During development, it was tested
using the excellent open-source Tomcat_ servlet container.

Optional
--------

Subversion Clients
******************

The latest HJB source can be retrieved and viewed via anonymous
`Subversion access`_, a good `subversion client`_ allows you to view the
latest, greatest version of the source code. Read the `subversion
book`_ for advice on using subversion.

Eclipse
*******

Most of HJB development has been performed using the `Eclipse
SDK`_. HJB's eclipse project file and settings are included in the
subversion repository.

.. _Subversion access: ./repository.html

.. _Ant: http://ant.apache.org

.. _JUnit: http://www.junit.org

.. _installing Ant optional tasks: http://ant.apache.org/manual/install.html#optionalTasks

.. _ant website installation page: http://ant.apache.org/manual/install.htm

.. _Eclipse SDK: http://www.eclipse.org

.. _Tomcat: http://tomcat.apache.org

.. _Sun website: http://java.sun.com/products/jms/docs.html

.. _subversion client: http://subversion.tigris.org/links.html#clients

.. _subversion book: http://svnbook.red-bean.com/

.. Copyright (C) 2006 Tim Emiola