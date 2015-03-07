# LMCiP - Analyse Jar

Reads one or more jar files and extracts outputs all methods.


## Usage

Use the ```lcmip-core-1.0-jar-with-dependencies.jar``` which is build with ```mvn assembly:assembly```.

    -javaagent:LCMIP_ANALYSE_JAR_PATH=OUTPUT_FILE#PACKAGE_1;PACKAGE_2;PACKAGE_N

Example

    -javaagent:/lcmip/lcmip-core-1.0-jar-with-dependencies.jar=/lcmip/lcmip-used.txt#at.rseiler.lcmip.demo;at.rseiler.lcmip.demo2
