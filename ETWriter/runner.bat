@echo off
setlocal

rem Set the path to the directory containing the jar command. Becomes argument
set "JAR_PATH=C:\Program Files\Java\jdk-20\bin"

rem Ensure the directory exists
if not exist "%JAR_PATH%" (
    echo Directory not found: %JAR_PATH%
    exit /b 1
)

if "%~2"=="" (
    exit /b 1
)

START /B /WAIT cmd /c "kotlinc *.kt -include-runtime -d main.jar"

REM # User please ensure resources is presented and trusted
"%JAR_PATH%\jar.exe" uf main.jar -C resources .

java -jar main.jar %1 %2 %3 %4 %5 "%~6" "%~7"