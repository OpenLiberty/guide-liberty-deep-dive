// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.deepdive.rest;

//imports for testing
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

//imports for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//imports for testcontainers
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

import io.openliberty.deepdive.rest.model.Inventory;
import io.openliberty.deepdive.rest.resources.SystemResourceInterface;
import jakarta.json.JsonArray;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@Testcontainers
public class InventoryDaoIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryDaoIT.class);
    private static final String APP_PATH = "/inventory/api";
    private static final String POSTGRESS_HOST = "postgress";

    public static SystemResourceInterface systems;

    public static Network network = Network.newNetwork();

    @Container
    public static GenericContainer<?> mongodb 
                = new GenericContainer<>("postgress:v1")
                    .withNetwork(network)
                    .withExposedPorts(5432)
                    .withNetworkAliases(POSTGRESS_HOST)
                    .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    @Container
    public static LibertyContainer libertyContainer
                        = new LibertyContainer("inventory:v1")
                            .withExposedPorts(9080)
                            .withNetwork(network)
                            .waitingFor(Wait.forHttp("/health/ready"))
                            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    public static void setupLibertyContainer() {
        System.out.println("INFO: Starting Liberty Container setup");
        systems = libertyContainer.createRestClient(
            SystemResourceInterface.class, APP_PATH);
    }

    public static void addDataToDatabase() {
        System.out.println("INFO: Adding data to database");
        systems.addNewInventory("1stInv", "Morning", "Southampton");
        systems.addNewInventory("2ndInv", "Lunch", "London");
        systems.addNewInventory("3ndInv", "Afternoon", "Manchester");
        systems.addNewInventory("4ndInv", "Evening", "Birmingham");
        System.out.println("INFO: Printing all inventory data: " 
            + systems.getInventories());
    }

    @BeforeAll
    public static void setupTestClass() {
        setupLibertyContainer();
        addDataToDatabase();
    }

    @Test
    public void getInventorySize() {
        System.out.println(
            "TEST: Testing getting the size of the list of inventories");

        Integer invSize  = systems.getInventories().size();
        System.out.println("Inventory size: " + invSize);
        assertNotNull(invSize);
    }

    @Test
    public void getAllInventories() {
        System.out.println(
            "TEST: Testing getting all inventories");

        JsonArray inventories = systems.getInventories();
        System.out.println("Get all inventories: " + inventories);
        assertNotNull(inventories.size());
    }

    @Test
    public void getInventory() {
        System.out.println(
            "TEST: Testing geting a specific inventory");

        System.out.println("Printing inventory: " + systems.getInventory(52));
        assertNotNull(systems.getInventory(52));
    }

    @Test
    public void updateInventory() {
        System.out.println(
            "TEST: Testing Updating a inventory");

        Jsonb jsonb = JsonbBuilder.create();

        Inventory inv = new Inventory("updated", "USA", "morning");
        System.out.println("Testing updating inventory");
        systems.updateInventory("updated", "morning", "USA", 52);
        Inventory updatedInv = jsonb.fromJson(
            systems.getInventory(52).toString(), Inventory.class);
        assertEquals(inv.getName(), updatedInv.getName());
        assertEquals(inv.getTime(), updatedInv.getTime());
        assertEquals(inv.getLocation(), updatedInv.getLocation());
        System.out.println("Inventory updated attempted!");
    }

    @Test
    public void deleteInventory() {
        System.out.println("TEST: Testing deleting an inventory");

        Jsonb jsonb = JsonbBuilder.create();

        Inventory inv =  jsonb.fromJson(
            systems.getInventory(53).toString(), Inventory.class);
        System.out.println("Testing deleting " + inv.getName());
        systems.deleteInventory(53);
        inv =  jsonb.fromJson(systems.getInventory(53).toString(), Inventory.class);
        assertNull(inv.getName());
        System.out.println("Inventory delete attempted!");
    }

}
