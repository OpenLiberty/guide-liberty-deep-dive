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
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import io.openliberty.deepdive.rest.model.SystemData;

@ApplicationScoped
@Path("/systems")
public class SystemResource {

    @Inject
    Inventory inventory;

    // tag::listContents[]
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    // tag::listContentsAPIResponseScheme[]
    @APIResponseSchema(value = Inventory.class,
        responseDescription = "host:properties pairs stored in the inventory.",
        responseCode = "200")
    // end::listContentsAPIResponseScheme[]
    // tag::listContentsOperation[]
    @Operation(
        summary = "List contents.",
        description = "Returns the currently stored host:properties pairs in the inventory.",
        operationId = "listContents")
    // end::listContentsOperation[]
    public List<SystemData> listContents() {
        return inventory.getSystems();
    }
    // end::listContents[]

    // tag::getSystem[]
    @GET
    @Path("/{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    // tag::getSystemAPIResponseSchema[]
    @APIResponseSchema(value = SystemData.class,
        responseDescription = "JVM system properties of a particular host.",
        responseCode = "200")
    // end::getSystemAPIResponseSchema[]
    // tag::getSystemParameter[]
    @Parameter(
        name = "hostname",
        in = ParameterIn.QUERY,
        description = "The hostname of the system",
        required = true,
        example = "foo",
        schema = @Schema(type = SchemaType.STRING))
    // end::getSystemParameter[]
    // tag::getSystemOperation[]
    @Operation(
        summary = "Get System",
        description = "Retrieves and returns the JVM system properties from the system "
        + "service running on the particular host.",
        operationId = "getSystem"
    )
    // end::getSystemOperation[]
    public SystemData getSystem(
        @PathParam("hostname") String hostname) {
    	return inventory.getSystem(hostname);
    }
    // end::getSystem[]

    // tag::addSystem[]
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    // tag::addSystemAPIResponses[]
    @APIResponses(value={
        // tag::addSystemAPIResponse[]
        @APIResponse(
            responseCode = "200",
            description = "Successfully added system to inventory"
        ),
        @APIResponse(
           responseCode = "400",
            description = "Unable to add system to inventory" 
        )
        // end::addSystemAPIResponse[]
    })
    // end::addSystemAPIResponses[]
    // tag::addSystemParameters[]
    @Parameters(value={
        // tag::addSystemParameter[]
        @Parameter(
            name = "hostname",
            in = ParameterIn.QUERY,
            description = "The hostname of the system",
            required = true,
            example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "osName",
            in = ParameterIn.QUERY,
            description = "The osName of the system",
            required = true,
            example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "javaVersion",
            in = ParameterIn.QUERY,
            description = "The javaVersion of the system",
            required = true,
            example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "heapSize",
            in = ParameterIn.QUERY,
            description = "The heapSize of the system",
            required = true,
            example = "foo",
            schema = @Schema(type = SchemaType.LONG)
        ),
        // end::addSystemParameter[]
    })
    // end::addSystemParameters[]
    // tag::addSystemOperation[]
    @Operation(
        summary = "Add system",
        description = "This adds a system and its properties to the inventory list of systems.",
        operationId = "addSystem"
    )
    // end::addSystemOperation[]
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
    // end::addSystem[]

    // tag::updateSystem[]
    @PUT
    @Path("/{hostname}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    // tag::updateSystemAPIResponses[]
    @APIResponses(value={
        // tag:: updateSystemAPIResponse[]
        @APIResponse(
            responseCode = "200",
            description = "Successfully updated system"
        ),
        @APIResponse(
           responseCode = "400",
            description = "Unable to update system, as this system does not exist in the inventory list" 
        )
        // end:: updateSystemAPIResponse[]
    })
    // end::updateSystemAPIResponses[]
    // tag::updateSystemParameters[]
    @Parameters(value={
        // tag::updateSystemParameter[]
        @Parameter(
            name = "hostname",
            in = ParameterIn.QUERY,
            description = "The hostname of the system",
            required = true,
            example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "osName",
            in = ParameterIn.QUERY,
            description = "The osName of the system",
            required = true,
            example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "javaVersion",
            in = ParameterIn.QUERY,
            description = "The javaVersion of the system",
            required = true,
            example = "foo",
            schema = @Schema(type = SchemaType.STRING)
        ),
        @Parameter(
            name = "heapSize",
            in = ParameterIn.QUERY,
            description = "The heapSize of the system",
            required = true,
            example = "foo",
            schema = @Schema(type = SchemaType.LONG)
        ),
        // end::updateSystemParameter[]
    })
    // end::updateSystemParameters[]
    // tag::updateSystemOperation[]
    @Operation(
        summary = "Update system",
        description = "This updates a system and its properties on the inventory list of systems.",
        operationId = "updateSystem"
    )
    // end::updateSystemOperation[]
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
    // end::updateSystem[]

    // tag::removeSystem[]
    @DELETE
    @Path("/{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    // tag::removeSystemAPIResponses[]
    @APIResponses(value={
        // tag::removeSystemAPIResponse[]
        @APIResponse(
            responseCode = "200",
            description = "Successfully deleted system from inventory"
        ),
        @APIResponse(
           responseCode = "400",
            description = "Unable to delete system from inventory, as this system does not exist" 
        )
        // tag::removeSystemAPIResponse[]
    })
    // end::removeSystemAPIResponses[]
    // tag::removeSystemParameter[]
    @Parameter(
        name = "hostname",
        in = ParameterIn.QUERY,
        description = "The hostname of the system",
        required = true,
        example = "foo",
        schema = @Schema(type = SchemaType.STRING)
    )
    // end::removeSystemParameter[]
    // tag::removeSystemOperation[]
    @Operation(
        summary = "Remove system",
        description = "This removes a system and its properties from the inventory list of systems.",
        operationId = "removeSystem"
    )
    // end::removeSystemOperation[]
    public Response removeSystem(@PathParam("hostname") String hostname) {
        if (inventory.removeSystem(hostname)) {
            return success(hostname + " was removed.");
        } else {
            return fail(hostname + " does not exists.");
        }
    }
    // end::removeSystem[]
    
    // tag::addSystemClient[]
    @POST
    @Path("/client/{hostname}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    // tag::addSystemClientAPIResponses[]
    @APIResponses(value={
        // tag::addSystemClientAPIResponse[]
        @APIResponse(
            responseCode = "200",
            description = "Successfully added system client"
        ),
        @APIResponse(
           responseCode = "400",
            description = "Unable to add system client" 
        )
        // tag::addSystemClientAPIResponse[]
    })
    // end::addSystemClientAPIResponses[]
    // tag::addSystemClientParameter[]
    @Parameter(
        name = "hostname",
        in = ParameterIn.QUERY,
        description = "The hostname of the system",
        required = true,
        example = "foo",
        schema = @Schema(type = SchemaType.STRING)
    )
    // end::addSystemClientParameter[]
    // tag::addSystemClientOperation[]
    @Operation(
        summary = "Add system client",
        description = "This adds a system client.",
        operationId = "addSystemClient"
    )
    // end::addSystemClientOperation[]
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
    // end::addSystemClient[]
}
