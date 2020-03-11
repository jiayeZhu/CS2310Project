@echo off
title SIS Server

cd ..\NewSISServer
javac -d . -sourcepath ..\Utils\* -classpath ..\Libraries\* *.java
java -classpath .;..\Libraries\* SISServer

pause