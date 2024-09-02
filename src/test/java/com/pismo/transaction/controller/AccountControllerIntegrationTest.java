package com.pismo.transaction.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pismo.transaction.dto.LoginDto;
import com.pismo.transaction.dto.RegisterDto;
import com.pismo.transaction.security.JwtService;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Account Controller Integration Test")
public class AccountControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @DisplayName("Should register user when input is valid")
    public void testShouldRegisterUserWhenInputIsValid() throws Exception {

        RegisterDto registerDto = new RegisterDto("joe", "user@example.com", "password", "1234567890");

        mockMvc.perform(post("/api/accounts/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should not register user when email is not valid")
    public void testShouldNotRegisterUserWhenEmailNotValid() throws Exception {

        RegisterDto registerDto = new RegisterDto("joe", "invalidmail", "password", "1234567890");

        mockMvc.perform(post("/api/accounts/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should not register user when password is not valid")
    public void testShouldNotRegisterUserWhenPasswordIsNotValid() throws Exception {

        RegisterDto registerDto = new RegisterDto("joe", "user@example.com", "", "1234567890");

        mockMvc.perform(post("/api/accounts/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql("/sql/accounts.sql")
    @DisplayName("Should login user when input is valid")
    public void testLoginIntegration() throws Exception {
        LoginDto loginDto = new LoginDto("a@b.com", "string");
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not login user when email is not valid")
    public void testShouldNotLoginUserWhenEmailNotValid() throws Exception {
        LoginDto loginDto = new LoginDto("user", "password");

        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get account with token")
    @Sql("/sql/accounts.sql")
    public void testUserShouldGetAccountWithToken() throws Exception {
        User user = new User("a@b.com","string",List.of(new SimpleGrantedAuthority("USER")));
        String accessToken = jwtService.generateToken(user);
        mockMvc.perform(post("/api/accounts/me")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not get account without token")
    public void testUserShouldNotGetAccountWithoutToken() throws Exception {

        String accessToken = jwtService.generateToken(
                new User(
                        "not@registered.com",
                        "",
                        List.of(new SimpleGrantedAuthority("USER"))));

        mockMvc.perform(post("/api/accounts/me")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
