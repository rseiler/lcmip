# LMCiP - Demo

Demo project to demonstrate how the tools work.

* start the Java program with ```lcmip-javaagent``` to get the list of all "used methods"
* generate the list of "all methods" with ```lcmip-jar-analyser```
* generate the summary report with ```lcmip-summary```

How to find all unused classes and all unused methods of the demo module:

    > mvn clean package
    lcmip-demo\target> java -javaagent:..\..\lcmip-javaagent\target\lcmip-javaagent-1.0.jar=method-used.txt#at.rseiler.lcmip.demo -jar lcmip-demo-1.0.jar method-used.txt#at.rseiler.lcmip.demo
    lcmip-demo\target> java -jar ..\..\lcmip-jar-analyser\target\lcmip-jar-analyser-1.0-jar-with-dependencies.jar lcmip-demo-1.0.jar > method-all.txt
    lcmip-demo\target> java -jar ..\..\lcmip-summary\target\lcmip-summary-1.0.jar method-all.txt method-used.txt