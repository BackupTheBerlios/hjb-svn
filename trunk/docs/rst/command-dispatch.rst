================
Command Dispatch
================

JMS Commands
------------

The JMS API defines several interfaces and classes.  These types
contain various methods, each of which specifies it own set of
arguments. JMS vendors typically implement most of this API, though
it has optional parts that vendors are not obliged to support.

Some JMS methods imply specific timing constraints, others must be run
on specific threads at specific points in their lifecycle.

HJB attempts to support as much of the non-optional JMS API as possible.
To allow the methods in the JMS API to invoked via HTTP in a uniform
fashion, and to support additional operational constraints on some of
the API methods, HJB decouples invocation of the JMS API methods from
the processing of the HTTP request using *JMSCommands*.

JMS Commands

* uniquely map to specific URL patterns recognised by the HJB servlet.

* are constructed with references to all the JMS objects and
  invocation parameters necessary to complete their execution.

* are constructed by one of the `JMS Command Generators`_

* capture any exceptions that occur during execution and keep them as
  faults.

* once executed, provide a status message describing the result of the
  command execution.

* once executed, provide access to objects that may need to be returned
  in the HTTP response, e.g, a received message, or a fault.

* are scheduled for execution on specific threads.

* are queued on their execution threads, so that only one command is
  in execution at any time. (This resolves the threading constraint
  required by JMS Sessions once message processing begins, see
  [JMSSpec]_).

* are an example of using the Command Pattern (cf [DesignPatterns]_).


JMS Command Generators
----------------------

If `JMS Commands`_ are the JMS-facing part of the processing of a HTTP
request by HJB, then *JMS Command Generators* are the HTTP-facing
portion.

There is a one-one relationship between JMS Commands and JMS Command
Generators. The Generator

* determines whether a specified URL is a match for its associated JMS
  Command.

* constructs a JMS Command by 

  - finds any JMS objects it references.

  - collects any command invocation parameters from the HTTP request.

  - uses the JMS objects and invocation parameters to
    create a new JMSCommand instance.

* determines the thread on which the command should be executed.

* once the JMS Command is executed, is used to construct the HTTP
  response containing the status and result of the executed command.

HJBServlet and Command Dispatch
-------------------------------

The HJBServlet ties JMS Command Generators and their commands
together. It

* maintains a list of JMS Command Generators for each HTTP request
  method.

* on receiving a HTTP request on a given URL for a given method, it
  determines if there is a generator matching that URL.

  - if there is none, it returns *404 NOT FOUND* in the HTTP response.

* on finding a matching Command Generator, it uses it to generate the
  JMS Command, then schedules the Command for execution on its
  specific execution thread.

* once execution is complete, it uses the Command Generator to send a
  response based on the final stateOB of the Command.

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. [DesignPatterns] `Design Patterns
   <http://en.wikipedia.org/wiki/Design_Patterns>`_
