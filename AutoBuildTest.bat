@echo off
echo Starting build...
call mvn clean compile test assembly:single
echo build successful