#!/bin/sh
#
# Use this script to run your program LOCALLY.
#
# Note: Changing this script WILL NOT affect how CodeCrafters runs your program.
#
# Learn more: https://codecrafters.io/program-interface
export JAVA_HOME=$(/usr/libexec/java_home -v 25)
export PATH=$JAVA_HOME/bin:$PATH
export OPENROUTER_API_KEY="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"
export OPENROUTER_BASE_URL=""
set -e # Exit early if any commands fail

# Copied from .codecrafters/compile.sh
#
# - Edit this to change how your program compiles locally
# - Edit .codecrafters/compile.sh to change how your program compiles remotely
(
  cd "$(dirname "$0")" # Ensure compile steps are run within the repository directory
  mvn -q -B package -Ddir=/tmp/codecrafters-build-claude-code-java
)

# Copied from .codecrafters/run.sh
#
# - Edit this to change how your program runs locally
# - Edit .codecrafters/run.sh to change how your program runs remotely
exec java --enable-native-access=ALL-UNNAMED --enable-preview -jar /tmp/codecrafters-build-claude-code-java/codecrafters-claude-code.jar "$@"
