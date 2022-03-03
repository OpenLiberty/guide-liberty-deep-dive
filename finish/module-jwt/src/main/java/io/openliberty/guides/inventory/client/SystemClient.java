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
package io.openliberty.guides.inventory.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/api")
public interface SystemClient extends AutoCloseable {

    @GET
    @Path("/property/{property}")
    public String getProperty(@HeaderParam("Authorization") String authHeader, @PathParam("property") String property);

    @GET
    @Path("/heapsize")
    public Long getHeapSize(@HeaderParam("Authorization") String authHeader);

}
