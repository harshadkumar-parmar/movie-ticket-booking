package com.pismo.transaction.service;

import com.pismo.transaction.entity.Account;
import com.pismo.transaction.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pismo.transaction.dto.LoginDto;
import com.pismo.transaction.dto.RegisterDto;
import com.pismo.transaction.exception.ResourceNotFoundException;
import com.pismo.transaction.security.JwtService;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    private Account account;

    @BeforeEach
    public void setup() {
        account = new Account();
        account.setAccountId(1L);
        account.setEmail("john.doe@example.com");
        account.setName("John");
        account.setDocumentNumber("1234567890");
        account.setPassword("12345678");
    }

    @Test
    @Description("Should return account when get account by id")
    public void shouldReturnAccountWhenGetAccountById() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        Account result = accountService.getAccount(1L);
        assertEquals(account, result);
    }

    @Test
    @Description("Should return null when get account by id not found")
    public void shouldReturnNullWhenGetAccountByIdNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> accountService.getAccount(1L));
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    @Description("Should return account when get account by email")
    public void shouldReturnAccountWhenGetAccountByEmail() {
        when(accountRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(account));

        Account result = accountService.geAccountByEmail("john.doe@example.com");

        assertEquals(account, result);
    }

    @Test
    @Description("Should return null when get account by email not found")
    public void shouldReturnNullWhenGetAccountByEmailNotFound() {
        when(accountRepository.findByEmail("john.doe@example.com")).thenThrow(NoSuchElementException.class);
        Exception exception = assertThrows(NoSuchElementException.class,
                () -> accountService.geAccountByEmail("john.doe@example.com"));
        assertNull(exception.getMessage());
    }

    @Test
    @Description("Should create account when all input are valid")
    public void shouldCreateAccountWhenAllInputAreValid() {
        Mockito.when(passwordEncoder.encode("12345678")).thenReturn("mockPassword");
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        RegisterDto registerDto = new RegisterDto("john", "john.doe@example.com", "12345678", "1234567890");
        Account result = accountService.createAccount(registerDto);
        assertInstanceOf(Account.class, result);
        assertEquals(account, result);
    }

    @Test
    @Description("Should throw exception when email already exists")
    public void shouldThrowExceptionWhenEmailAlreadyExists() {
        Mockito.when(accountRepository.existsByEmail("john.doe@example.com")).thenReturn(true);
        Mockito.when(passwordEncoder.encode("12345678")).thenReturn("mockPassword");
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        RegisterDto registerDto = new RegisterDto("john", "john.doe@example.com", "12345678", "1234567890");
        Exception exception = assertThrows(RuntimeException.class,
                () -> accountService.createAccount(registerDto));
        assertEquals("Email is already taken!", exception.getMessage());
    }

    @Test
    @Description("Should login with credentials and return token")
    public void shouldLoginWithCredentialsAndReturnToken() {
        Mockito.when(passwordEncoder.encode("12345678")).thenReturn("mockPassword");
        when(accountRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(account));
        when(jwtService.generateToken(any())).thenReturn("mockToken");
        when(authenticationManager.authenticate(any())).thenReturn(null);
        LoginDto loginDto = new LoginDto("john.doe@example.com", "12345678");
        String result = accountService.login(loginDto);
        assertEquals("mockToken", result);
    }
}