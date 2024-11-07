package com.example.tt2024b004.skincanbe.repository.reporte;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.Reporte.Reporte;
import com.example.tt2024b004.skincanbe.model.usuario.Paciente;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;
import com.example.tt2024b004.skincanbe.repository.usuario.UsuarioRepository;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Prueba unitaria de ReporteRepository")
public class ReporteRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    LesionRepository lesionRepository;

    @Autowired
    ReporteRepository reporteRepository;

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
        lesionprueba.setTipo("Maligna");
        lesionprueba.setNombre_lesion("Carcinoma Basocelular");
        lesionprueba.setDescripcion("Lesión de carcinoma basocelular");
        lesionprueba.setUsuario(usuario);
        testEntityManager.persist(lesionprueba);

        Reporte reporteprueba = new Reporte();
        reporteprueba.setFecha_generacion("7-11-2024");
        reporteprueba.setDescripcion("Reporte de la lesion tal");
        reporteprueba.setLesion(lesionprueba);
        testEntityManager.persist(reporteprueba);


    }

    @Test
    void testFindByLesion() {
        Lesion lesionMuestra = new Lesion();
        lesionMuestra.setId_lesion(1L);
        lesionMuestra.setTipo("Maligna");
        lesionMuestra.setNombre_lesion("Carcinoma Basocelular");
        lesionMuestra.setDescripcion("Lesión de carcinoma basocelular");
        Optional<Reporte> registroReporte = reporteRepository.findByLesion(lesionMuestra);
        assertEquals(1L, registroReporte.get().getId_reporte()); 

    }
}
