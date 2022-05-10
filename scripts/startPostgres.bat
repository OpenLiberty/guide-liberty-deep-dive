@echo off

cd .\finish\postgres || exit
docker build -t postgres-sample .
docker run --name postgres-container -p 5432:5432 -d postgres-sample
