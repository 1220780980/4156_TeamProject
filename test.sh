#!/bin/bash

# Run tests and checks

# Switch to Java 17
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}' | awk -F'.' '{print $1}')
if [ "$JAVA_VERSION" != "17" ]; then
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
    export PATH="$JAVA_HOME/bin:$PATH"
fi

cd nutriflow-service || exit 1

echo "Running tests..."
mvn clean test

if [ $? -ne 0 ]; then
    echo "Tests failed"
    exit 1
fi

echo ""
echo "Running checkstyle..."
mvn checkstyle:check

echo ""
echo "Generating coverage report..."
mvn jacoco:report

echo ""
echo "Done. Reports in target/site/"
