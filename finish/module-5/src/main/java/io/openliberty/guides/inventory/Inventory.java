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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.openliberty.guides.inventory.model.SystemData;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Inventory {

    private List<SystemData> systems = Collections.synchronizedList(new ArrayList<>());

    public List<SystemData> getSystems() {
        return systems;
    }

    public SystemData getSystem(String hostname) {
        for (SystemData s : systems) {
            if (s.getHostname().equalsIgnoreCase(hostname)) {
                return s;
            }
        }
        return null;
    }

    public boolean contains(String hostname) {
        return getSystem(hostname) != null;
    }

    public void add(String hostname, String osName, String javaVersion, Long heapSize) {
        systems.add(new SystemData(hostname, osName, javaVersion, heapSize));
    }

    public void update(String hostname, String osName, String javaVersion, Long heapSize) {
        for (SystemData s : systems) {
            if (s.getHostname().equalsIgnoreCase(hostname)) {
                s.setOsName(osName);
                s.setJavaVersion(javaVersion);
                s.setHeapSize(heapSize);
            }
        }
    }

    public boolean removeSystem(String hostname) {
        return systems.remove(getSystem(hostname));
    }

}
