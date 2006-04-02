Command Dispatch
================

JMS Commands
------------

The JMS API defines several interfaces and classes.  These types
contain various methods, each of which specifies it own set of
arguments. JMS vendors typically implement most of this API, though
it has some optional parts that vendors are not obliged to support.

In addition, some JMS methods imply specific timing constraints,
others must be run on specific threads.  

HJB attempts support as much of the non-optional JMS API as possible.
To allow these methods to invoked via HTTP in a uniform fashion, HJB
decouples invocation of all these JMS API methods from the processing
the HTTP request using *JMSCommands*.

JMS Commands

* uniquely map to a specific URL patterns in the HJB servlet.

* are constructed with references to all the JMS objects and
  invocation parameters necessary to complete their execution.

* are constructed by `JMS Command Generators`_

* capture any exceptions that occur during execution and report them
  as faults.

* once executed provide a status message describing the result of the
  command execution.

* once executed provide access to objects that may need to be returned
  in the HTTP response, e.g, a received message.

* are an instance of the Command Pattern (cf [DesignPatterns]_).

* are scheduled for execution on specific threads.

* are queued on their execution threads, so that only one command is
  in execution at any time. (This resolves the threading constraint
  required by JMS Sessions once message processing begins, see
  [JMSSpec]_).


JMS Command Generators
----------------------

If `JMS Commands`_ are the JMS facing part of the processing of HTTP
request by HJB, *JMS Command Generators* are the HTTP facing portion.

There is a 1-1 relation ship between JMS Commands and JMS Command
Generators. The JMS Command Generator

* determines whether a specified URL is a match for its associated JMS
  Command.

* constructs the JMS Command by 

  - finding any JMS objects it references.

  - collecting any invocation parameters from the HTTP request.

  - using JMS objects and invocation parameters to create a new
    JMSCommand instance.

* determines the thread on which the command should be executed.

* once the JMS Command is executed, it uses the status of the command
  and any state it contains to construct a response and send it back
  to the client.


.. [JMSSpec] `Java Message Service specification 1.1
   <http://java.sun.com/products/jms/docs.html>`_

.. [DesignPatterns] `Design Patterns
   <http://en.wikipedia.org/wiki/Design_Patterns>`_
