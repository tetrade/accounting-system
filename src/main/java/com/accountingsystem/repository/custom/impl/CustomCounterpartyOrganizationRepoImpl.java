package com.accountingsystem.repository.custom.impl;

import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.repository.custom.CustomCounterpartyOrganizationRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class CustomCounterpartyOrganizationRepoImpl implements CustomCounterpartyOrganizationRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void updateCounterpartyOrganization(int counterpartyOrganizationId, CounterpartyOrganizationDto c) {
        entityManager.createNativeQuery("UPDATE counterparty_organization SET name = ?, address = ?, inn = ?" +
                        " WHERE counterparty_organization.id = ?")
                .setParameter(1, c.getName())
                .setParameter(2, c.getAddress())
                .setParameter(3, c.getInn())
                .setParameter(4, counterpartyOrganizationId)
                .executeUpdate();
    }

    @Transactional
    public void insertCounterpartyOrganization(CounterpartyOrganizationDto c) {
        entityManager.createNativeQuery("INSERT INTO counterparty_organization (name, address, inn) VALUES (?,?,?)")
                .setParameter(1, c.getName())
                .setParameter(2, c.getAddress())
                .setParameter(3, c.getInn())
                .executeUpdate();
    }

}
