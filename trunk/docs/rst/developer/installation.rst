==============
Installing HJB
==============

* Ensure that all the `prerequisites are met`.

* For a source install, get the source from either the anonymous
  subversion repository or by `downloading` the source distribution
  and unpacking it.
* For a binary install, download the `binary archive` and unzip it.

* The top level directory of the downloaded/unpacked distribution will
  be referred to as *${HJB_ROOT}*.

* For a source install, use ant to build the hjb distributables that
  are available in the binary distribution

  - $ cd ${HJB_ROOT}

  - $ ant dist.main

* Create the hjb_deploy.properties file that will be used by the ant
  build to generate the WebARchive file.

  - *Use the distributed file hjb_deploy.properties.template as an
     example. It is used to produce it contains comments explaining
     what each property is used for and whether why they are required*

* If necessary, stop HJB's servlet container.

* Use ant to create and deploy the WebARrchive file to a location
  where it can be used by the servlet container. *N.B. this location
  should have been included as a property value in the
  hjb_deploy.properties file*

  - $ cd ${HJB_ROOT}

  - $ ant deploy.webapp -Dhjbdeploy.file=/path/to/hjb_deploy.properties

* If necessary, restart HJB's servlet container.

* Test the installation using one of the `HJB client libraries`.

.. _prerequisites are met: ./prerequisites.rst

.. _anonymous subversion: http://hjb.tigris.org/source/browse/hjb

.. _downloading: http://hjb.tigris.org/source/to/be/done

.. _HJB client libraries: ./client.rst