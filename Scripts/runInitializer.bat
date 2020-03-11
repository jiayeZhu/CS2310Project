@echo off
title Initializer

javac -d ..\init -sourcepath ..\Utils\* -classpath ..\Libraries\* ..\init\*.java
start "Initializer" /D"../init" java -classpath .;..\Libraries\* NewInitializer