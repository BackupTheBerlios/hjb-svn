=================
Register Provider
=================

`master command list`_

URL-Pattern

  HJB_ROOT_URI/provider-name/register

Expected-Parameters 

  multiple; they are used to populate the JNDI Context environment

Returns 

  N/A

Uses the parameters in a POST request to register a Provider with
HJB. This command

* uses the POSTed parameters to populate the Hashtable that configures
  the Provider's JNDI Initial Context.

* can be invoked multiple times with the exactly the same paramters;
  after the first successful attempt, HJB ignores subsequent
  invocations.

.. _master command list: ./master-command-list.html