package com.nus.iss.config.filter;

import com.nus.iss.config.security.AppUserDetailsService;
import com.nus.iss.config.security.JwtConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private AppUserDetailsService appUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtConfig, appUserDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtConfig, appUserDetailsService);
    }

    @Test
    void doFilterInternal_InvalidAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtConfig, appUserDetailsService);
    }

    @Test
    void doFilterInternal_ValidToken() throws ServletException, IOException {
        String jwt = "validJwtToken";
        String username = "testuser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtConfig.extractUsername(jwt)).thenReturn(username);
        when(appUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtConfig.isTokenValid(jwt)).thenReturn(true);
        when(userDetails.getPassword()).thenReturn("password");
        when(userDetails.getAuthorities()).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtConfig, times(1)).extractUsername(jwt);
        verify(appUserDetailsService, times(1)).loadUserByUsername(username);
        verify(jwtConfig, times(1)).isTokenValid(jwt);
        verify(filterChain, times(1)).doFilter(request, response);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(username, authentication.getPrincipal());
    }

}