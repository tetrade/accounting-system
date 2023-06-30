package com.accountingsystem.controller.dtos.mappers;

import com.accountingsystem.advice.exceptions.NoSuchRowException;
import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.entitys.CounterpartyOrganization;
import com.accountingsystem.repository.CounterpartyOrganizationRepo;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Set;


@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public abstract class CounterpartyOrganizationMapper {

    @Autowired
    CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    public abstract CounterpartyOrganizationDto map(CounterpartyOrganization counterpartyOrganization);

    public CounterpartyOrganization mapIdToCounterpartyOrganization(Integer counterpartyOrganizationId) {
        return counterpartyOrganizationRepo.findById(counterpartyOrganizationId)
                .orElseThrow(() -> new NoSuchRowException("id", counterpartyOrganizationId, "counterparty organization"));
    }

    abstract Set<CounterpartyOrganizationDto> map(Collection<CounterpartyOrganization> counterpartyOrganizations);
}
