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
package io.openliberty.deepdive.rest;

import java.util.List;

import io.openliberty.deepdive.rest.model.SystemData;
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

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Path("/systems")
public class SystemResource {

    @Inject
    Inventory inventory;

    // tag::injectConfig[]
    @Inject
    @ConfigProperty(name = "client.https.port")
    String CLIENT_PORT;
    // end::injectConfig[]

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponseSchema(value = SystemData.class,
        responseDescription = "A list of system data stored within the inventory.",
        responseCode = "200")
    @Operation(
        summary = "List contents.",
        description = "Returns the currently stored system data in the inventory.",
        operationId = "listContents")
    public List<SystemData> listContents() {
        return inventory.getSystems();
    }

    @GET
    @Path("/{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponseSchema(value = SystemData.class,
        responseDescription = "JVM system properties of a particular host.",
        responseCode = "200")
    @Operation(
        summary = "Get System",
        description = "Retrieves and returns the JVM system properties from the system "
        + "service running on the particular host.",
        operationId = "getSystem"
    )
    public SystemData getSystem(
        @Parameter(
            name = "hostname", in = ParameterIn.PATH,
            description = "The hostname of the system",
            required = true, example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        )
        @PathParam("hostname") String hostname) {
    	return inventory.getSystem(hostname);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value={
        @APIResponse(
            responseCode = "200",
            description = "Successfully added system to inventory"
        ),
        @APIResponse(
           responseCode = "400",
            description = "Unable to add system to inventory" 
        )
    })
    @Parameters(value={
        @Parameter(
            name = "hostname", in = ParameterIn.QUERY,
            description = "The hostname of the system",
            required = true, example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "osName", in = ParameterIn.QUERY,
            description = "The osName of the system",
            required = true, example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "javaVersion", in = ParameterIn.QUERY,
            description = "The javaVersion of the system",
            required = true, example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "heapSize", in = ParameterIn.QUERY,
            description = "The heapSize of the system",
            required = true, example = "foo",
            schema = @Schema(type = SchemaType.NUMBER)
        ),
    })
    @Operation(
        summary = "Add system",
        description = "This adds a system and its properties to the inventory list of systems.",
        operationId = "addSystem"
    )
    public Response addSystem(
        @FormParam("hostname") String hostname,
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
    @APIResponses(value={
        @APIResponse(
            responseCode = "200",
            description = "Successfully updated system"
        ),
        @APIResponse(
           responseCode = "400",
           description = "Unable to update system, as this system does not exist in the inventory list" 
        )
    })
    @Parameters(value={
        @Parameter(
            name = "hostname", in = ParameterIn.PATH,
            description = "The hostname of the system",
            required = true, example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "osName", in = ParameterIn.QUERY,
            description = "The osName of the system",
            required = true, example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "javaVersion", in = ParameterIn.QUERY,
            description = "The javaVersion of the system",
            required = true, example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "heapSize", in = ParameterIn.QUERY,
            description = "The heapSize of the system",
            required = true, example = "foo",
            schema = @Schema(type = SchemaType.NUMBER)
        ),
    })
    @Operation(
        summary = "Update system",
        description = "This updates a system and its properties on the inventory list of systems.",
        operationId = "updateSystem"
    )
    public Response updateSystem(
        @PathParam("hostname") String hostname,
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
    @APIResponses(value={
        @APIResponse(
            responseCode = "200",
            description = "Successfully deleted system from inventory"
        ),
        @APIResponse(
           responseCode = "400",
            description = "Unable to delete system from inventory, as this system does not exist" 
        )
    })
    @Parameter(
        name = "hostname", in = ParameterIn.PATH,
        description = "The hostname of the system",
        required = true, example = "foo",
        schema = @Schema(type = SchemaType.STRING)
    )
    @Operation(
        summary = "Remove system",
        description = "This removes a system and its properties from the inventory list of systems.",
        operationId = "removeSystem"
    )
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
    @APIResponses(value={
        @APIResponse(
            responseCode = "200",
            description = "Successfully added system client"
        ),
        @APIResponse(
           responseCode = "400",
            description = "Unable to add system client" 
        )
    })
    @Parameter(
        name = "hostname", in = ParameterIn.PATH,
        description = "The hostname of the system",
        required = true, example = "foo",
        schema = @Schema(type = SchemaType.STRING)
    )
    @Operation(
        summary = "Add system client",
        description = "This adds a system client.",
        operationId = "addSystemClient"
    )

    //tag::printClientPort[]
    public Response addSystemClient(@PathParam("hostname") String hostname) {
    	System.out.println(CLIENT_PORT);
        return success("Client Port:" + CLIENT_PORT);
    }
    //tag::printClientPort[]

    private Response success(String message) {
        return Response.ok("{ \"ok\" : \"" + message + "\" }").build();
    }

    private Response fail(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity("{ \"error\" : \"" + message + "\" }")
                       .build();
    }
}
