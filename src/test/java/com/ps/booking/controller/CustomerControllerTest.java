package com.ps.booking.controller;

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
import org.springframework.test.context.ActiveProfiles;

import com.ps.booking.dto.LoginDto;
import com.ps.booking.dto.RegisterDto;
import com.ps.booking.entity.Customer;
import com.ps.booking.exception.ResourceNotFoundException;
import com.ps.booking.service.CustomerService;


@SpringBootTest
@Description("Tests for CustomerController")
@ActiveProfiles("test")
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;
    private UsernamePasswordAuthenticationToken authorization;
    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setId(1L);
        customer.setMobileNumber("1234567890");
        customer.setEmail("a@b.com");
        customer.setName("Joe");
        
        authorization = new UsernamePasswordAuthenticationToken("john.doe@example.com", "password");
    }

    @Test
    @Description("Should return customer when get customer by id")
    public void testGetCustomerById() throws Exception {
        when(customerService.getCustomer(customer.getId())).thenReturn(customer);
        ResponseEntity<Customer> response = customerController.getCustomer(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    @Description("Should not get customer when id not found")
    public void testShouldNotGetCustomerByIdWhenIdNotFound() throws Exception {
    when(customerService.getCustomer(10L)).thenThrow(ResourceNotFoundException.class);
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> customerController.getCustomer(10L));
        assertEquals(null, ex.getMessage());
    }

    @Test
    @Description("Should return current customer")
    public void testShouldGetCurrentCustomer() throws Exception {
        when(customerService.getCustomerByEmail("john.doe@example.com")).thenReturn(customer);
        ResponseEntity<Customer> response = customerController.getCurrentCustomer(authorization);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    @Description("Should throw exception when email not registerd")
    public void testShouldThrowExceptionWhenUserNotFoundWithEmail() throws Exception {
        when(customerService.getCustomerByEmail("notvalid")).thenThrow(RuntimeException.class);
        
        UsernamePasswordAuthenticationToken notValid = new UsernamePasswordAuthenticationToken("notvalid", "password");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerController.getCurrentCustomer(notValid), "User not found");
        assertEquals(null, exception.getMessage());
    }

    @Test
    @Description("Should create customer")
    public void testShouldCreateCustomer() throws Exception {
        RegisterDto registerDto = new RegisterDto("john", "john.doe@example.com", "12345678", "1234567890");
        when(customerService.createCustomer(registerDto)).thenReturn(customer);
        ResponseEntity<String> response = customerController.register(registerDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Customer created successfully.", response.getBody());
    }

    @Test
    @Description("Should throw exception when email already exists")
    public void testShouldThrowExceptionWhenEmailAlreadyExists() throws Exception {
        when(customerService.createCustomer(
            new RegisterDto("john", "john.doe@example.com", "12345678", "1234567890"))
            ).thenThrow(new RuntimeException("Email is already taken!"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerController.register(new RegisterDto(
            "john", "john.doe@example.com", "12345678", "1234567890")),
            "Email is already taken!");
        assertEquals("Email is already taken!", exception.getMessage());
    }

    @Test
    public void testShouldLogin() throws Exception {
        LoginDto loginDto = new LoginDto("john.doe@example.com", "password");
        when(customerService.login(loginDto)).thenReturn("token");
        ResponseEntity<String> response = customerController.login(loginDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody());
    }

    @Test
    public void testShouldNotLoginWithBadCredentials() throws Exception {
        LoginDto loginDto = new LoginDto("john.doe2@example.com", "password");
        when(customerService.login(loginDto)).thenThrow(new RuntimeException("Bad credentials"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerController.login(loginDto), "Bad credentials");
        assertEquals("Bad credentials", exception.getMessage());
    }
}