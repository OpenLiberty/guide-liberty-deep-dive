@echo off

cd .\finish\system || exit
call mvn clean package liberty:create liberty:install-feature liberty:deploy
call mvn liberty:start
