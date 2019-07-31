@echo off
echo Starting build...
if exist SARC.jar (
    echo Deleting existing binary
    del /q SARC.jar
)
echo Starting maven
echo.
call mvnw.cmd clean compile test assembly:single
if exist target\SARC-1.0-jar-with-dependencies.jar (
    move target\SARC-1.0-jar-with-dependencies.jar .
    rename SARC-1.0-jar-with-dependencies.jar SARC.jar
)
echo.
echo.
echo To run the jar file, use the 'java -jar SARC-1.0-jar-with-dependencies.jar' command
echo To get usage help, run 'java -jar SARC-1.0-jar-with-dependencies.jar -h'