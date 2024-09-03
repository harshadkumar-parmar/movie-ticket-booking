package com.pismo.transaction.service;


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

import com.pismo.transaction.dto.LoginDto;
import com.pismo.transaction.dto.RegisterDto;
import com.pismo.transaction.entity.Account;
import com.pismo.transaction.exception.ResourceNotFoundException;
import com.pismo.transaction.repository.AccountRepository;
import com.pismo.transaction.security.JwtService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Creates a new account
     * 
     * @param account the account to create
     * @return the created account
     */
    public Account createAccount(@RequestBody RegisterDto signUpDto) {
        log.trace("Creating user {}", signUpDto.getEmail());
        // add check for username exists in a DB
        if (accountRepository.existsByEmail(signUpDto.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        // create user object
        Account user = new Account();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setDocumentNumber(signUpDto.getDocumentNumber());
        return accountRepository.save(user);
    }

    /**
     * Retrieves an account by id
     * 
     * @param accountId the id of the account
     * @return the account with the given id
     * @throws ResourceNotFoundException if the account is not found
     */
    public Account getAccount(@PathVariable Long accountId) throws ResourceNotFoundException  {
        log.trace("Fetching account by id: {}", accountId);
        return accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
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
     * Retrieves an account by email
     * 
     * @param email the email of the account
     * @return the account with the given email
     * @throws NoSuchElementException if the account is not found
     */
    public Account geAccountByEmail(String email) {
        log.trace("Fetching account by email: {}", email);
        return accountRepository.findByEmail(email).orElseThrow();
    }
}
