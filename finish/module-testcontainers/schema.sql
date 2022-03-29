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