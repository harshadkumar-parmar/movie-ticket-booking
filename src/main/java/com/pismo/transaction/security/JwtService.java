package com.pismo.transaction.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private long jwtExpiration;

  public String extractUsername(String token) {
    log.trace("Extracting username from token: {}", token);
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    log.trace("Extracting claim from token: {}", token);
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    log.trace("Generating token for user: {}", userDetails.getUsername());
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    log.trace("Generating token for user: {}", userDetails.getUsername());
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  private String buildToken(
    Map<String, Object> extraClaims,
    UserDetails userDetails,
    long expiration
  ) {
    log.trace("Building token for user: {}", userDetails.getUsername());
    return Jwts
      .builder()
      .setClaims(extraClaims)
      .setSubject(userDetails.getUsername())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(getSignInKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    log.trace("Checking if token is valid for user: {}", userDetails.getUsername());
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  /**
   * Checks if the given JWT token has expired.
   *
   * @param token the JWT token to check
   * @return true if the token has expired, false otherwise
   */
  private boolean isTokenExpired(String token) {
    log.trace("Checking if token is expired: {}", token);
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    log.trace("Extracting expiration from token: {}", token);
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    log.trace("Extracting all claims from token: {}", token);
    return Jwts
      .parserBuilder()
      .setSigningKey(getSignInKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private Key getSignInKey() {
    log.trace("Getting signing key: {}", secretKey);
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
