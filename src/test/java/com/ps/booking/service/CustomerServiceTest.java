package com.ps.booking.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.ps.booking.dto.LoginDto;
import com.ps.booking.dto.RegisterDto;
import com.ps.booking.entity.Customer;
import com.ps.booking.exception.ResourceNotFoundException;
import com.ps.booking.repository.CustomerRepository;
import com.ps.booking.security.JwtService;

@SpringBootTest
@Description("Tests for AccountService")
@ActiveProfiles("test")
public class CustomerServiceTest {

    @Mock
    private CustomerRepository accountRepository;

    @InjectMocks
    private CustomerService accountService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    private Customer account;

    @BeforeEach
    public void setup() {
        account = new Customer();
        account.setId(1L);
        account.setEmail("john.doe@example.com");
        account.setName("John");
        account.setMobileNumber("1234567890");
        account.setPassword("12345678");
    }

    @Test
    @Description("Should return account when get account by id")
    public void shouldReturnAccountWhenGetAccountById() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        Customer result = accountService.getCustomer(1L);
        assertEquals(account, result);
    }

    @Test
    @Description("Should return null when get account by id not found")
    public void shouldReturnNullWhenGetAccountByIdNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> accountService.getCustomer(1L));
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    @Description("Should return account when get account by email")
    public void shouldReturnAccountWhenGetAccountByEmail() {
        when(accountRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(account));

        Customer result = accountService.getCustomerByEmail("john.doe@example.com");

        assertEquals(account, result);
    }

    @Test
    @Description("Should return null when get account by email not found")
    public void shouldReturnNullWhenGetAccountByEmailNotFound() {
        when(accountRepository.findByEmail("john.doe@example.com")).thenThrow(NoSuchElementException.class);
        Exception exception = assertThrows(NoSuchElementException.class,
                () -> accountService.getCustomerByEmail("john.doe@example.com"));
        assertNull(exception.getMessage());
    }

    @Test
    @Description("Should create account when all input are valid")
    public void shouldCreateAccountWhenAllInputAreValid() {
        Mockito.when(passwordEncoder.encode("12345678")).thenReturn("mockPassword");
        when(accountRepository.save(any(Customer.class))).thenReturn(account);
        RegisterDto registerDto = new RegisterDto("john", "john.doe@example.com", "12345678", "1234567890");
        Customer result = accountService.createCustomer(registerDto);
        assertInstanceOf(Customer.class, result);
        assertEquals(account, result);
    }

    @Test
    @Description("Should throw exception when email already exists")
    public void shouldThrowExceptionWhenEmailAlreadyExists() {
        Mockito.when(accountRepository.existsByEmail("john.doe@example.com")).thenReturn(true);
        Mockito.when(passwordEncoder.encode("12345678")).thenReturn("mockPassword");
        when(accountRepository.save(any(Customer.class))).thenReturn(account);
        RegisterDto registerDto = new RegisterDto("john", "john.doe@example.com", "12345678", "1234567890");
        Exception exception = assertThrows(RuntimeException.class,
                () -> accountService.createCustomer(registerDto));
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