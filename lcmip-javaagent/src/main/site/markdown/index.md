# LMCiP - Core

It's a Java Agent which will logs all method calls of the specified packages.

The output will be written into the specified file. This will happen synchronously to the method call. Therefor it can
impact the performance.

To reduce the performance impact a BufferedWriter with 80kb is used. Additional every 60s the buffer is flushed.
That's needed in order to ensure that all method calls are written to the file.
There is no own thread to call the flush method. So instrumented methods must be called.

Note: if the program processes less than 80kb output and runs less than 60s then nothing will be written to file.


## Usage

The ```lcmip-core``` jar file needs to be attached as Java Agent to the program.

    -javaagent:LCMIP_CORE_PATH=OUTPUT_FILE#PACKAGE_1;PACKAGE_2;PACKAGE_N

* OUTPUT\_FILE: the path to the file to which the output will be written. The OUTPUT\_FILE is separated with a hash (#) from the PACKAGEs.
* PACKAGE\_\*: the packages which should be monitored. The packages are separated with a semicolon (;).

Example

    java -javaagent:..\..\lcmip-javaagent\target\lcmip-javaagent-1.0.jar=method-used.txt#at.rseiler.lcmip.demo -jar lcmip-demo-1.0.jar method-used.txt#at.rseiler.lcmip.demo
