// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.deepdive.rest.dao;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import io.openliberty.deepdive.rest.model.Inventory;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
// tag::InventoryDao[]
public class InventoryDao {

    // tag::PersistenceContext[]
    @PersistenceContext(name = "jpa-unit")
    // end::PersistenceContext[]
    private EntityManager em;

    // tag::createInventory[]
    public void createInventory(Inventory inventory) {
        // tag::Persist[]
        em.persist(inventory);
        // end::Persist[]
    }
    // end::createInventory[]

    // tag::readInventory[]
    public Inventory readInventory(int inventoryId) {
        // tag::Find[]
        return em.find(Inventory.class, inventoryId);
        // end::Find[]
    }
    // end::readInventory[]

    // tag::updateInventory[]
    public void updateInventory(Inventory inventory) {
        // tag::Merge[]
        em.merge(inventory);
        // end::Merge[]
    }
    // end::updateInventory[]

    // tag::deleteInventory[]
    public void deleteInventory(Inventory inventory) {
        // tag::Remove[]
        em.remove(inventory);
        // end::Remove[]
    }
    // end::deleteInventory[]

    // tag::readAllInventories[]
    public List<Inventory> readAllInventories() {
        return em.createNamedQuery("Inventory.findAll", Inventory.class).getResultList();
    }
    // end::readAllInventories[]

    // tag::findInventory[]
    public List<Inventory> findInventory(String name, String location, String time) {
        return em.createNamedQuery("Inventory.findInventory", Inventory.class)
            .setParameter("name", name)
            .setParameter("location", location)
            .setParameter("time", time).getResultList();
    }
    // end::findInventory[]
}
// end::InventoryDao[]
