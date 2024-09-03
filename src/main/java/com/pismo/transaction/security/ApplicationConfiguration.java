package com.pismo.transaction.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pismo.transaction.entity.Account;
import com.pismo.transaction.exception.ResourceNotFoundException;
import com.pismo.transaction.repository.AccountRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@AllArgsConstructor
@Slf4j
public class ApplicationConfiguration {

  private final AccountRepository accountRepository;

  @Bean
  UserDetailsService userDetailsService() {
    return username -> {
      log.trace("User details service called with username: {}", username);
      Account account = accountRepository.findByEmail(username)
          .orElseThrow(() -> new ResourceNotFoundException("User not found"));
      return new User(
        account.getEmail(), 
        account.getPassword(), 
        true, true, 
        true, true, 
        List.of(new SimpleGrantedAuthority("USER")));
    };
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    log.trace("Password encoder called");
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    log.trace("Authentication manager called");
    return config.getAuthenticationManager();
  }

  @Bean
  AuthenticationProvider authenticationProvider() {
    log.trace("Authentication provider called");
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }
}
