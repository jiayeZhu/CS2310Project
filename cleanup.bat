@echo off
title CleanUp

del /s Components\*.class
del /s ControllerComponents\*.class
del /s init\*.class
del /s Libraries\*.class
del /s NewSISServer\*.class
del /s SISRemote\*.class
del /s Utils\*.class
pause