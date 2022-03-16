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
package io.openliberty.deepdive.rest.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
//tag:: SystemDataSchema[]
@Schema(name="SystemData", description="POJO that represents a single inventory entry.")
public class SystemData {
//end:: SystemDataSchema[]

    private int id;

    //tag:: hostnameSchema[]
    @Schema(required = true)
    private String hostname;
    //end:: hostnameSchema[]
    
    private String osName;
    private String javaVersion;
    private Long   heapSize;

    public SystemData() {
    }

    public SystemData(String hostname, String osName, String javaVersion, Long heapSize) {
        this.hostname = hostname;
        this.osName = osName;
        this.javaVersion = javaVersion;
        this.heapSize = heapSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public Long getHeapSize() {
        return heapSize;
    }

    public void setHeapSize(Long heapSize) {
        this.heapSize = heapSize;
    }

    @Override
    public boolean equals(Object host) {
      if (host instanceof SystemData) {
        return hostname.equals(((SystemData) host).getHostname());
      }
      return false;
    }
}
