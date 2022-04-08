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
// tag::ReadinessCheck[]
package io.openliberty.deepdive.rest.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import java.io.IOException;
import java.net.Socket;

// tag::Readiness[]
@Readiness
// end::Readiness[]
// tag::ApplicationScoped[]
@ApplicationScoped
// end::ApplicationScoped[]
public class ReadinessCheck implements HealthCheck {

    @Inject
    @ConfigProperty(name = "postgres/hostname")
    private String host;

    @Inject
    @ConfigProperty(name = "postgres/portnum")
    private int port;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder =
            HealthCheckResponse.named("Readiness Check");

        try {
            Socket socket = new Socket(host, port);
            socket.close();
            responseBuilder.up();
        } catch (Exception e) {
            responseBuilder.down();
        }
        return responseBuilder.build();
    }
}
// end::ReadinessCheck[]
