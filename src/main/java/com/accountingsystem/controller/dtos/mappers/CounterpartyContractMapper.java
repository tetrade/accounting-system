package com.accountingsystem.controller.dtos.mappers;


import com.accountingsystem.advice.exceptions.NoSuchRowException;
import com.accountingsystem.controller.dtos.CounterpartyContractDto;
import com.accountingsystem.entitys.CounterpartyContract;


import org.mapstruct.InjectionStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.Set;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD,
        uses = {CounterpartyOrganizationMapper.class, ContractStageMapper.class},
        imports = NoSuchRowException.class
)
public interface CounterpartyContractMapper {

    @Named(value = "mapToCounterpartyContractDto")
    public CounterpartyContractDto mapToCounterpartyContractDto(CounterpartyContract counterpartyContract);

    @Mapping(target = "counterpartyOrganization", source = "counterpartyOrganizationId")
    CounterpartyContract mapToCounterpartyContract(CounterpartyContractDto counterpartyContractDto);

    @IterableMapping(qualifiedByName = "mapToCounterpartyContractDto")
    Set<CounterpartyContractDto> mapToCounterpartyContractDtoSet(Collection<CounterpartyContract> counterpartyContracts);
}
