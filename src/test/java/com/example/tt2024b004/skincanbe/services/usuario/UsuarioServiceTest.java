package com.example.tt2024b004.skincanbe.services.usuario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.usuario.Especialista;
import com.example.tt2024b004.skincanbe.model.usuario.Paciente;
import com.example.tt2024b004.skincanbe.model.usuario.TokenVerificacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.usuario.TokenVerificacionRepository;
import com.example.tt2024b004.skincanbe.repository.usuario.UsuarioRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TokenVerificacionRepository tkVrfRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Long userId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(userId);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
    }

    @Test
    @Transactional(readOnly = true)
    public void testObtenerTodosLosEspecialistas() {
        Especialista especialista1 = new Especialista();
        Especialista especialista2 = new Especialista();
        List<Especialista> especialistas = Arrays.asList(especialista1, especialista2);

        when(usuarioRepository.findSpecialist()).thenReturn(especialistas);

        List<Especialista> result = usuarioService.obtenerTodosLosEspecialistas();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(especialista1));
        assertTrue(result.contains(especialista2));
    }

    @Test
    @Transactional(readOnly = true)
    public void testObtenerEspecialistasPorNomYCed() {
        String filtro = "Juan Perez";
        Especialista especialista1 = new Especialista();
        Especialista especialista2 = new Especialista();
        List<Especialista> especialistas = Arrays.asList(especialista1, especialista2);

        when(usuarioRepository.findSpecialistByNombreYCedula(filtro)).thenReturn(especialistas);

        List<Especialista> result = usuarioService.obtenerEspecialistasPorNomYCed(filtro);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(especialista1));
        assertTrue(result.contains(especialista2));
    }

    @Test
    public void testResetNewPassword_UserNotFound() {
        String correo = "test@example.com";
        String newPassword = "newPassword123";

        when(usuarioRepository.encontrarCorreo(correo)).thenReturn(null);

        Usuario result = usuarioService.resetNewPassword(correo, newPassword);

        assertNull(result);
    }

    @Test
    public void testResetNewPassword_UserFound() {
        String correo = "test@example.com";
        String newPassword = "newPassword123";
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);

        when(usuarioRepository.encontrarCorreo(correo)).thenReturn(usuario);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.resetNewPassword(correo, newPassword);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    public void testExistsByCorreo() {
        String correo = "test@example.com";

        when(usuarioRepository.existsByCorreo(correo)).thenReturn(true);

        boolean result = usuarioService.existsByCorreo(correo);

        assertTrue(result);
        verify(usuarioRepository).existsByCorreo(correo);
    }

    @Test
    public void testObtenerTodosLosUsuarios() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(usuario1));
        assertTrue(result.contains(usuario2));
    }

    @Test
    @Transactional
    public void testGuardarUsuario() throws MessagingException {
        Usuario usuario = new Usuario();
        usuario.setCorreo("prueba@gmail.com");
        usuario.setNombre("Prueba");
        usuario.setApellidos("Prueba");

        Usuario savedUsuario = new Usuario();
        savedUsuario.setId(1L);
        savedUsuario.setCorreo("prueba@gmail.com");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUsuario);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Usuario result = usuarioService.guardarUsuario(usuario);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @Transactional
    public void testCrearTokenVerificacion() throws MessagingException {
        Usuario usuario = new Usuario();
        usuario.setCorreo("prueba@gmail.com");
        TokenVerificacion token = new TokenVerificacion(usuario);

        when(tkVrfRepository.save(any(TokenVerificacion.class))).thenReturn(token);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Configura el spy para que no haga nada cuando se llama a enviarCorreoValidacion
        doNothing().when(usuarioService).enviarCorreoValidacion(any(Usuario.class), anyString());



        usuarioService.crearTokenVerificacion(usuario);

        verify(tkVrfRepository).save(any(TokenVerificacion.class));
        verify(usuarioService).enviarCorreoValidacion(eq(usuario), anyString());
    }

    @Test
    public void testEnviarCorreoValidacion() throws MessagingException {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@example.com");
        usuario.setNombre("Juan Perez");
        String token = "12345";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        usuarioService.enviarCorreoValidacion(usuario, token);

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    @Transactional
    public void testActualizarUsuario() {
        Usuario usuario = new Usuario();
        Usuario updatedUsuario = new Usuario();
        updatedUsuario.setId(1L);

        when(usuarioRepository.save(usuario)).thenReturn(updatedUsuario);

        Usuario result = usuarioService.actualizarUsuario(usuario);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    public void testCrearUsuario_Especialista() throws MessagingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("tipo_usuario", "Especialista");
        payload.put("cedula", "123456");
        payload.put("nombre", "Juan");
        payload.put("apellidos", "Perez");
        payload.put("edad", 30);
        payload.put("correo", "juan.perez@example.com");
        payload.put("password", "password123");

        when(usuarioRepository.existsByCorreo("juan.perez@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        Usuario savedUsuario = new Especialista();
        savedUsuario.setId(1L);
        savedUsuario.setCorreo("juan.perez@example.com");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUsuario);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Usuario result = usuarioService.crearUsuario(payload);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(usuarioRepository, times(2)).save(any(Usuario.class));
        verify(usuarioService).crearTokenVerificacion(savedUsuario);
    }

    @Test
    public void testCrearUsuario_Paciente() throws MessagingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("tipo_usuario", "Paciente");
        payload.put("nombre", "Maria");
        payload.put("apellidos", "Lopez");
        payload.put("edad", 25);
        payload.put("correo", "maria.lopez@example.com");
        payload.put("password", "password123");

        when(usuarioRepository.existsByCorreo("maria.lopez@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        Usuario savedUsuario = new Paciente();
        savedUsuario.setId(2L);
        savedUsuario.setCorreo("maria.lopez@example.com");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUsuario);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Usuario result = usuarioService.crearUsuario(payload);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(usuarioRepository, times(2)).save(any(Usuario.class));
        verify(usuarioService).crearTokenVerificacion(savedUsuario);
    }

    @Test
    public void testCrearUsuario_CorreoExistente() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("tipo_usuario", "Usuario");
        payload.put("correo", "existing@example.com");

        when(usuarioRepository.existsByCorreo("existing@example.com")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario(payload);
        });

        assertEquals("Esta correo ya esta registrado!", exception.getMessage());
    }

    @Test
    public void testSendPasswordResetEmail() throws MessagingException {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@example.com");
        usuario.setNombre("Juan Perez");
        String token = "12345";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        usuarioService.sendPasswordResetEmail(usuario, token);

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    public void testExistsByEmailUsuario() {
        String correo = "test@example.com";
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);

        when(usuarioRepository.encontrarCorreo(correo)).thenReturn(usuario);

        Usuario result = usuarioService.existsByEmailUsuario(correo);

        assertNotNull(result);
        assertEquals(correo, result.getCorreo());
        verify(usuarioRepository).encontrarCorreo(correo);
    }

}
