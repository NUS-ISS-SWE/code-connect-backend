package com.nus.iss.config.security;

import com.nus.iss.entity.AppUser;
import com.nus.iss.repository.AppUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtConfigTest {

    private JwtConfig jwtConfig;

    @Mock
    private AppUserRepository appUserRepository;

    private SecretKey secretKey;
    private final String issuer = "nus-iss-codeconnect";
    private final long expirationTimeMillis = 86400000;

    @BeforeEach
    void setUp() {
        String secret = "bnVzLWlzcy1jb2RlY29ubmVjdC1qd3Qtc2VjcmV0LXRva2Vu";
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
        jwtConfig = new JwtConfig(issuer, secret, expirationTimeMillis, appUserRepository);
    }

    @Test
    void generateToken_Success() {
        String username = "testuser";
        String role = "EMPLOYER";

        String token = jwtConfig.generateToken(username, role);

        assertNotNull(token);
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(username, claims.getSubject());
        assertEquals(role, claims.get("ROLE"));
        assertEquals(issuer, claims.getIssuer());
    }

    @Test
    void extractUsername_Success() {
        String username = "testuser";
        String token = jwtConfig.generateToken(username, "USER");

        String extractedUsername = jwtConfig.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void isTokenValid_Success() {
        String username = "testuser";
        String token = jwtConfig.generateToken(username, "USER");

        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        when(appUserRepository.findByUsername(username)).thenReturn(Optional.of(appUser));

        boolean isValid = jwtConfig.isTokenValid(token);

        assertTrue(isValid);
        verify(appUserRepository, times(1)).findByUsername(username);
    }

    @Test
    void isTokenValid_UserNotFound() {
        String username = "unknownuser";
        String token = jwtConfig.generateToken(username, "USER");

        when(appUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean isValid = jwtConfig.isTokenValid(token);

        assertFalse(isValid);
        verify(appUserRepository, times(1)).findByUsername(username);
    }

    @Test
    void isTokenValid_TokenExpired() {
        String username = "testuser";
        String token = Jwts.builder()
                .issuer(issuer)
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().minusMillis(expirationTimeMillis)))
                .signWith(secretKey)
                .compact();
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> jwtConfig.isTokenValid(token));
    }
}