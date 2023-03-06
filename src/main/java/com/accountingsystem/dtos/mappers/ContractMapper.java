package com.accountingsystem.dtos.mappers;

import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.entitys.Contract;
import com.accountingsystem.enums.EType;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        componentModel = "spring",
        uses = {ContractStageMapper.class, CounterpartyContractMapper.class}
)
public interface ContractMapper {
    ContractDto map(Contract contract);
    Contract map(ContractDto contractDto);
    Contract map(@MappingTarget Contract contract, ContractDto contractDto);
    Set<ContractDto> map(List<Contract> contractList);
    Set<ContractDto> map(Set<Contract> contractSet);
}
