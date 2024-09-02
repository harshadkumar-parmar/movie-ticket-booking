package com.pismo.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.pismo.demo.entity.Account;
import com.pismo.demo.service.AccountService;

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
    public void testGetAccountById() throws Exception {
        when(accountService.getAccount(account.getAccountId())).thenReturn(account);

        ResponseEntity<Account> response = accountController.getAccount(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    public void testGetAccountByIdNotFound() throws Exception {
        when(accountService.getAccount(10L)).thenThrow(NotFoundException.class);
        ResponseEntity<Account> response = accountController.getAccount(10L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetCurrentAccount() throws Exception {
        when(accountService.geAccountByEmail("john.doe@example.com")).thenReturn(account);
        ResponseEntity<Account> response = accountController.getCurrentAccount(authorization);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    public void testGetCurrentAccountNotFound() throws Exception {
        when(accountService.geAccountByEmail("john.doe@example.com")).thenThrow(RuntimeException.class);
        ResponseEntity<Account> response = accountController.getCurrentAccount(
            new UsernamePasswordAuthenticationToken("b", "password")
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}