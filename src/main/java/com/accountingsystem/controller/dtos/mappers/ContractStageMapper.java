package com.accountingsystem.controller.dtos.mappers;

import com.accountingsystem.controller.dtos.ContractStageDto;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.entitys.ContractStage;
import org.apache.poi.ss.usermodel.DateUtil;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD
)
public interface ContractStageMapper {

    default Double dateToExcelDate(LocalDate localDate) {
        if (localDate == null) return null;
        return DateUtil.getExcelDate(localDate);
    }

    @Named(value = "mapToContractStageDto")
    ContractStageDto mapToContractStageDto(ContractStage contractStage);
    @Mapping(target = "id", ignore = true)
    ContractStage mapToContractStage(ContractStageDto contractStageDto);

    @IterableMapping(qualifiedByName = "mapToContractStageDto")
    Set<ContractStageDto> mapToContractStageDtoSet(Collection<ContractStage> contractStage);

    ContractStageDtoExcel mapToContractStageDtoExcel(ContractStage contractStage);

    Set<ContractStageDtoExcel> mapToContractStageDtoExcelSet(Collection<ContractStage> contractStage);
}
