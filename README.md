ISS Arduino
===========

Summary
-------

Code that polls the Unofficial Heavens Above API (uhaapi.com) for information
about when the International Space Station will pass above the Met Office in
the UK and turns this into a number between 0 and 255 for controlling hardware.
 
Architecture
------------

The code is split into two parts:

   * iss.py - the python logic that polls the webserver
   * src/IssSketch.java - the Java logic, using the Processing libraries

There is also a shell script used to run the code.

At runtime, the code works by:

   1. the jython library runs the python code in iss.py
   2. the python code fetches the latest information from the UHAAPI
   3. control is then handed to the Java code, passing in a reference to a
      callback method (getData)  
   4. the Java code then passes control to the Processing library (PApplet)
   5. the draw loop is used to call getData once per second to act on the data

Setup
-----

A. Need to create a lib folder and copy the Processing jars into it:
   lib/core.jar
   lib/RXTXcomm.jar
   lib/serial.jar
The easiest way to find these is to export a Processing sketch as an
application, you'll find the jars in the output folder.


B. The native RXTX serial library also needs to be copied into the top level
directory -- on my Mac I found this at:
/Applications/Processing.app/Contents/Resources/Java/modes/java/libraries/serial/library/macosx/librxtxSerial.jnilib

C. The Java code needs to be compiled by loading the project into Eclipse.
 