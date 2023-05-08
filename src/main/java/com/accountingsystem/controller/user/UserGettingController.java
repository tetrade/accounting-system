package com.accountingsystem.controller.user;


import com.accountingsystem.controller.dtos.*;
import com.accountingsystem.controller.dtos.mappers.UserMapper;
import com.accountingsystem.filters.*;
import com.accountingsystem.service.UserDetailsImpl;
import com.accountingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RequestMapping("api/user/")
@RestController
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class UserGettingController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    // Методы для получения информации пользователя
    @PostMapping("counterparty-organizations/search")
    public ResponseEntity<Page<CounterpartyOrganizationDto>> getCounterpartyOrganizations(@RequestBody SearchRequest searchRequest) {
        Page<CounterpartyOrganizationDto> counterpartyOrganizationDtos = userService.getCounterpartyOrganizations(searchRequest);
        return ResponseEntity.ok().body(counterpartyOrganizationDtos);
    }

    @PostMapping("contracts/search")
    public ResponseEntity<Page<ContractDto>> getContracts(
            @RequestBody SearchRequest searchRequest,
            Authentication authentication
    ) {

        searchRequest.addUserLoginFilter(authentication.getName());
        Page<ContractDto> contractDtos = userService.getContracts(searchRequest);
        return ResponseEntity.ok().body(contractDtos);
    }

    @PostMapping("contracts/{contractId}/counterparty-contracts/search")
    public ResponseEntity<Page<CounterpartyContractDto>> getCounterpartyContractsByContractId(
            @PathVariable @Positive Integer contractId,
            @RequestBody SearchRequest searchRequest,
            Authentication authentication
    ) {
        searchRequest.addUserLoginFilter(authentication.getName());
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<CounterpartyContractDto> counterpartyContractDtos = userService.getCounterpartyContracts(searchRequest);
        return ResponseEntity.ok().body(counterpartyContractDtos);
    }

    @PostMapping("contracts/{contractId}/contract-stages/search")
    public ResponseEntity<Page<ContractStageDto>> getContractStagesByContractId(
            @PathVariable @Positive Integer contractId,
            @RequestBody SearchRequest searchRequest,
            Authentication authentication
    ) {
        searchRequest.addUserLoginFilter(authentication.getName());
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<ContractStageDto> contractStageDtos = userService.getContractStages(searchRequest);
        return ResponseEntity.ok().body(contractStageDtos);
    }

    @PostMapping("/user-info")
    public ResponseEntity<UserDto> getUserInfo(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userMapper.mapToUserDto(userDetails));
    }
}
