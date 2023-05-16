package com.accountingsystem.controllers;

import com.accountingsystem.configs.jwt.JwtUtils;
import com.accountingsystem.controller.AdminController;
import com.accountingsystem.controller.dtos.*;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.filters.*;
import com.accountingsystem.repository.UserLogRepository;
import com.accountingsystem.service.AdminService;
import com.accountingsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.JSONParser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserLogRepository userLogRepository;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnCreated_whenCalledCreateCounterpartyOrganization() throws Exception {
        CounterpartyOrganizationDto counterpartyOrganizationDto = new CounterpartyOrganizationDto();
        counterpartyOrganizationDto.setName("test");
        counterpartyOrganizationDto.setAddress("qe13");
        counterpartyOrganizationDto.setInn("012345678912");

        mvc.perform(
                MockMvcRequestBuilders.post("/api/admin/counterparty-organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(counterpartyOrganizationDto))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(adminService, times(1)).createCounterpartyOrganization(counterpartyOrganizationDto);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnNoContent_whenCalledUpdateCounterpartyOrganization() throws Exception {
        CounterpartyOrganizationDto counterpartyOrganizationDto = new CounterpartyOrganizationDto();
        counterpartyOrganizationDto.setName("test");
        counterpartyOrganizationDto.setAddress("qe13");
        counterpartyOrganizationDto.setInn("012345678912");

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/admin/counterparty-organizations/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(counterpartyOrganizationDto))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(adminService, times(1)).updateCounterpartyOrganization(2, counterpartyOrganizationDto);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnNoContent_whenCalledDeleteCounterpartyOrganization() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/admin/counterparty-organizations/3")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(adminService, times(1)).deleteCounterpartyOrganization(3);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnPage_whenCalledGetCounterpartyContractsByContractId() throws Exception {

        when(userService.getCounterpartyContracts(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addFilter(new FilterRequest(EPublicKey.NAME, ETargetEntity.COUNTERPARTY_ORGANIZATION, EOperator.EQUAL, "something"));

        mvc.perform(
                MockMvcRequestBuilders.post("/api/admin/contracts/2/counterparty-contracts/search")
                           .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(new ArrayList<>())));

        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, 2);

        verify(userService).getCounterpartyContracts(searchRequest);
    }


    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnCreated_whenCreateCounterpartyContract() throws Exception {
        CounterpartyContractDto ccD = new CounterpartyContractDto();
        ccD.setName("test1");
        ccD.setType(EType.WORKS);
        ccD.setAmount(BigDecimal.valueOf(123));
        ccD.setPlannedStartDate(LocalDate.of(2002, 7, 13));
        ccD.setPlannedEndDate(LocalDate.of(2003, 5, 5));
        ccD.setCounterpartyOrganizationId(1);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/admin/contracts/4/counterparty-contracts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0,\"name\":\"test1\",\"type\":\"Работы\",\"amount\":123," +
                                "\"plannedStartDate\":\"13.07.2002\",\"plannedEndDate\":\"05.05.2003\"," +
                                "\"actualStartDate\":null,\"actualEndDate\":null,\"counterpartyOrganizationId\":1}")
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        verify(adminService).createCounterpartyContract(4, ccD);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnNoContent_whenUpdateCounterpartyContract() throws Exception {
        CounterpartyContractDto ccD = new CounterpartyContractDto();
        ccD.setName("test1");
        ccD.setType(EType.DELIVERY);
        ccD.setAmount(BigDecimal.valueOf(1190));
        ccD.setPlannedStartDate(LocalDate.of(2002, 7, 13));
        ccD.setPlannedEndDate(LocalDate.of(2003, 5, 5));
        ccD.setCounterpartyOrganizationId(5);

        mvc.perform(
                MockMvcRequestBuilders.put("/api/admin/counterparty-contracts/10")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0,\"name\":\"test1\",\"type\":\"Поставка\",\"amount\":1190," +
                                "\"plannedStartDate\":\"13.07.2002\",\"plannedEndDate\":\"05.05.2003\"," +
                                "\"actualStartDate\":null,\"actualEndDate\":null,\"counterpartyOrganizationId\":5}")
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(adminService, times(1)).updateCounterpartyContract(10, ccD);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnNoContent_whenCalledDeleteCounterpartyContract() throws Exception {

        mvc.perform(
                MockMvcRequestBuilders.delete("/api/admin/counterparty-contracts/10")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(adminService, times(1)).deleteCounterpartyContract(10);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnPage_whenCalledGetContractStagesByContractId() throws Exception {

        when(userService.getContractStages(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addFilter(new FilterRequest(EPublicKey.NAME, ETargetEntity.CONTRACT_STAGE, EOperator.EQUAL, "something"));

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/admin/contracts/2/contract-stages/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(new ArrayList<>())));

        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, 2);

        verify(userService).getContractStages(searchRequest);
    }


    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnCreated_whenCreateContractStages() throws Exception {
        ContractStageDto csD = new ContractStageDto();
        csD.setName("test1");
        csD.setPlannedMaterialCosts(BigDecimal.valueOf(90));
        csD.setPlannedSalaryExpenses(BigDecimal.valueOf(9012.32));
        csD.setAmount(BigDecimal.valueOf(123));
        csD.setPlannedStartDate(LocalDate.of(2002, 7, 13));
        csD.setPlannedEndDate(LocalDate.of(2003, 5, 5));


        mvc.perform(
                MockMvcRequestBuilders.post("/api/admin/contracts/6/contract-stages")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(csD))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        verify(adminService).createContractStage(6, csD);
    }


    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnNoContent_whenUpdateContractStages() throws Exception {
        ContractStageDto csD = new ContractStageDto();
        csD.setName("test1");
        csD.setPlannedMaterialCosts(BigDecimal.valueOf(90));
        csD.setPlannedSalaryExpenses(BigDecimal.valueOf(9012.32));
        csD.setAmount(BigDecimal.valueOf(123));
        csD.setPlannedStartDate(LocalDate.of(2002, 7, 13));
        csD.setPlannedEndDate(LocalDate.of(2003, 5, 5));

        mvc.perform(
                MockMvcRequestBuilders.put("/api/admin/contract-stages/10")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(csD))
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(adminService, times(1)).updateContractStage(10, csD);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnNoContent_whenCalledDeleteContractStages() throws Exception {

        mvc.perform(
                MockMvcRequestBuilders.delete("/api/admin/contract-stages/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(adminService, times(1)).deleteContractStage(5);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnPage_whenCalledGetContractsWithUsers() throws Exception {
        ContractDto contractDto = new ContractDto();
        contractDto.setName("qwe");
        contractDto.setType(EType.WORKS);
        contractDto.setAmount(BigDecimal.valueOf(19));
        contractDto.setPlannedEndDate(LocalDate.of(2002, 5, 5));
        contractDto.setPlannedStartDate(LocalDate.of(666, 6, 6));

        UserDto userDto = new UserDto();
        userDto.setIsAdmin(true);
        userDto.setLogin("1qqww");
        userDto.setFullName("Да Да Дадович");
        userDto.setTerminationDate(LocalDate.of(2024, 10, 10));

        ContractUserDto cuD = new ContractUserDto();
        cuD.setContract(contractDto);
        cuD.setUser(userDto);

        when(adminService.getContractWithUsers(any())).thenReturn(new PageImpl<>(Stream.of(cuD).collect(Collectors.toList())));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addFilter(new FilterRequest(EPublicKey.LOGIN, ETargetEntity.USER, EOperator.LIKE, "smt"));

        JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/admin/contracts/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content")
                        .value(jsonParser.parse(objectMapper.writeValueAsString(Stream.of(cuD).collect(Collectors.toList())))));;

        verify(adminService).getContractWithUsers(searchRequest);
    }


    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnCreated_whenCreateContractForUser() throws Exception {
        ContractDto contractDto = new ContractDto();
        contractDto.setName("qwe");
        contractDto.setType(EType.WORKS);
        contractDto.setAmount(BigDecimal.valueOf(19));
        contractDto.setPlannedEndDate(LocalDate.of(2002, 5, 5));
        contractDto.setPlannedStartDate(LocalDate.of(666, 6, 6));

        mvc.perform(
                MockMvcRequestBuilders.post("/api/admin/contracts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractDto))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        verify(adminService).createContract(contractDto);
    }


    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnNoContent_whenUpdateContract() throws Exception {
        ContractDto contractDto = new ContractDto();
        contractDto.setName("qweee");
        contractDto.setType(EType.DELIVERY);
        contractDto.setAmount(BigDecimal.valueOf(190));
        contractDto.setPlannedEndDate(LocalDate.of(2013, 10, 10));
        contractDto.setPlannedStartDate(LocalDate.of(777, 7, 7));
        contractDto.setActualEndDate(LocalDate.now().plusYears(2));
        contractDto.setActualEndDate(LocalDate.now());

        mvc.perform(
                MockMvcRequestBuilders.put("/api/admin/contracts/10")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractDto))
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(adminService, times(1)).updateContract(10, contractDto);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnNoContent_whenCalledDeleteContract() throws Exception {

        mvc.perform(
                MockMvcRequestBuilders.delete("/api/admin/contracts/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(adminService, times(1)).deleteContract(5);
    }


    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}, username = "test-admin")
    void shouldReturnUsers_whenCalledGetUsers() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/api/admin/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SearchRequest()))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        verify(adminService, times(1)).getAllUsers(new SearchRequest());
    }
}
