package com.example.tt2024b004.skincanbe.security.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.services.usuario.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAttemptAuthentication() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Usuario usuario = new Usuario();
        usuario.setCorreo("test@example.com");
        usuario.setPassword("password");

        byte[] userJson = new ObjectMapper().writeValueAsBytes(usuario);
        when(request.getInputStream()).thenReturn(new DelegatingServletInputStream(new ByteArrayInputStream(userJson)));

        // Crear un mock de Authentication con valores específicos
        Authentication mockAuthentication = mock(Authentication.class);

        when(mockAuthentication.getPrincipal()).thenReturn("test@example.com");
        when(mockAuthentication.getCredentials()).thenReturn("password");

        // Usamos un Argument Matcher
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        // Ejecutamos el método de prueba
        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        assertNotNull(result);

        assertEquals("password", result.getCredentials());
    }

    @Test
    public void testSuccessfulAuthentication() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        Authentication authResult = mock(Authentication.class);

        CustomUserDetails user = mock(CustomUserDetails.class);
        when(authResult.getPrincipal()).thenReturn(user);
        when(user.getUsername()).thenReturn("test@example.com");
        when(user.getNombre()).thenReturn("Juan");
        when(user.getApellidos()).thenReturn("Perez");
        when(user.getTipoUsuario()).thenReturn("Paciente");
        when(user.getStatus()).thenReturn("Activo");
        when(user.getId()).thenReturn("1");

        // Mockear el PrintWriter para evitar NullPointerException
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        jwtAuthenticationFilter.successfulAuthentication(request, response, chain, authResult);

        verify(response).addHeader(eq("Authorization"), anyString());
        verify(response).setContentType("application/json");
        verify(response).setStatus(200);
    }

    @Test
    public void testUnsuccessfulAuthentication() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException failed = mock(AuthenticationException.class);

        when(failed.getMessage()).thenReturn("Authentication failed");

        // Mockear el PrintWriter para evitar NullPointerException
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        jwtAuthenticationFilter.unsuccessfulAuthentication(request, response, failed);

        verify(response).setStatus(401);
        verify(response).setContentType("application/json");
    }

}
