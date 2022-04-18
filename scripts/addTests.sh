#!/bin/bash

if [[ -e ./start/inventory/src/test ]]; then
    rm -fr ./start/inventory/src/test
fi

cp -fr ./finish/module-testcontainers/src/test ./start/inventory/src/
cp ./finish/module-testcontainers/pom.xml ./start/inventory/pom.xml
