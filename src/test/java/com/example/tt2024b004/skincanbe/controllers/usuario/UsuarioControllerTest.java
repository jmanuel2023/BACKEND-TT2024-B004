package com.example.tt2024b004.skincanbe.controllers.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.services.usuario.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc MockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp(){
        usuario=new Usuario();
        usuario.setNombre("Octavio");
        usuario.setId(1L);
        usuario.setApellidos("Lopez");
        usuario.setCorreo("octavio@gamil.com");
        usuario.setEdad(25);
        usuario.setPassword("Octavitito1234");
        usuario.setStatus("Pendiente");
    }

    @Test
    void testCrearUsuario() {

    }

    @Test
    void testForgotPassword() {

    }

    @Test
    void testObtenerEspecialistasPorNombreYCedula() {

    }

    @Test
    void testObtenerTodosLosEspecialistas() {

    }

    @Test
    void testResetPassword() {

    }

    @Test
    void testValidarCorreo() {

    }
}
