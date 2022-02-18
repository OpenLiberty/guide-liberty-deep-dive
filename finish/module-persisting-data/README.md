docker pull postgres:14.1

docker run -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=adminpwd -p 5432:5432 postgresclear

logging into postgres: 
docker exec -it [CONTAINERID]  psql -U admin

Creating table and adding sequence: 
CREATE TABLE event (
  	eventId SERIAL, 
  	eventName varchar(100), 
  	eventLocation varchar(100), 
  	eventTime varchar(100),
  	primary key(eventId)
);

CREATE SEQUENCE event_id
START 1
INCREMENT 1
OWNED BY event.eventId;


Running liberty server: 
mvn liberty:dev