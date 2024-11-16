package com.ps.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.booking.dto.LoginDto;
import com.ps.booking.dto.RegisterDto;
import com.ps.booking.entity.Customer;
import com.ps.booking.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/customer")
@Tag(name = "Customer", description = "Customer Management API")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Customer Login", description = "Login a user with a password, and email", responses = {
            @ApiResponse(responseCode = "201", description = "User Login successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto customer) {
        log.trace("Login request received for email: {}", customer.getEmail());
        String token = customerService.login(customer);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    @Operation(summary = "Customer creation", description = "Register a new user with a password, and email", responses = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        log.trace("Register request received for email: {}", registerDto.getEmail());
        customerService.createCustomer(registerDto);
        return new ResponseEntity<>("Customer created successfully.", HttpStatus.CREATED);
    }

    @Operation(summary = "Get customer details", description = "Get customer details based on customer id", responses = {
            @ApiResponse(responseCode = "201", description = "User Login successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId) throws Exception {
        log.trace("Get customer request received for customer id: {}", customerId);
        Customer customer = customerService.getCustomer(customerId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @Operation(summary = "Get customer details", description = "Get customer details based on customer id", responses = {
            @ApiResponse(responseCode = "201", description = "User Login successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/me")
    public ResponseEntity<Customer> getCurrentCustomer(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication) {

        log.trace("Get customer request received for email: {}", authentication.getName());
        Customer customer = customerService.getCustomerByEmail(authentication.getName());

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
