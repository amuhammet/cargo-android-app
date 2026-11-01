@echo off
setlocal

set APP_HOME=%~dp0
set WRAPPER_DIR=%APP_HOME%gradle\wrapper
set JAR=%WRAPPER_DIR%\gradle-wrapper.jar
set PROPS=%WRAPPER_DIR%\gradle-wrapper.properties

if not exist "%PROPS%" (
  echo Missing gradle-wrapper.properties at %PROPS%
  exit /b 1
)

if not exist "%JAR%" (
  if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
  echo Downloading Gradle wrapper jar...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "(New-Object System.Net.WebClient).DownloadFile('https://raw.githubusercontent.com/gradle/gradle/v8.9.0/gradle/wrapper/gradle-wrapper.jar', '%JAR%')"
)

if not exist "%JAR%" (
  echo Failed to download Gradle wrapper jar.
  exit /b 1
)

java -classpath "%JAR%" org.gradle.wrapper.GradleWrapperMain %*
