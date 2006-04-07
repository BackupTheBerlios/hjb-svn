==================
Delete Destination
==================

`master command list`_

URL-Pattern

  PROVIDER_URI/jndi-key

Expected-Parameters 

  None

Returns 

  N/A

This DELETE request removes the registration of the destination with
specified jndi-key from the HJB runtime.

* can be invoked multiples times - if the destination is no longer
  registered, the command is ignored.


.. _master command list: ./master-command-list.html
