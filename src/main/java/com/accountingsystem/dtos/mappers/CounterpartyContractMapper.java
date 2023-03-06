package com.accountingsystem.dtos.mappers;


import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.enums.EType;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {CounterpartyOrganizationMapper.class}
)
public interface CounterpartyContractMapper {

    CounterpartyContractDto map(CounterpartyContract counterpartyContract);

    CounterpartyContract map(CounterpartyContractDto counterpartyContractDto);

    CounterpartyContract map(
            @MappingTarget CounterpartyContract counterpartyContract,
            CounterpartyContractDto counterpartyContractDto
    );

    Set<CounterpartyContractDto> map(List<CounterpartyContract> counterpartyContracts);
    Set<CounterpartyContractDto> map(Set<CounterpartyContract> counterpartyContracts);
}
