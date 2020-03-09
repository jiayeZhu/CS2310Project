@echo off
title SuperController

javac -sourcepath ../../Component/SuperController -cp ../../Components/* ../../Components/SuperController/*.java
start "SuperController" /D"../../Components/SuperController" java -cp .;../* CreateSuperController