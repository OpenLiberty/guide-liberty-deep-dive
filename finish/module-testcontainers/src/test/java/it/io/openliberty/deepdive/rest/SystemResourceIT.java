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
package it.io.openliberty.deepdive.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class SystemResourceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemResourceIT.class);
    private static final String APP_PATH = "/inventory/api";
    private static final String POSTGRES_HOST = "postgres";
    private static final String POSTGRES_IMAGE_NAME = "postgres-sample:latest";
    private static final String APP_IMAGE_NAME = "liberty-deepdive-inventory:1.0-SNAPSHOT";

    public static SystemResourceClient client;
    public static Network network = Network.newNetwork();
    private static String authHeaderAdmin;
    
    // tag::PostgresSetup[]
    @Container
    public static GenericContainer<?> postgresContainer
        = new GenericContainer<>(POSTGRES_IMAGE_NAME)
              .withNetwork(network)
              .withExposedPorts(5432)
              .withNetworkAliases(POSTGRES_HOST)
              .withLogConsumer(new Slf4jLogConsumer(LOGGER));
    // end::PostgresSetup[]

    // tag::LibertySetup[]
    @Container
    public static LibertyContainer libertyContainer
        = new LibertyContainer(APP_IMAGE_NAME)
              .withEnv("POSTGRES_HOSTNAME", POSTGRES_HOST)
              .withExposedPorts(9080)
              .withNetwork(network)
              .waitingFor(Wait.forHttp("/health/ready"))
              .withLogConsumer(new Slf4jLogConsumer(LOGGER));
    // end::LibertySetup[]

    @BeforeAll
    public static void setupTestClass() throws Exception {
        System.out.println("INFO: Starting Liberty Container setup");
        client = libertyContainer.createRestClient(
            SystemResourceClient.class, APP_PATH);
        String usernameAndPassword = "bob" + ":" + "bobpwd";
        authHeaderAdmin = "Basic "
                + java.util.Base64.getEncoder()
                .encodeToString(usernameAndPassword.getBytes());
    }

    private void showSystemData(SystemData system) {
        System.out.println("TEST: SystemData > " + 
        	    system.getId() + ", " +
        	    system.getHostname() + ", " +
        	    system.getOsName() + ", " +
        	    system.getJavaVersion() + ", " +
        	    system.getHeapSize());
    }
    
    @Test
    @Order(1)
    public void testAddSystem() {
        System.out.println("TEST: Testing add a system");
    	client.addSystem("localhost", "linux", "8", Long.valueOf(1024));
    	List<SystemData> systems = client.listContents();
    	assertEquals(1, systems.size());
    	showSystemData(systems.get(0));
    	assertEquals("8", systems.get(0).getJavaVersion());
    	assertEquals(Long.valueOf(1024), systems.get(0).getHeapSize());
    }
    
    @Test
    @Order(2)
    public void testUpdateSystem() {
        System.out.println("TEST: Testing update a system");
    	client.updateSystem(authHeaderAdmin, "localhost", "linux", "11", Long.valueOf(2048));
    	List<SystemData> systems = client.listContents();
    	showSystemData(systems.get(0));
    	assertEquals("11", systems.get(0).getJavaVersion());
    	assertEquals(Long.valueOf(2048), systems.get(0).getHeapSize());
    }
    
    @Test
    @Order(3)
    public void testRemoveSystem() {
        System.out.println("TEST: Testing remove a system");
    	client.removeSystem(authHeaderAdmin, "localhost");
    	List<SystemData> systems = client.listContents();
    	assertEquals(0, systems.size());
    }
}
