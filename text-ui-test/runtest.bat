@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM collect all .java files recursively
if exist sources.txt del sources.txt
for /r "..\src\main\java" %%f in (*.java) do echo %%f>> sources.txt

REM compile all files in source packages into bin
javac -Xlint:none -d ..\bin -cp ..\src\main\java @sources.txt
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)

REM no error here, errorlevel == 0

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ..\bin invicta.InvictaBot < input.txt > ACTUAL.TXT

REM compare the output to the expected output
FC ACTUAL.TXT EXPECTED.TXT
