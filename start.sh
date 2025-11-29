#!/bin/bash

# Start Nutriflow service

# Switch to Java 17
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}' | awk -F'.' '{print $1}')
if [ "$JAVA_VERSION" != "17" ]; then
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
    export PATH="$JAVA_HOME/bin:$PATH"
    echo "Switched to Java 17"
fi

# Check port 8080
PORT_PID=$(lsof -ti:8080)
if [ ! -z "$PORT_PID" ]; then
    echo "Port 8080 in use (PID: $PORT_PID)"
    read -p "Kill process? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        kill -9 $PORT_PID
        echo "Process killed"
    else
        exit 1
    fi
fi

cd nutriflow-service || exit 1

echo "Starting service on http://localhost:8080"
echo ""

mvn spring-boot:run -Dspring-boot.run.profiles=local
