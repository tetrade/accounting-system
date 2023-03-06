package com.accountingsystem.dtos.mappers;

import com.accountingsystem.dtos.ContractStageDto;
import com.accountingsystem.entitys.ContractStage;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ContractStageMapper {
    ContractStageDto map(ContractStage contractStage);

    ContractStage map(ContractStageDto contractStageDto);

    ContractStage map(
            @MappingTarget ContractStage contractStage,
            ContractStageDto contractStageDto
    );

    Set<ContractStageDto> map(List<ContractStage> contractStage);
    Set<ContractStageDto> map(Set<ContractStage> contractStage);
}
