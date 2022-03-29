docker build .
docker image ls
docker run -p 5432:5432 -d [IMAGE ID]
docker stop [CONTAINER ID]

Running liberty server: 
mvn liberty:dev