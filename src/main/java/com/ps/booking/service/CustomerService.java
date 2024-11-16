package com.ps.booking.service;


import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ps.booking.dto.LoginDto;
import com.ps.booking.dto.RegisterDto;
import com.ps.booking.entity.Customer;
import com.ps.booking.exception.ResourceNotFoundException;
import com.ps.booking.repository.CustomerRepository;
import com.ps.booking.security.JwtService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Creates a new customer
     * 
     * @param customer the customer to create
     * @return the created customer
     */
    public Customer createCustomer(@RequestBody RegisterDto signUpDto) {
        log.trace("Creating user {}", signUpDto.getEmail());
        // add check for username exists in a DB
        if (customerRepository.existsByEmail(signUpDto.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        // create user object
        Customer user = new Customer();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setMobileNumber(signUpDto.getMobileNumber());
        return customerRepository.save(user);
    }

    /**
     * Retrieves an customer by id
     * 
     * @param customerId the id of the customer
     * @return the customer with the given id
     * @throws ResourceNotFoundException if the customer is not found
     */
    public Customer getCustomer(@PathVariable Long customerId) throws ResourceNotFoundException  {
        log.trace("Fetching customer by id: {}", customerId);
        return customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    /**
     * Logs in a user and returns a JWT token
     * 
     * @param loginDto the login credentials
     * @return the JWT token
     * @throws BadCredentialsException if the credentials are invalid
     */
    public String login(LoginDto loginDto) {
        log.trace("Logging in user {}", loginDto.getEmail());
        AbstractAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtService.generateToken(
                new User(loginDto.getEmail(), loginDto.getPassword(), List.of(new SimpleGrantedAuthority("USER"))));
    }

    /**
     * Retrieves an customer by email
     * 
     * @param email the email of the customer
     * @return the customer with the given email
     * @throws NoSuchElementException if the customer is not found
     */
    public Customer getCustomerByEmail(String email) {
        log.trace("Fetching customer by email: {}", email);
        return customerRepository.findByEmail(email).orElseThrow();
    }
}
