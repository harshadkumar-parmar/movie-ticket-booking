package com.ps.booking.security;

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

import com.ps.booking.entity.Customer;
import com.ps.booking.exception.ResourceNotFoundException;
import com.ps.booking.repository.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@AllArgsConstructor
@Slf4j
public class ApplicationConfiguration {

  private final CustomerRepository customerRepository;

  @Bean
  UserDetailsService userDetailsService() {
    return username -> {
      log.trace("User details service called with username: {}", username);
      Customer customer = customerRepository.findByEmail(username)
          .orElseThrow(() -> new ResourceNotFoundException("User not found"));
      return new User(
        customer.getEmail(), 
        customer.getPassword(), 
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
