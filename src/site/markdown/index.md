# LMCiP - Log Called Methods in Production

Do you want to find out if you have dead code in your project? Than this tool will help you.

It will tell you which classes and methods were never called. But it will only tell you the method name. So it's not
possible to differ between overloaded methods. For me it's not an issue and this way the code is very simple. I hope
that it isn't an issue for you, too.

__Note__: if some method were never called within the monitoring phase doesn't mean that this methods aren't needed.
Only while the monitoring phase nobody needed that methods. To decide how long the monitoring phase should be you can
look into the output file - the date time is logged. Use all the information __wisely__.


## How it works

To track the method calls the ```lcmip-javaagent``` is used. A [Java Agent](http://docs.oracle.com/javase/7/docs/api/java/lang/instrument/package-summary.html)
can instrument the code. This means that the Java Agent will modify the original code to instrument it. In this case
a static method call is added at the beginning of each method. The ```at.rseiler.lcmip.javaagent.MethodLogger#log()```
will be called with the full method name (classname#methodname). The ```MethodLogger``` stores all method names in an
concurrent hash set and writes all new method names to the specified output file. Now you know the used methods.

But how to know the unused methods? You can find that out by using the other two lcmip tools.

* ```lcmip-jar-analyser```: scans the specified jars and lists all methods
* ```lcmip-summary```: takes the file with "all methods" and the file with the "used methods" and generates the summary. Listing all unused classes and unused methods.

The output of the summary report looks like this:

    unused classes: 1/4
    unused methods: 5/12

    unused classes
    at.rseiler.lcmip.demo.UnusedClass

    unused methods
    at.rseiler.lcmip.demo.Main#<init>
    at.rseiler.lcmip.demo.Main$MainInnerClass#unusedMethod
    at.rseiler.lcmip.demo.UnusedClass#<init>
    at.rseiler.lcmip.demo.UnusedClass#unused
    at.rseiler.lcmip.demo.UsedClass#unused


## Usage

* start the Java program with ```lcmip-javaagent``` to get the list of all "used methods"
* generate the list of "all methods" with ```lcmip-jar-analyser```
* generate the summary report with ```lcmip-summary```

How to find all unused classes and unused methods of the demo module:

    > mvn clean package
    lcmip-demo\target> java -javaagent:..\..\lcmip-javaagent\target\lcmip-javaagent-1.0.jar=method-used.txt#at.rseiler.lcmip.demo -jar lcmip-demo-1.0.jar method-used.txt#at.rseiler.lcmip.demo
    lcmip-demo\target> java -jar ..\..\lcmip-jar-analyser\target\lcmip-jar-analyser-1.0-jar-with-dependencies.jar lcmip-demo-1.0.jar > method-all.txt
    lcmip-demo\target> java -jar ..\..\lcmip-summary\target\lcmip-summary-1.0.jar method-all.txt method-used.txt


## Overhead

For a typical Java application the overhead should not be noticeable. Nevertheless test it before you run it in production.


## What is ```<init>``` and ```<clinit>```?

* ```<init>```: it's the initialization block to create an instance (the class member fields).
* ```<clinit>```: it's the static initialization block to setup the class (the static class variables).

[3.9 Specially Named Initialization Methods](http://docs.oracle.com/javase/specs/jvms/se6/html/Overview.doc.html#12174)

> At the level of the Java virtual machine, every constructor (§2.12) appears as an instance initialization method
that has the special name ```<init>```. This name is supplied by a compiler. Because the name ```<init>``` is not a valid
identifier, it cannot be used directly in a program written in the Java programming language. Instance
initialization methods may be invoked only within the Java virtual machine by the invokespecial instruction, and
they may be invoked only on uninitialized class instances. An instance initialization method takes on the access
permissions (§2.7.4) of the constructor from which it was derived.

> A class or interface has at most one class or interface initialization method and is initialized (§2.17.4) by
invoking that method. The initialization method of a class or interface is static and takes no arguments. It has
the special name ```<clinit>```. This name is supplied by a compiler. Because the name ```<clinit>``` is not a valid
identifier, it cannot be used directly in a program written in the Java programming language. Class and interface
initialization methods are invoked implicitly by the Java virtual machine; they are never invoked directly from any
Java virtual machine instruction, but are invoked only indirectly as part of the class initialization process.

### Example

    class AClass {

        private static Log LOG = LogFactory.getLog(); // <clinit>

        private int x = 1;   // <init>

        static {
            // <clinit>
        }

        {
            // <init>
        }

        AClass() {
            // <init>
        }

    }


## Downloads

* [lcmip-jar-analyser-1.0.jar](./builds/lcmip-jar-analyser-1.0.jar)
* [lcmip-javaagent-1.0.jar](./builds/lcmip-javaagent-1.0.jar)
* [lcmip-summary-1.0.jar](./builds/lcmip-summary-1.0.jar)
