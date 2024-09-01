package com.pismo.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

import com.pismo.demo.dto.LoginDto;
import com.pismo.demo.dto.RegisterDto;
import com.pismo.demo.entity.Account;
import com.pismo.demo.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account", description = "Account Management API")
public class AccountController {
   
    Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private AccountService accountService;

    @Operation(summary = "Account Login",
               description = "Login a user with a password, and email",
               responses = {
                   @ApiResponse(responseCode = "201", description = "User Login successfully", content=@Content),
                   @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                   @ApiResponse(responseCode = "500", description = "Internal Server Error")
               })
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto account) {
        try {
            logger.trace("in login");
            String token = accountService.login(account);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error on login", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }

     @Operation(summary = "Account creation",
               description = "Register a new user with a password, and email",
               responses = {
                   @ApiResponse(responseCode = "201", description = "User registered successfully"),
                   @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                   @ApiResponse(responseCode = "500", description = "Internal Server Error")
               })
    @PostMapping("/")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        try {
            accountService.createAccount(registerDto);
            return new ResponseEntity<>("Account created successfully.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }

    @Operation(summary = "Get account details",
    description = "Get account details based on account id",
    responses = {
        @ApiResponse(responseCode = "201", description = "User Login successfully", content=@Content),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable Long accountId) {
        try {
            Account account = accountService.getAccount(accountId);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }

    @Operation(summary = "Get account details",
    description = "Get account details based on account id",
    responses = {
        @ApiResponse(responseCode = "201", description = "User Login successfully", content=@Content),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/me")
    public ResponseEntity<Account> getCurrentAccount(@CurrentSecurityContext(expression = "authentication") 
    Authentication authentication) {
        try {
            Account account = accountService.geAccountByEmail(authentication.getName());
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
}