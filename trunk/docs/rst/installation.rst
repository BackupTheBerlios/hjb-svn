==============
Installing HJB
==============

* Ensure that all the `installation prerequisites`_ are met.

* To install from a source-only distribution, get the source from
  either the `anonymous subversion`_ repository or by `downloading`_
  the source distribution and unpacking it.

* To install from a binaries-only distribution, download the `binary
  archive`_ and unzip it.

* In the rest of note, the top level directory of the
  downloaded/gunpacked distribution will is referred to as
  *${HJB_ROOT}*.

* If a source distribution has been downloaded, use ant_ to build the
  hjb binaries that are present in the binary distribution

  - $ cd ${HJB_ROOT}

  - $ ant dist.bin

* An HJB war (Web ARchive) file for the particular JMS messaging
  provider HJB will communicate with needs to be generated.  A war is
  a deployable unit that is added to a servlet container and
  executed. The HJB war is the physical deployment unit that
  communicates with a given Provider.  HJB's build script includes an
  ant task that simplifies the creation of the war file, as follows:

  - Create a hjb_deploy.properties file that contains various
    properties used by the ant build script to generate the war file.

  - Use the distribution file hjb_deploy.properties.template as an
    example. It contains comments explaining what each property means.

* The ant script creates the war file, but can also deploy it to the
  servlet container.  If the war file is to be deployed as well as
  built, it may be necessary to stop the target servlet container
  during deployment.

* Use the *dist.webapp* ant target to create the war file without
  deploying it.  The war file should be then be deployed in an
  appropriate manner to the target servlet container.

  - $ cd ${HJB_ROOT}

  - $ ant dist.webapp -Dhjb.deploy.properties=/path/to/hjb_deploy.properties   

OR


* Use the *deploy.webapp* ant target to create the war file and copy
  it to a location where it can be used by the target servlet
  container (*N.B. the location is one of the property values assigned
  in the hjb_deploy.properties file*):

  - $ cd ${HJB_ROOT}

  - $ ant deploy.webapp -Dhjb.deploy.properties=/path/to/hjb_deploy.properties

* If the servlet container is not running, restart it.

* Test the installation using one of the `HJB client libraries`_.

.. _installation prerequisites: ./prerequisites.html

.. _ant: http://ant.apache.org

.. _anonymous subversion: http://hjb.tigris.org/source/browse/hjb

.. _downloading: http://prdownload.berlios.de/hjb/hjb-src-0.8.1beta.jar

.. _binary archive: http://prdownload.berlios.de/hjb/hjb-bin-0.8.1beta.jar

.. _HJB client libraries: ./hjb-clients.html

.. Copyright (C) 2006 Tim Emiola