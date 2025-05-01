package com.nus.iss.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JwtConfigTest {

    @Autowired
    private JwtConfig jwtConfig;

    private SecretKey key;

    @BeforeEach
    void setUp() {
        byte[] keyBytes = Decoders.BASE64.decode("bnVzLWlzcy1jb2RlY29ubmVjdC1qd3Qtc2VjcmV0LXRva2Vu");
        this.key = Keys.hmacShaKeyFor(keyBytes);

    }

    @Test
    void testGetClaims() {
        Map<String, String> claims = new HashMap<>();
        claims.put("ROLE", "ADMIN");
        String token = Jwts.builder()
                .claims(claims)
                .issuer("nus-iss")
                .subject("testUser")
                .issuedAt(Date.from(Instant.now()))
                .signWith(key)
                .compact();

        Claims parsedClaims = jwtConfig.getClaims(token);
        assertNotNull(parsedClaims);
        assertEquals("testUser", parsedClaims.getSubject());
        assertEquals("ADMIN", parsedClaims.get("ROLE"));
    }
}
