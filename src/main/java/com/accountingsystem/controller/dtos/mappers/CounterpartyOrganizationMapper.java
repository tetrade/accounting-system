package com.accountingsystem.controller.dtos.mappers;

import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.entitys.CounterpartyOrganization;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Set;


@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CounterpartyOrganizationMapper {
    CounterpartyOrganizationDto map(CounterpartyOrganization counterpartyOrganization);
    @Mapping(ignore = true, target = "id")
    CounterpartyOrganization map(CounterpartyOrganizationDto counterpartyOrganizationDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void mapToTargetCounterpartyOrganization(
            @MappingTarget CounterpartyOrganization counterpartyOrganization,
            CounterpartyOrganizationDto counterpartyOrganizationDto
            );

    Set<CounterpartyOrganizationDto> map(Collection<CounterpartyOrganization> counterpartyOrganizations);
}
