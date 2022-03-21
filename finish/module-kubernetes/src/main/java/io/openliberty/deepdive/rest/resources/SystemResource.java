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
package io.openliberty.deepdive.rest.resources;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import io.openliberty.deepdive.rest.dao.InventoryDao;
import io.openliberty.deepdive.rest.model.Inventory;

// tag::RequestedScoped[]
@RequestScoped
// end::RequestedScoped[]
@Path("systems")
// tag::DAO[]
// tag::InventoryResource[]
public class SystemResource {

    @Inject
    private InventoryDao inventoryDAO;

    /**
     * This method creates a new inventory from the submitted data (name, time and
     * location) by the user.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    // tag::Transactional[]
    @Transactional
    // end::Transactional[]
    public Response addNewInventory(@FormParam("name") String name,
        @FormParam("time") String time, @FormParam("location") String location) {
        Inventory newInventory = new Inventory(name, location, time);
        if (!inventoryDAO.findInventory(name, location, time).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Inventory already exists").build();
        }
        inventoryDAO.createInventory(newInventory);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * This method updates a new inventory from the submitted data (name, time and
     * location) by the user.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response updateInventory(@FormParam("name") String name,
        @FormParam("time") String time, @FormParam("location") String location,
        @PathParam("id") int id) {
        Inventory prevInventory = inventoryDAO.readInventory(id);
        if (prevInventory == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Inventory does not exist").build();
        }
        if (!inventoryDAO.findInventory(name, location, time).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Inventory already exists").build();
        }
        prevInventory.setName(name);
        prevInventory.setLocation(location);
        prevInventory.setTime(time);

        inventoryDAO.updateInventory(prevInventory);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * This method deletes a specific existing/stored inventory
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteInventory(@PathParam("id") int id) {
        Inventory inventory = inventoryDAO.readInventory(id);
        if (inventory == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Inventory does not exist").build();
        }
        inventoryDAO.deleteInventory(inventory);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * This method returns a specific existing/stored inventory in Json format
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonObject getInventory(@PathParam("id") int inventoryId) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Inventory inventory = inventoryDAO.readInventory(inventoryId);
        if (inventory != null) {
            builder.add("name", inventory.getName()).add("time", inventory.getTime())
                .add("location", inventory.getLocation()).add("id", inventory.getId());
        }
        return builder.build();
    }

    /**
     * This method returns the existing/stored inventories in Json format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonArray getInventories() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder finalArray = Json.createArrayBuilder();
        for (Inventory inventory : inventoryDAO.readAllInventories()) {
            builder.add("name", inventory.getName()).add("time", inventory.getTime())
                   .add("location", inventory.getLocation()).add("id", inventory.getId());
            finalArray.add(builder.build());
        }
        return finalArray.build();
    }
}
// end::DAO[]
// end::InventoryResource[]
