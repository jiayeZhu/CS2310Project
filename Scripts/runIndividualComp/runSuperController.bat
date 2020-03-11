@echo off
title SuperController

cd ..\..\Components\SuperController
javac -d . -sourcepath ..\..\Utils\* -cp ..\..\Libraries\* *.java
start "SuperController"  java -cp .;..\*;..\..\Libraries\* CreateSuperController
pause