@echo off
title Gesture

javac -sourcepath ../../Component/Gesture -cp ../../Components/* ../../Components/Gesture/*.java
start "Gesture" /D"../../Components/Gesture" java -cp .;../* CreateGesture