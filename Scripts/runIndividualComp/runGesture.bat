@echo off
title Gesture

cd ..\..\Components\Gesture
javac -d . -classpath ..\..\Libraries\* ..\..\Utils\*.java
javac -d . -classpath .;..\..\Libraries\* *.java
start "Gesture" java -classpath .;..\..\Libraries\* NewCreateGesture
