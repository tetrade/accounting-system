package com.accountingsystem.controller.dtos.mappers;

import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.controller.dtos.ContractUserDto;
import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import org.apache.poi.ss.usermodel.DateUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;


import java.util.Collection;
import java.util.Set;

@Mapper(
        injectionStrategy = InjectionStrategy.FIELD,
        componentModel = "spring",
        uses = {ContractStageMapper.class, CounterpartyContractMapper.class, UserMapper.class},
        imports = DateUtil.class
)
public interface ContractMapper {
    @Named(value = "mapToContractDto")
    ContractDto mapToContractDto(Contract contract);

    @Mapping(source = "userId", target = "user")
    Contract mapToContract(ContractDto contractDto);

    @AfterMapping
    default void mapToContract(@MappingTarget Contract contract, ContractDto contractDto){
        if (contract.getCounterpartyContracts() != null) {
            contract.getCounterpartyContracts().forEach(cc -> cc.setContract(contract));
        }
        if (contract.getContractStages() != null) {
            contract.getContractStages().forEach( cs -> cs.setContract(contract));
        }
    }

    @IterableMapping(qualifiedByName = "mapToContractDto")
    Set<ContractDto> mapToContractDtoSet(Collection<Contract> contractList);

    @Mapping(target = "type", expression = "java(contract.getType().getType())")
    ContractDtoExcel mapToContractDtoExcel(Contract contract);

    Set<ContractDtoExcel> mapToContractDtoExcelSet(Collection<Contract> contracts);

    @Mapping(source = "id", target = "contract.id")
    @Mapping(source = "name", target = "contract.name")
    @Mapping(source = "type", target = "contract.type")
    @Mapping(source = "amount", target = "contract.amount")
    @Mapping(source = "plannedStartDate", target = "contract.plannedStartDate")
    @Mapping(source = "plannedEndDate", target = "contract.plannedEndDate")
    @Mapping(source = "actualStartDate", target = "contract.actualStartDate")
    @Mapping(source = "actualEndDate", target = "contract.actualEndDate")
    @Mapping(source = "user", target = "user")
    ContractUserDto mapToContractUser(Contract contract);

    Set<ContractUserDto> mapToContractUserDtos(Collection<Contract> contracts);

    @Mapping(target = "type", expression = "java(counterpartyContract.getType().getType())")
    @Mapping(source = "contract", target = "contractDtoExcel")
    CounterpartyContractDtoExcel mapToContractDtoExcel(CounterpartyContract counterpartyContract);

    Set<CounterpartyContractDtoExcel> mapToCounterpartyContractsDtoExcelSet(Collection<CounterpartyContract> counterpartyContracts);

}
