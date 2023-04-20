package com.accountingsystem.controller.dtos.mappers;


import com.accountingsystem.controller.dtos.CounterpartyContractDto;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.entitys.CounterpartyContract;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Set;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {CounterpartyOrganizationMapper.class, ContractMapper.class}
)
public interface CounterpartyContractMapper {

    @Named(value = "mapToCounterpartyContractDto")

    @Mapping(source = "counterpartyOrganization", target = "counterpartyOrganizationDto")
    CounterpartyContractDto mapToCounterpartyContractDto(CounterpartyContract counterpartyContract);

    CounterpartyContract mapToCounterpartyContract(CounterpartyContractDto counterpartyContractDto);

    @IterableMapping(qualifiedByName = "mapToCounterpartyContractDto")
    Set<CounterpartyContractDto> mapToCounterpartyContractDtoSet(Collection<CounterpartyContract> counterpartyContracts);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "counterpartyOrganization", ignore = true)
    @Mapping(target = "id", ignore = true)
    void mapToTargetCounterpartyContract(@MappingTarget CounterpartyContract counterpartyContract, CounterpartyContractDto counterpartyContractDto);

    @Mapping(target = "type", expression = "java(counterpartyContract.getType().getType())")
    @Mapping(source = "contract", target = "contractDtoExcel")
    CounterpartyContractDtoExcel mapToContractDtoExcel(CounterpartyContract counterpartyContract);

    Set<CounterpartyContractDtoExcel> mapToCounterpartyContractsDtoExcelSet(Collection<CounterpartyContract> counterpartyContracts);
}
