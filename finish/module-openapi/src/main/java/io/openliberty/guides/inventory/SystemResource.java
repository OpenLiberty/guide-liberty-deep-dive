// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.inventory;

import java.util.List;

import io.openliberty.guides.inventory.model.SystemData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/systems")
public class SystemResource {

    @Inject
    Inventory inventory;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponseSchema(value = InventoryList.class,
        responseDescription = "host:properties pairs stored in the inventory.",
        responseCode = "200")
    @Operation(
        summary = "List inventory contents.",
        description = "Returns the currently stored host:properties pairs in the "
        + "inventory.")
    public List<SystemData> listContents() {
        return inventory.getSystems();
    }

    @GET
    @Path("/{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(
        responseCode = "404",
        description = "Missing description",
        content = @Content(mediaType = "application/json"))
    @APIResponseSchema(value = Properties.class,
        responseDescription = "JVM system properties of a particular host.",
        responseCode = "200")
    @Operation(
        summary = "Get JVM system properties for particular host",
        description = "Retrieves and returns the JVM system properties from the system "
        + "service running on the particular host.")
    public SystemData getSystem(@PathParam("hostname") String hostname) {
    	return inventory.getSystem(hostname);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSystem(@FormParam("hostname") String hostname,
        @FormParam("osName") String osName,
        @FormParam("javaVersion") String javaVersion,
        @FormParam("heapSize") Long heapSize) {

        if (inventory.contains(hostname)) {
            return fail(hostname + " already exists.");
        }
        inventory.add(hostname, osName, javaVersion, heapSize);
        return success(hostname + " was added.");
    }

    @PUT
    @Path("/{hostname}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSystem(@PathParam("hostname") String hostname,
        @FormParam("osName") String osName,
        @FormParam("javaVersion") String javaVersion,
        @FormParam("heapSize") Long heapSize) {

        if (!inventory.contains(hostname)) {
            return fail(hostname + " does not exists.");
        }
        inventory.update(hostname, osName, javaVersion, heapSize);
        return success(hostname + " was updated.");
    }

    @DELETE
    @Path("/{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSystem(@PathParam("hostname") String hostname) {
        if (inventory.removeSystem(hostname)) {
            return success(hostname + " was removed.");
        } else {
            return fail(hostname + " does not exists.");
        }
    }
    
    @POST
    @Path("/client/{hostname}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSystemClient(@PathParam("hostname") String hostname) {
    	return fail("This api is not implemented yet.");
    }

    private Response success(String message) {
        return Response.ok("{ \"ok\" : \"" + message + "\" }").build();
    }

    private Response fail(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity("{ \"error\" : \"" + message + "\" }")
                       .build();
    }
}
