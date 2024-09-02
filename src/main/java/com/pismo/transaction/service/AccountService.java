package com.pismo.transaction.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import com.pismo.transaction.repository.AccountRepository;
import com.pismo.transaction.security.JwtService;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    
    /**
     * Creates a new account
     * @param account the account to create
     * @return the created account
     */
    public Account createAccount(@RequestBody RegisterDto signUpDto) {
        
        // add check for username exists in a DB
        if(accountRepository.existsByEmail(signUpDto.getEmail())){
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
     * @param accountId the id of the account
     * @return the account with the given id
     * @throws NotFoundException if the account is not found
     */
    public Account getAccount(@PathVariable Long accountId) throws NotFoundException  {
        return accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException());
    }


    /**
     * Logs in a user and returns a JWT token
     * @param loginDto the login credentials
     * @return the JWT token
     * @throws BadCredentialsException if the credentials are invalid
     */
    public String login(LoginDto loginDto) {
        AbstractAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Set <GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        return jwtService.generateToken(new User(loginDto.getEmail(), loginDto.getPassword(), roles));
    }

    /**
     * Retrieves an account by email
     * @param email the email of the account
     * @return the account with the given email
     * @throws NoSuchElementException if the account is not found
     */
    public Account geAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow();
    }
}
