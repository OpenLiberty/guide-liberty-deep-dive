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
// tag::SystemLivenessCheck[]
package io.openliberty.deepdive.rest;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

// tag::Liveness[]
@Liveness
// end::Liveness[]
@ApplicationScoped
public class SystemLivenessCheck implements HealthCheck {

  @Override
  public HealthCheckResponse call() {
    MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
    long memUsed = memBean.getHeapMemoryUsage().getUsed();
    long memMax = memBean.getHeapMemoryUsage().getMax();

    return HealthCheckResponse.named(
      SystemResource.class.getSimpleName() + " Liveness Check")
                              .withData("memory used", memUsed)
                              .withData("memory max", memMax)
                              .status(memUsed < memMax * 0.9).build();
  }
}
// end::SystemLivenessCheck[]