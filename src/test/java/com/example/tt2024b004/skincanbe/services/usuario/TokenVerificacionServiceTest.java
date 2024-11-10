package com.example.tt2024b004.skincanbe.services.usuario;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.usuario.TokenVerificacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.usuario.TokenVerificacionRepository;

public class TokenVerificacionServiceTest {

    @Mock
    private TokenVerificacionRepository tokenVerificacionRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private TokenVerificacionService tokenVerificacionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testValidarToken_TokenValido() {
        String token = "validToken";
        Usuario usuario = new Usuario();
        TokenVerificacion tokenVerificacion = new TokenVerificacion();
        tokenVerificacion.setToken(token);
        tokenVerificacion.setUsuario(usuario);
        tokenVerificacion.setFechaExp(LocalDateTime.now().plusDays(1));

        when(tokenVerificacionRepository.findByToken(token)).thenReturn(Optional.of(tokenVerificacion));

        String result = tokenVerificacionService.validarToken(token);

        assertEquals("Correo validado correctamente", result);
        verify(usuarioService).actualizarUsuario(usuario);
        verify(tokenVerificacionRepository).delete(tokenVerificacion);
    }

    @Test
    @Transactional
    public void testValidarToken_TokenExpirado() {
        String token = "expiredToken";
        TokenVerificacion tokenVerificacion = new TokenVerificacion();
        tokenVerificacion.setToken(token);
        tokenVerificacion.setFechaExp(LocalDateTime.now().minusDays(1));

        when(tokenVerificacionRepository.findByToken(token)).thenReturn(Optional.of(tokenVerificacion));

        String result = tokenVerificacionService.validarToken(token);

        assertEquals("Token expirado", result);
        verify(usuarioService, never()).actualizarUsuario(any());
        verify(tokenVerificacionRepository, never()).delete(any());
    }

    @Test
    @Transactional
    public void testValidarToken_TokenInvalido() {
        String token = "invalidToken";

        when(tokenVerificacionRepository.findByToken(token)).thenReturn(Optional.empty());

        String result = tokenVerificacionService.validarToken(token);

        assertEquals("Token inválido", result);
        verify(usuarioService, never()).actualizarUsuario(any());
        verify(tokenVerificacionRepository, never()).delete(any());
    }
}

