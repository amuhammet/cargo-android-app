#!/usr/bin/env sh
set -eu

APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_DIR="$APP_HOME/gradle/wrapper"
JAR="$WRAPPER_DIR/gradle-wrapper.jar"
PROPS="$WRAPPER_DIR/gradle-wrapper.properties"

if [ ! -f "$PROPS" ]; then
  echo "Missing gradle-wrapper.properties at $PROPS" >&2
  exit 1
fi

if [ ! -f "$JAR" ]; then
  mkdir -p "$WRAPPER_DIR"
  echo "Downloading Gradle wrapper jar..."
  curl -fsSL "https://raw.githubusercontent.com/gradle/gradle/v8.9.0/gradle/wrapper/gradle-wrapper.jar" -o "$JAR"
fi

exec java -classpath "$JAR" org.gradle.wrapper.GradleWrapperMain "$@"
