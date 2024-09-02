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

@Configuration
@AllArgsConstructor
public class ApplicationConfiguration {

  private final AccountRepository accountRepository;

  @Bean
  UserDetailsService userDetailsService() {
    return username -> {
      System.out.println("------------------------------------");
      System.out.println("username: " + username);
      accountRepository.findAll().forEach(t-> System.out.println(t.getAccountId()+"======"+t.getPassword()+"--"+t.getEmail()));
      System.out.println("I got"+ accountRepository.findByEmail(username).get().getEmail());
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
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }
}
