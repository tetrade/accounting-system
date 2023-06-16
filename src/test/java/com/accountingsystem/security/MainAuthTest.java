package com.accountingsystem.security;

import com.accountingsystem.controller.dtos.LoginRequest;
import com.accountingsystem.controller.dtos.SignUpRequest;
import com.accountingsystem.entitys.User;
import com.accountingsystem.entitys.enums.ERole;
import com.accountingsystem.filters.SearchRequest;
import com.accountingsystem.repository.RoleRepo;
import com.accountingsystem.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@SpringBootTest
@AutoConfigureMockMvc
class MainAuthTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "\"user\"", "user_role");
    }

    @Test
    void shouldUnauthorizedStatus_whenTryingGetAccessToAdminMethodsWithoutLogin() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/admin/contracts/2/counterparty-contracts/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new SearchRequest()))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldUnauthorizedStatus_whenTryingGetAccessToAdminMethodsWithUserRole() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/admin/contracts/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new SearchRequest()))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "ROLE"})
    void shouldOkStatus_whenTryingGetAccessToAdminMethodsWithAdmin() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/admin/contracts/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new SearchRequest()))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void shouldOkStatus_whenTryingGetAccessToUserMethodsWithUserRole() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/user/contracts/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new SearchRequest()))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldUnauthorizedStatus_whenTryingGetAccessToUserMethodsWithoutAuth() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/user/counterparty-organization/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new SearchRequest()))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void shouldRegisterUser_whenSignUp() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLogin("test-login");
        signUpRequest.setFullName("Пупс Пупс Пупсович");
        signUpRequest.setPassword("qe123g!fw*");

        mvc.perform(
                MockMvcRequestBuilders.post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        User underTest = userRepo.findByLogin(signUpRequest.getLogin()).orElse(null);

        assertThat(underTest).extracting("login", "fullName", "terminationDate")
                .containsExactly(signUpRequest.getLogin(), signUpRequest.getFullName(), LocalDate.now().plusMonths(1));

        assertThat(passwordEncoder.matches(signUpRequest.getPassword(), underTest.getPassword())).isTrue();

        assertThat(underTest.getRoles()).extracting("name").containsOnly(ERole.ROLE_USER);
    }

    @Test
    void shouldLoginUser_whenUserLoginWithRightCredits() throws Exception {
        String password = "qwe123!23";

        User user = new User();
        user.setLogin("test-login-user");
        user.setFullName("Пятлин Иван Крутович");
        user.setTerminationDate(LocalDate.of(2030, 7, 13));
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Stream.of(roleRepo.findByName(ERole.ROLE_USER).orElse(null)).collect(Collectors.toSet()));
        user = userRepo.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(user.getLogin());
        loginRequest.setPassword(password);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/auth/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists("jwt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is(user.getLogin())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", Matchers.is(user.getFullName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.terminationDate", Matchers.is("13.07.2030")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAdmin", Matchers.is(false)));
    }
}
