@echo off 

cd ./start/inventory || exit
mvn liberty:stop