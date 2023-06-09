package com.accountingsystem.controllers;


import com.accountingsystem.configs.jwt.JwtUtils;
import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.controller.dtos.mappers.UserMapper;
import com.accountingsystem.controller.UserController;
import com.accountingsystem.excel.ExcelReportWriter;
import com.accountingsystem.filters.*;
import com.accountingsystem.repository.UserLogRepository;
import com.accountingsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserLogRepository userLogRepository;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ExcelReportWriter excelReportWriter;

    @Test
    @WithMockUser(username = "test-login")
    void shouldReturnPageContractsAndModifySearchRequest_whenCalled() throws Exception {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addUserLoginFilter("test-login");

        when(userService.getContracts(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/user/contracts/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(new ArrayList<>())));

        verify(userService, times(1)).getContracts(searchRequest);

    }

    @Test
    @WithMockUser("test-login")
    void shouldReturnPageCounterpartyOrganizations_whenCalled() throws Exception {

        CounterpartyOrganizationDto co1 = new CounterpartyOrganizationDto();
        co1.setInn("012345678901");
        co1.setName("CounterpartyOrganization");
        co1.setAddress("address");
        co1.setId(1);

        CounterpartyOrganizationDto co2 = new CounterpartyOrganizationDto();
        co2.setInn("012345678902");
        co2.setName("CounterpartyOrganization1");
        co2.setAddress("address1");
        co2.setId(2);

        when(userService.getCounterpartyOrganizations(any())).thenReturn(new PageImpl<>(
                Stream.of(co1, co2).collect(Collectors.toList())
        ));

        JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/user/counterparty-organizations/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content")
                        .value(jsonParser.parse(objectMapper.writeValueAsString(Stream.of(co1, co2).collect(Collectors.toList())))));

        verify(userService, times(1)).getCounterpartyOrganizations(new SearchRequest());
    }

    @Test
    @WithMockUser("test-login")
    void shouldReturnPageCounterpartyContract_whenCalled() throws Exception {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addUserLoginFilter("test-login");
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, 2);

        when(userService.getCounterpartyContracts(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/user/contracts/2/counterparty-contracts/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(new ArrayList<>())));

        verify(userService, times(1)).getCounterpartyContracts(searchRequest);
    }

    @Test
    @WithMockUser("test-login")
    void shouldReturnPageContractStages_whenCalled() throws Exception {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addUserLoginFilter("test-login");
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, 3);

        when(userService.getContractStages(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/user/contracts/3/contract-stages/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(new ArrayList<>())));

        verify(userService, times(1)).getContractStages(searchRequest);
    }

    @Test
    @WithMockUser("test-login")
    void shouldCreateContractReport_whenCalled() throws Exception {

        when(userService.getContractsBetweenDates(any(), any(), any())).thenReturn(new HashSet<>());
        when(userService.getCounterpartyContractsBetweenDates(any(), any(), any())).thenReturn(new HashSet<>());
        when(excelReportWriter.createContractReport(anySet(), anySet())).thenReturn(new ByteArrayOutputStream(3));

        mvc.perform(
                        MockMvcRequestBuilders.get("/api/user/downland-contract-report/dates")
                                .param("plannedStartDate", "13.07.2002")
                                .param("plannedEndDate", "18.08.2005")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filename=contract-report.xlsx"))
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet")));

        ArgumentCaptor<String> captorString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<LocalDate> captorLocalDate = ArgumentCaptor.forClass(LocalDate.class);

        verify(userService, times(1)).getContractsBetweenDates(captorString.capture(), captorLocalDate.capture(), captorLocalDate.capture());
        verify(userService, times(1)).getCounterpartyContractsBetweenDates(captorString.capture(), captorLocalDate.capture(), captorLocalDate.capture());


        assertThat(captorString.getAllValues()).hasSize(2).containsOnly("test-login");
        assertThat(captorLocalDate.getAllValues()).containsExactly(
                LocalDate.of(2002, 7, 13), LocalDate.of(2005, 8, 18),
                LocalDate.of(2002, 7, 13), LocalDate.of(2005, 8, 18)
        );
    }

    @Test
    @WithMockUser("test-login")
    void shouldCreateContractStageReport_whenCalled() throws Exception {

        when(userService.getContractStagesContractForContractId(any(  ), any())).thenReturn(new HashSet<>());
        when(excelReportWriter.createContractStagesReport(anySet())).thenReturn(new ByteArrayOutputStream(3));

        mvc.perform(
                        MockMvcRequestBuilders.get("/api/user/downland-contract-stage-report/2")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filename=contract-stage-report.xlsx"))
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet")));

        verify(userService, times(1)).getContractStagesContractForContractId("test-login", 2);
    }
}
