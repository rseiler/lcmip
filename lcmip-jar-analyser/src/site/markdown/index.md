# LMCiP - Analyse Jar

Reads one or more jar files and extracts all methods.

__Note__: you should not use jar files with dependencies. Otherwise you have to clean the output.


## Usage

Use the ```lcmip-core-1.0-jar-with-dependencies.jar```.

    lcmip-jar-analyser-1.0-jar-with-dependencies.jar JAR_FILES

Example

    java -jar ..\..\lcmip-jar-analyser\target\lcmip-jar-analyser-1.0-jar-with-dependencies.jar lcmip-demo-1.0.jar > method-all.txt
