package com.accountingsystem.dtos.mappers;

import com.accountingsystem.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.entitys.CounterpartyOrganization;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CounterpartyOrganizationMapper {
    CounterpartyOrganizationDto map(CounterpartyOrganization counterpartyOrganization);
    CounterpartyOrganization map(CounterpartyOrganizationDto counterpartyOrganizationDto);

    void mapToTargetCounterpartyOrganization(
            @MappingTarget CounterpartyOrganization counterpartyOrganization,
            CounterpartyOrganizationDto counterpartyOrganizationDto
            );

    Set<CounterpartyOrganizationDto> map(Collection<CounterpartyOrganization> counterpartyOrganizations);
}
