#!/bin/bash

cd ./finish/system || exit
mvn clean package liberty:create liberty:install-feature liberty:deploy
mvn liberty:start
