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

Most these items are required_ as is described below.  There are some
useful optional_ items are well.

Required
--------

Java SDK 1.4 or later
*********************

HJB is coded using the Java SDK 1.4.  If you are thinking of hacking
HJB, get an the SDK for your platform.

**N.B.** At the moment, the project does **not** use any features of
Java 5. This is done purposefully, to allow HJB to be extended
entirely within development teams that are constrained to using Java
1.4,as is still quite common.


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


A Servlet Container
*******************

HJB can be deployed against any servlet container, and there are a
quite of few good ones available! During development, it was tested
using the excellent open-source `Tomcat`_ servlet container.

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

.. _ant website installation page: http://ant.apache.org/manual/install.htm

.. _Eclipse SDK: http://www.eclipse.org

.. _Tomcat: http://tomcat.apache.org

.. _Sun website: http://java.sun.com/products/jms/docs.html

.. _subversion client: http://hjb.tigris.org/servlets/ProjectSource

.. _subversion book: http://svnbook.red-bean.com/