#!/bin/bash

# Setup check for project environment

echo "Checking environment..."
echo ""

# Check Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}' | awk -F'.' '{print $1}')

if [ "$JAVA_VERSION" != "17" ]; then
    echo "Warning: Java 17 required, currently using Java $JAVA_VERSION"
    echo "Switch with: export JAVA_HOME=\$(/usr/libexec/java_home -v 17)"
    echo ""
else
    echo "Java 17: OK"
fi

# Check Maven
if command -v mvn &> /dev/null; then
    echo "Maven: OK"
else
    echo "Error: Maven not found"
    exit 1
fi

# Check PostgreSQL
echo "Checking PostgreSQL..."
if pg_isready -h localhost -p 5432 &> /dev/null || \
   psql -h localhost -U postgres -c "SELECT 1" &> /dev/null 2>&1; then
    echo "PostgreSQL: OK"
else
    echo "PostgreSQL: Cannot connect (may need to start service)"
fi

echo ""
echo "Setup check complete."
echo "Run ./start.sh to start the service"
