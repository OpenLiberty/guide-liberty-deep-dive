docker pull postgres:14.1

docker run -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=adminpwd -p 5432:5432 postgres

logging into postgres: 
docker exec -it [CONTAINERID]  psql -U admin

Creating table and adding sequence: 
CREATE TABLE inventory (
  	inventoryId SERIAL, 
  	inventoryName varchar(100), 
  	inventoryLocation varchar(100), 
  	inventoryTime varchar(100),
  	primary key(inventoryId)
);

CREATE SEQUENCE inventory_id
START 1
INCREMENT 1
OWNED BY inventory.inventoryId;


Running liberty server: 
mvn liberty:dev