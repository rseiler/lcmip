# LMCiP - Core

It's a Java Agent which will logs all method calls.

The output will be written into the specified file. This will happen synchronously to the method call. Therefor it can
impact the performance.

To reduce the performance impact a BufferedWriter with 80kb is used. Additional every 60s the buffer is flushed,
if there is something to flush. That's needed in order to ensure that all method calls are written to the file.
There is no own thread to call the flush method. So instrumented methods must be called.

Note: if the program processes less than 80kb output and runs less than 60s than nothing will be written to file.


## Usage

The lcmip-core needs to be attached as Java Agent to the program.

    -javaagent:LCMIP_CORE_PATH=OUTPUT_FILE#PACKAGE_1;PACKAGE_2;PACKAGE_N

Example

    -javaagent:/lcmip/lcmip-core-1.0.jar=/lcmip/lcmip-used.txt#at.rseiler.lcmip.demo;at.rseiler.lcmip.demo2
