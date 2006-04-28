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


Required
--------

Java SDK 1.4 or later
*********************

HJB is coded using the Java SDK 1.4.  If you are thinking of hacking
HJB, get an the SDK for your platform.

**N.B.** At moment, the project does **not** use any features of
Java5. This is done purposefully, to allow HJB to be extended entirely
within a Java 1.4 development environment if this is the only way
available.


Ant 1.6.5 or later
******************

The HJB project is built, tested and deployed using `Ant`_. Download it
and set it up as described on the `ant website installation
page`_. Note that `Ant`_ is required even with the binary-only HJB
distribution, to support a number of deployment related targets.    

Library Jars
************

Most of the jars required for developing/extending HJB are included in
the source distribution or via anonymous SVN.  The only one not
present is jms.jar, which cannot be distributed with HJB.  It can be
downloaded from the `Sun website`_.

For HJB to communicate with a particular vendors's JMS provider
requires the vendor's JMS library jars.  The vendor's documentataion
should indicate how to obtain these.

Once obtained, these vendor jars will need to be deployed in the HJB
servlet.  The HJB distribution includes has an ant task that
simplifies this procedure.


Servlet Container
*****************

HJB can be deployed against any servlet container, and there are a
quite of few good ones available! During development, it was tested
using the excellent open-source `Tomcat`_ servlet container.

Optional
--------

Subversion
**********

The latest HJB source can be retrieved and viewed via anonymous
`Subversion access`_.

Eclipse
*******

Most of HJB development has been performed using the `Eclipse
SDK`_. The Eclipse project file and settings are included in the
subversion repository.

.. _Subversion access: ./repository.html

.. _Ant: http://ant.apache.org

.. _ant website installation page: http://ant.apache.org/manual/install.htm

.. _Eclipse SDK: http://www.eclipse.org

.. _Tomcat: http://tomcat.apache.org

.. _Sun website: http://java.sun.com/products/jms/docs.html