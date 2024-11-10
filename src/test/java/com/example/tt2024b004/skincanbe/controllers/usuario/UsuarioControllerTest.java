package com.example.tt2024b004.skincanbe.controllers.usuario;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.tt2024b004.skincanbe.model.usuario.Especialista;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.security.JwtUtil;
import com.example.tt2024b004.skincanbe.services.usuario.TokenVerificacionService;
import com.example.tt2024b004.skincanbe.services.usuario.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TokenVerificacionService tokenVerificacionService;


    
    private Usuario usuario;

    private List<Especialista> especialistas;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombre("Octavio");
        usuario.setId(1L);
        usuario.setApellidos("Lopez");
        usuario.setCorreo("octavio@gmail.com");
        usuario.setEdad(25);
        usuario.setPassword("Octavitito1234");
        usuario.setStatus("Pendiente");

        Especialista especialista1 = new Especialista();
        especialista1.setNombre("Juan");
        especialista1.setCedula("12345");

        Especialista especialista2 = new Especialista();
        especialista2.setNombre("Pedrito");
        especialista2.setCedula("67890");

        especialistas = Arrays.asList(especialista1, especialista2);
    }

    @Test
    void testCrearUsuario() throws Exception {
        Map<String, Object> peticion = new HashMap<>();
        peticion.put("nombre", "Octavio");
        peticion.put("id", 1L);
        peticion.put("apellidos", "Lopez");
        peticion.put("correo", "octavio@gmail.com");
        peticion.put("password", "Octavitito1234");
        peticion.put("status", "Pendiente");
        peticion.put("edad", 25);
        // Aquí, Mockito.<Map<String, Object>>any() asegura que any() acepte cualquier
        // Map<String, Object> como argumento para crearUsuario, ayudando a Mockito y al
        // compilador de Java a entender que el tipo coincide exactamente con lo
        // esperado.
        Mockito.when(usuarioService.crearUsuario(Mockito.<Map<String, Object>>any())).thenReturn(usuario);

        // Realizar la solicitud POST y verificar la respuesta
        mockMvc.perform(post("/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(peticion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Octavio"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.apellidos").value("Lopez"))
                .andExpect(jsonPath("$.correo").value("octavio@gmail.com"))
                .andExpect(jsonPath("$.edad").value(25))
                // .andExpect(jsonPath("$.password").value("Octavitito1234"))
                .andExpect(jsonPath("$.status").value("Pendiente"));
    }

    @Test
    void testForgotPassword() throws Exception {

        Map<String, String> request = new HashMap<>();
        request.put("correo", "otrousuario@gmail.com");
        Mockito.when(usuarioService.existsByEmailUsuario("octavio@gmail.com")).thenReturn(null);

        // Realizar la solicitud POST y verificar la respuesta
        mockMvc.perform(post("/forgotpassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Correo no encontrado!"));
    }

    @Test
    void testObtenerEspecialistasPorNombreYCedula() throws Exception {
        String filtro = "Juan";
        Mockito.when(usuarioService.obtenerEspecialistasPorNomYCed("Juan")).thenReturn(especialistas);

        mockMvc.perform(get("/specialistFilter/" + filtro)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].cedula").value("12345"))
                .andExpect(jsonPath("$[1].nombre").value("Pedrito"))
                .andExpect(jsonPath("$[1].cedula").value("67890"));

    }

    @Test
    void testObtenerTodosLosEspecialistas() throws Exception {
        Mockito.when(usuarioService.obtenerTodosLosEspecialistas()).thenReturn(especialistas);
        mockMvc.perform(get("/specialistFilter")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].cedula").value("12345"))
                .andExpect(jsonPath("$[1].nombre").value("Pedrito"))
                .andExpect(jsonPath("$[1].cedula").value("67890"));
    }

    @Test
    void testResetPassword() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", "validToken");
        request.put("password", "newPassword");

        // Configurar el comportamiento del mock estático para JwtUtil
        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.validatePasswordResetToken("validToken")).thenReturn(true);
            mockedJwtUtil.when(() -> JwtUtil.getEmailFromPasswordResetToken("validToken"))
                    .thenReturn("octavio@gmail.com");

            // Configurar el comportamiento del servicio mock
            Mockito.when(usuarioService.resetNewPassword("octavio@gmail.com", "newPassword")).thenReturn(usuario);

            // Realizar la solicitud POST y verificar la respuesta
            mockMvc.perform(post("/reset-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Contraseña actualizada correctamente"));
        }
    }

    @Test
    void testValidarCorreo() throws Exception {
        String token = "validToken";
        Mockito.when(tokenVerificacionService.validarToken(token)).thenReturn("Correo validado correctamente");

        mockMvc.perform(get("/validar")
                .param("token", "validToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Correo validado correctamente"));

    }
}
