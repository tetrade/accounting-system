package com.accountingsystem.dtos.mappers;

import com.accountingsystem.dtos.ContractStageDto;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.entitys.ContractStage;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ContractMapper.class}
)
public interface ContractStageMapper {

    @Named(value = "mapToContractStageDto")
    ContractStageDto mapToContractStageDto(ContractStage contractStage);
    @Mapping(target = "id", ignore = true)
    ContractStage mapToContractStage(ContractStageDto contractStageDto);

    @IterableMapping(qualifiedByName = "mapToContractStageDto")
    Set<ContractStageDto> mapToContractStageDtoSet(Collection<ContractStage> contractStage);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapToTargetContractStage(@MappingTarget ContractStage contractStage, ContractStageDto contractStageDto);

    ContractStageDtoExcel mapToContractStageDtoExcel(ContractStage contractStage);

    Set<ContractStageDtoExcel> mapToContractStageDtoExcelSet(Collection<ContractStage> contractStage);
}
