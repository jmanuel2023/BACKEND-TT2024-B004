package com.example.tt2024b004.skincanbe.repository.lesion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.usuario.Paciente;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Preubas unitarias de LesionRepository")
public class LesionRepositoryTest {

    @Autowired
    LesionRepository lesionRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach
    void setUp(){
         Usuario usuario = Paciente.builder()
                .nombre("Pedrito")
                .apellidos("Serrano Montes")
                .password("Pedrito123")
                .correo("pedrito@gmail.com")
                .edad(30)
                .status("Pendiente")
                .build();
        testEntityManager.persist(usuario);

        Lesion lesionprueba = new Lesion();
        lesionprueba.setFecha(LocalDate.now());
        lesionprueba.setNombre_lesion("Carcinoma Basocelular");
        lesionprueba.setDescripcion("Lesión de carcinoma basocelular");
        lesionprueba.setUsuario(usuario);
        testEntityManager.persist(lesionprueba);

    }
    @Test
    void testFindByUsuario_Id() {
        List<Lesion> listaLesion = lesionRepository.findByUsuario_Id(1L);
        assertEquals(LocalDate.now(), listaLesion.get(0).getFecha());

    }
}
