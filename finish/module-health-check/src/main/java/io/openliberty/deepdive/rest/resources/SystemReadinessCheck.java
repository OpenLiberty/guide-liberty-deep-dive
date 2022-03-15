// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
// tag::SystemReadinessCheck[]
package io.openliberty.deepdive.rest.resources;
import jakarta.enterprise.context.ApplicationScoped;

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
public class SystemReadinessCheck implements HealthCheck {

  private String host = "localhost";
  private int port = 5432;

  @Override
  public HealthCheckResponse call() {
    HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Readiness Health Check");
    
    try {
      connectToServer(host, port);
      responseBuilder.up();
    } catch (Exception e) {
      responseBuilder.down();
    }
    return responseBuilder.build();
  }

  private void connectToServer(String dbhost, int port) throws IOException {
    Socket socket = new Socket(dbhost, port);
    socket.close();
  }
}