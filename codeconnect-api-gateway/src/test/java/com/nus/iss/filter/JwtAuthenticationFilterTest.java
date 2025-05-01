package com.nus.iss.filter;

import com.nus.iss.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private GatewayFilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Captor
    private ArgumentCaptor<ServerWebExchange> exchangeCaptor;

    @Mock
    private Claims mockClaims;

    private MockServerWebExchange exchange;
    private final String testToken = "valid-jwt-token";
    private final String testUsername = "employee1";
    private final String testRole = "EMPLOYEE";

    @BeforeEach
    void setUp() {
    }

    private MockServerWebExchange createExchangeWithHeader(String headerName, String headerValue) {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/login")
                .header(headerName, headerValue)
                .build();
        return MockServerWebExchange.from(request);
    }

    private MockServerWebExchange createExchangeWithoutHeader() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/login").build();
        return MockServerWebExchange.from(request);
    }


    @Test
    void jwtRoleFilter_shouldAllowAccess_whenValidTokenAndRole() {
        List<String> allowedRoles = List.of("EMPLOYEE", "ADMIN");
        exchange = createExchangeWithHeader(HttpHeaders.AUTHORIZATION, "Bearer " + testToken);

        when(mockClaims.getSubject()).thenReturn(testUsername);
        when(mockClaims.get("ROLE", String.class)).thenReturn(testRole);

        when(jwtConfig.getClaims(testToken)).thenReturn(mockClaims);
        when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        GatewayFilter gatewayFilter = jwtAuthenticationFilter.jwtRoleFilter(allowedRoles);
        Mono<Void> result = gatewayFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(jwtConfig).getClaims(testToken);
        verify(mockClaims).getSubject();
        verify(mockClaims).get("ROLE", String.class);
        verify(filterChain).filter(exchangeCaptor.capture());

        ServerWebExchange mutatedExchange = exchangeCaptor.getValue();
        assertNotNull(mutatedExchange);
        assertEquals(testUsername, mutatedExchange.getRequest().getHeaders().getFirst("X-Cdcnt-Username"));
        assertEquals(testRole, mutatedExchange.getRequest().getHeaders().getFirst("X-Cdcnt-Role"));
        assertEquals(exchange.getRequest().getMethod(), mutatedExchange.getRequest().getMethod());
        assertEquals(exchange.getRequest().getPath(), mutatedExchange.getRequest().getPath());
    }

    @Test
    void jwtRoleFilter_shouldReturnUnauthorized_whenAuthHeaderMissing() {
        List<String> allowedRoles = List.of("ADMIN");
        exchange = createExchangeWithoutHeader();

        GatewayFilter gatewayFilter = jwtAuthenticationFilter.jwtRoleFilter(allowedRoles);
        Mono<Void> result = gatewayFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(jwtConfig, never()).getClaims(anyString());
        verify(filterChain, never()).filter(any(ServerWebExchange.class));
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        assertTrue(exchange.getResponse().isCommitted());
    }

    @Test
    void jwtRoleFilter_shouldReturnUnauthorized_whenAuthHeaderNotBearer() {
        List<String> allowedRoles = List.of("EMPLOYEE");
        exchange = createExchangeWithHeader(HttpHeaders.AUTHORIZATION, "Basic somecredentials");

        GatewayFilter gatewayFilter = jwtAuthenticationFilter.jwtRoleFilter(allowedRoles);
        Mono<Void> result = gatewayFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(jwtConfig, never()).getClaims(anyString());
        verify(filterChain, never()).filter(any(ServerWebExchange.class));
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        assertTrue(exchange.getResponse().isCommitted());
    }

    @Test
    void jwtRoleFilter_shouldReturnUnauthorized_whenTokenIsMalformedOrExpired() {
        List<String> allowedRoles = List.of("EMPLOYEE");
        exchange = createExchangeWithHeader(HttpHeaders.AUTHORIZATION, "Bearer " + testToken);

        when(jwtConfig.getClaims(testToken)).thenThrow(new JwtException("Invalid JWT"));

        GatewayFilter gatewayFilter = jwtAuthenticationFilter.jwtRoleFilter(allowedRoles);
        Mono<Void> result = gatewayFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(jwtConfig).getClaims(testToken);
        verify(filterChain, never()).filter(any(ServerWebExchange.class));
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        assertTrue(exchange.getResponse().isCommitted());
    }

    @Test
    void jwtRoleFilter_shouldReturnForbidden_whenRoleClaimIsMissing() {
        List<String> allowedRoles = List.of("EMPLOYEE");
        exchange = createExchangeWithHeader(HttpHeaders.AUTHORIZATION, "Bearer " + testToken);

        when(mockClaims.getSubject()).thenReturn(testUsername);
        when(mockClaims.get("ROLE", String.class)).thenReturn(null);

        when(jwtConfig.getClaims(testToken)).thenReturn(mockClaims);

        GatewayFilter gatewayFilter = jwtAuthenticationFilter.jwtRoleFilter(allowedRoles);
        Mono<Void> result = gatewayFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(jwtConfig).getClaims(testToken);
        verify(mockClaims).get("ROLE", String.class);
        verify(filterChain, never()).filter(any(ServerWebExchange.class));
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
        assertTrue(exchange.getResponse().isCommitted());
    }
}