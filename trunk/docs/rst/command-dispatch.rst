================
Command Dispatch
================

JMS Commands
------------

The JMS API defines several interfaces and classes.  These type
descriptions contain various methods, each of which specifies it own
set of arguments. JMS vendors typically implement most of this API,
though it has optional parts that vendors are not obliged to support.

Some methods in the API sometimes specific constaints on how they are
used at runtime.  E.g., some JMS methods imply specific timing
constraints, others must be run on specific threads at specific points
in a JMS object's lifecycle.

HJB attempts to support as much of the non-optional JMS API as
possible.  To allow the methods in the JMS API to be invoked via HTTP
in a uniform fashion, and to support additional runtime constraints on
those API methods that need it, HJB decouples invocation of the JMS
API methods from the processing of the HTTP request using
*JMS Commands*.

JMS Commands

* uniquely map to specific URL patterns recognised by the HJB servlet.

* are constructed with references to all the JMS objects and
  invocation parameters necessary to complete their execution.

* are constructed by one of the `JMS Command Generators`_.

* capture any exceptions that occur during execution and retain them as
  faults.

* once executed, provide a status message describing the result of the
  command execution.

* once executed, provide access to objects that may need to be returned
  in the HTTP response, e.g, a received message, or a  retained fault.

* are scheduled for execution on specific threads.

* are queued on their execution threads, so that only one command is
  in execution at any time. (This resolves the threading constraint
  required of JMS Sessions once message processing begins as described
  in [JMSSpec]_).

* are inspired by the Command Pattern (cf [DesignPatterns]_), as known
  known in good OO circles, or by first-class functions/closures, as
  known in functional programming!


JMS Command Generators
----------------------

If `JMS Commands`_ are the JMS-facing part of the processing of a HTTP
request by HJB, then *JMS Command Generators* are the corresponding
HTTP-facing part.

There is a one-one relationship between JMS Commands and JMS Command
Generators. Each Generator is tightly coupled to the corresponding JMS
command and

* determines whether a specified URL is a match for its associated JMS
  Command.

* constructs a JMS Command; it 

  - finds any JMS objects it references.

  - collects any command invocation parameters from the HTTP request.

  - uses the JMS objects and invocation parameters to
    create a new JMSCommand instance.

* determines the thread on which the command should be executed.

* is used to construct the HTTP response containing the status and
  result of the executed command, once the JMS command has been
  executed.

The 'Command' - 'Command Generator' pairing is inspired by the
pattern-matching style of logic common in many functional programming
languages e.g., Haskell, Ocaml.

HJBServlet and Command Dispatch
-------------------------------

The HJBServlet ties JMS Command Generators and their commands
together. It

* maintains a list of JMS Command Generators for each HTTP request
  method that HJB can conceivably give a response to.

* on receiving a HTTP request on a given URL for a given method, it
  determines if there is a generator matching that URL.

  - if there is none, it returns *404 NOT FOUND* in the HTTP response.

* on finding a matching Generator, it uses it to generate the JMS
  Command, then schedules the Command for execution on its specific
  execution thread.

* once execution is complete, it uses the Command Generator to send a
  response based on the final state of the Command.

.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. [DesignPatterns] `Design Patterns
   <http://en.wikipedia.org/wiki/Design_Patterns>`_
