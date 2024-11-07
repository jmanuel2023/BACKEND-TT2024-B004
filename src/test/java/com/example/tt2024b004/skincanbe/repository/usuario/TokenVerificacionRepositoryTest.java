package com.example.tt2024b004.skincanbe.repository.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.tt2024b004.skincanbe.model.usuario.Paciente;
import com.example.tt2024b004.skincanbe.model.usuario.TokenVerificacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

@DataJpaTest
@DisplayName("Pruebas unitarias de TokenVerificacionRepository")
@ActiveProfiles("test")
public class TokenVerificacionRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    TokenVerificacionRepository tokenVerificacionRepository;

    @BeforeEach
    void setUp() {
         Usuario usuario = Paciente.builder()
                .nombre("Pedrito")
                .apellidos("Serrano Montes")
                .password("Pedrito123")
                .correo("pedrito@gmail.com")
                .edad(30)
                .status("Pendiente")
                .build();
        testEntityManager.persist(usuario);
        TokenVerificacion token1 = TokenVerificacion.builder()
                .fechaExp(LocalDateTime.now())
                .token("hola a todos")
                .usuario(usuario)
                .build();
        testEntityManager.persist(token1);

    }

    @Test
    @DisplayName("Prueba unitaria del metodo findByToken()")
    void testFindByToken() {
        Optional<TokenVerificacion> pruebaToken = tokenVerificacionRepository.findByToken("hola a todos");
        assertEquals("hola a todos", pruebaToken.get().getToken());
    }
}
