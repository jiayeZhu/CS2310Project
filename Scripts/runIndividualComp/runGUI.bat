@echo off
title GUI

cd ..\..\Components\GUI
javac -d . -classpath ..\..\Libraries\*;..\* *.java
start "GUI" java -cp .;..\*;..\..\Libraries\* CreateGUI