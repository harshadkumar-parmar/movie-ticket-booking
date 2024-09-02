package com.pismo.transaction.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.pismo.transaction.dto.LoginDto;
import com.pismo.transaction.dto.RegisterDto;
import com.pismo.transaction.entity.Account;
import com.pismo.transaction.exception.ResourceNotFoundException;
import com.pismo.transaction.service.AccountService;


@SpringBootTest
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private Account account;
    private UsernamePasswordAuthenticationToken authorization;
    @BeforeEach
    public void setup() {
        account = new Account();
        account.setAccountId(1L);
        account.setDocumentNumber("123");
        account.setEmail("a@b.com");
        account.setName("Joe");
        
        authorization = new UsernamePasswordAuthenticationToken("john.doe@example.com", "password");
    }

    @Test
    @Description("Should return account when get account by id")
    public void testGetAccountById() throws Exception {
        when(accountService.getAccount(account.getAccountId())).thenReturn(account);
        ResponseEntity<Account> response = accountController.getAccount(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    @Description("Should not get account when id not found")
    public void testShouldNotGetAccountByIdWhenIdNotFound() throws Exception {
    when(accountService.getAccount(10L)).thenThrow(ResourceNotFoundException.class);
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> accountController.getAccount(10L));
        assertEquals(null, ex.getMessage());
    }

    @Test
    @Description("Should return current account")
    public void testShouldGetCurrentAccount() throws Exception {
        when(accountService.geAccountByEmail("john.doe@example.com")).thenReturn(account);
        ResponseEntity<Account> response = accountController.getCurrentAccount(authorization);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    @Description("Should throw exception when email not registerd")
    public void testShouldThrowExceptionWhenUserNotFoundWithEmail() throws Exception {
        when(accountService.geAccountByEmail("notvalid")).thenThrow(RuntimeException.class);
        
        UsernamePasswordAuthenticationToken notValid = new UsernamePasswordAuthenticationToken("notvalid", "password");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountController.getCurrentAccount(notValid), "User not found");
        assertEquals(null, exception.getMessage());
    }

    @Test
    @Description("Should create account")
    public void testShouldCreateAccount() throws Exception {
        RegisterDto registerDto = new RegisterDto("john", "john.doe@example.com", "12345678", "1234567890");
        when(accountService.createAccount(registerDto)).thenReturn(account);
        ResponseEntity<String> response = accountController.register(registerDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Account created successfully.", response.getBody());
    }

    @Test
    @Description("Should throw exception when email already exists")
    public void testShouldThrowExceptionWhenEmailAlreadyExists() throws Exception {
        when(accountService.createAccount(
            new RegisterDto("john", "john.doe@example.com", "12345678", "1234567890"))
            ).thenThrow(new RuntimeException("Email is already taken!"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountController.register(new RegisterDto(
            "john", "john.doe@example.com", "12345678", "1234567890")),
            "Email is already taken!");
        assertEquals("Email is already taken!", exception.getMessage());
    }

    @Test
    public void testShouldLogin() throws Exception {
        LoginDto loginDto = new LoginDto("john.doe@example.com", "password");
        when(accountService.login(loginDto)).thenReturn("token");
        ResponseEntity<String> response = accountController.login(loginDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody());
    }

    @Test
    public void testShouldNotLoginWithBadCredentials() throws Exception {
        LoginDto loginDto = new LoginDto("john.doe2@example.com", "password");
        when(accountService.login(loginDto)).thenThrow(new RuntimeException("Bad credentials"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountController.login(loginDto), "Bad credentials");
        assertEquals("Bad credentials", exception.getMessage());
    }
}