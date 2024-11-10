package com.example.tt2024b004.skincanbe.services.usuario;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.tt2024b004.skincanbe.model.usuario.Especialista;
import com.example.tt2024b004.skincanbe.model.usuario.Paciente;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.usuario.UsuarioRepository;

public class JpaUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private JpaUserDetailsService jpaUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        String email = "test@example.com";
        Usuario usuario = new Usuario();
        usuario.setCorreo(email);
        usuario.setPassword("pruebacontraseña");
        usuario.setNombre("Preuba");

        when(usuarioRepository.findByCorreo(email)).thenReturn(Optional.of(usuario));

        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String email = "notfound@example.com";

        when(usuarioRepository.findByCorreo(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            jpaUserDetailsService.loadUserByUsername(email);
        });

        assertEquals(String.format("El usuario asociado con el correo %s no existe en la base de datos de SkinCanBe!", email), exception.getMessage());
    }

    @Test
    public void testLoadUserByUsername_UserIsPaciente() {
        String email = "paciente@example.com";
        Paciente paciente = new Paciente();
        paciente.setCorreo(email);
        paciente.setPassword("pruebacontraseñaPaciente");
        paciente.setNombre("Prueba Paciente");

        when(usuarioRepository.findByCorreo(email)).thenReturn(Optional.of(paciente));

        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails instanceof CustomUserDetails);
        assertEquals("Paciente", ((CustomUserDetails) userDetails).getTipoUsuario());
    }

    @Test
    public void testLoadUserByUsername_UserIsEspecialista() {
        String email = "especialista@example.com";
        Especialista especialista = new Especialista();
        especialista.setCorreo(email);
        especialista.setPassword("pruebacontraseñaEspecialista");

        when(usuarioRepository.findByCorreo(email)).thenReturn(Optional.of(especialista));

        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails instanceof CustomUserDetails);
        assertEquals("Especialista", ((CustomUserDetails) userDetails).getTipoUsuario());
    }
}