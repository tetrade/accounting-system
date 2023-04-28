package com.accountingsystem.controller.user;


import com.accountingsystem.controller.dtos.*;
import com.accountingsystem.controller.dtos.mappers.UserMapper;
import com.accountingsystem.excel.ExcelReportWriter;
import com.accountingsystem.filters.*;
import com.accountingsystem.service.UserDetailsImpl;
import com.accountingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("user-api/")
@RestController
public class UserGettingController {

    @Autowired
    private ExcelReportWriter excelReportWriter;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    // Методы для получения информации пользователя
    @GetMapping("counterparty-organizations/")
    public ResponseEntity<Page<CounterpartyOrganizationDto>> getCounterpartyOrganizations(@RequestBody SearchRequest searchRequest) {
        Page<CounterpartyOrganizationDto> counterpartyOrganizationDtos = userService.getCounterpartyOrganizations(searchRequest);
        return ResponseEntity.ok().body(counterpartyOrganizationDtos);
    }

    @GetMapping("contracts/")
    public ResponseEntity<Page<ContractDto>> getContracts(
            @RequestBody SearchRequest searchRequest,
            Authentication authentication
    ) {

        searchRequest.addUserLoginFilter(authentication.getName());
        Page<ContractDto> contractDtos = userService.getContracts(searchRequest);
        return ResponseEntity.ok().body(contractDtos);
    }

    @GetMapping("contracts/{contractId}/counterparty-contracts/")
    public ResponseEntity<Page<CounterpartyContractDto>> getCounterpartyContractsByContractId(
            @PathVariable Integer contractId,
            @RequestBody SearchRequest searchRequest,
            Authentication authentication
    ) {
        searchRequest.addUserLoginFilter(authentication.getName());
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<CounterpartyContractDto> counterpartyContractDtos = userService.getCounterpartyContracts(searchRequest);
        return ResponseEntity.ok().body(counterpartyContractDtos);
    }

    @GetMapping("contracts/{contractId}/contract-stages/")
    public ResponseEntity<Page<ContractStageDto>> getContractStagesByContractId(
            @PathVariable Integer contractId,
            @RequestBody SearchRequest searchRequest,
            Authentication authentication
    ) {
        searchRequest.addUserLoginFilter(authentication.getName());
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<ContractStageDto> contractStageDtos = userService.getContractStages(searchRequest);
        return ResponseEntity.ok().body(contractStageDtos);
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserDto> getUserInfo(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userMapper.mapToUserDto(userDetails));
    }
}
