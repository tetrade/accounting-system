package com.accountingsystem.dtos.mappers;


import com.accountingsystem.dtos.CounterpartyContractDto;
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
    CounterpartyContractDto mapToCounterpartyContractDto(CounterpartyContract counterpartyContract);
    CounterpartyContract mapToCounterpartyContract(CounterpartyContractDto counterpartyContractDto);

    @IterableMapping(qualifiedByName = "mapToCounterpartyContractDto")
    Set<CounterpartyContractDto> mapToCounterpartyContractDtoSet(Collection<CounterpartyContract> counterpartyContracts);

    void mapToTargetCounterpartyContract(@MappingTarget CounterpartyContract counterpartyContract, CounterpartyContractDto counterpartyContractDto);

    @Mapping(target = "type", expression = "java(counterpartyContract.getType().getType())")
    @Mapping(source = "contract", target = "contractDtoExcel")
    CounterpartyContractDtoExcel mapToContractDtoExcel(CounterpartyContract counterpartyContract);

    Set<CounterpartyContractDtoExcel> mapToCounterpartyContractsDtoExcelSet(Collection<CounterpartyContract> counterpartyContracts);
}
