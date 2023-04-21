package com.accountingsystem.controller.dtos.mappers;

import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.controller.dtos.ContractUserDto;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.entitys.Contract;
import org.apache.poi.ss.usermodel.DateUtil;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Mapper(
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        componentModel = "spring",
        uses = {ContractStageMapper.class, CounterpartyContractMapper.class, UserMapper.class},
        imports = DateUtil.class
)
public interface ContractMapper {
    @Named(value = "mapToContractDto")
    ContractDto mapToContractDto(Contract contract);

    Contract mapToContract(ContractDto contractDto);

    @IterableMapping(qualifiedByName = "mapToContractDto")
    Set<ContractDto> mapToContractDtoSet(Collection<Contract> contractList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapToTargetContract(@MappingTarget Contract contract, ContractDto contractDto);

    @Mapping(target = "type", expression = "java(contract.getType().getType())")
    ContractDtoExcel mapToContractDtoExcel(Contract contract);

    default Double dateToExcelDate(LocalDate localDate) {
        if (localDate == null) return null;
        return DateUtil.getExcelDate(localDate);
    }

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
}
