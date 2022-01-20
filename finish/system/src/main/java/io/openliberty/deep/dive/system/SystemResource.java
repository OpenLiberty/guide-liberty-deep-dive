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
package io.openliberty.deep.dive.system;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Properties;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/")
public class SystemResource {

    private static final MemoryMXBean MEM_BEAN = ManagementFactory.getMemoryMXBean();

    @GET
    @Path("/property/{property}")
    @RolesAllowed({ "admin", "user" })
    public String getProperty(@PathParam("property") String property) {
        return System.getProperty(property);
    }

    @GET
    @Path("/heapsize")
    @RolesAllowed({ "admin" })
    public Long getHeapSize() {
        return MEM_BEAN.getHeapMemoryUsage().getMax();
    }

}
