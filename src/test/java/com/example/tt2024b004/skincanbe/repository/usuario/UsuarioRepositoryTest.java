package com.example.tt2024b004.skincanbe.repository.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.tt2024b004.skincanbe.model.usuario.Especialista;
import com.example.tt2024b004.skincanbe.model.usuario.Paciente;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Pruebas unitarias UsuarioRepository")
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsuarioRepository usuarioRepository;

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

        Paciente paciente = new Paciente();
        paciente.setNombre("Carlos");
        paciente.setApellidos("Noguez Salvador");
        paciente.setCorreo("carlitos@gmail.com");
        paciente.setPassword("Carlitos456");
        paciente.setStatus("Pendiente");
        paciente.setEdad(20);
        testEntityManager.persist(paciente);

        Especialista especialista = new Especialista();
        especialista.setNombre("Gabriela");
        especialista.setApellidos("Perez Perez");
        especialista.setCorreo("gabriela@gmail.com");
        especialista.setPassword("gaby1345");
        especialista.setStatus("Pendiente");
        especialista.setEdad(20);
        especialista.setCedula("345678");
        testEntityManager.persist(especialista);

    }

    @Test
    @DisplayName("Prueba unitaria metodo encontrarCorreo()")
    void testEncontrarCorreo() {
        Usuario usuarioprueba = usuarioRepository.encontrarCorreo("pedrito@gmail.com");
        assertEquals(usuarioprueba.getCorreo(), "pedrito@gmail.com");
        System.out.println("Usuario" + usuarioprueba);
    }

    @Test
    @DisplayName("Prueba unitaria del metodo existsByCorreo()")
    void testExistsByCorreo() {
        boolean exists = usuarioRepository.existsByCorreo("pedrito@gmail.com");
        assertTrue(exists, "El correo debería existir en la base de datos");
    }

    @Test
    @DisplayName("Prueba unitaria del metodo findAllPacientes()")
    void testFindAllPacientes() {
        List<Usuario> listaUsuarios = usuarioRepository.findAllPacientes();
        // List<Paciente> pacientes = listaUsuarios.stream()
        // .filter(usuario -> usuario instanceof Paciente)
        // .map(usuario -> (Paciente) usuario)
        // .collect(Collectors.toList());

        assertFalse(listaUsuarios.isEmpty(),
                "La lista de pacientes no debe estar vacía.");
        assertEquals(listaUsuarios.get(0).getCorreo(), "carlitos@gmail.com");
    }

    @Test
    @DisplayName("Prueba unitaria del metodo findByCedula()")
    void testFindByCedula() {
        List<Especialista> listaeEspecialistas = usuarioRepository.findByCedula("345678");
        assertEquals("345678", listaeEspecialistas.get(0).getCedula());
    }

     @Test
     @DisplayName("Prueba unitaria del metodo findByCorreo()")
      void testFindByCorreo() {
      Optional<Usuario> usuarioprueba = usuarioRepository.findByCorreo("carlitos@gmail.com");
      assertEquals("carlitos@gmail.com", usuarioprueba.get().getCorreo());
     }
     
     @Test
     @DisplayName("Prueba unitaria del metodo findByNombre()")
     void testFindByNombre() {
        List<Usuario> usuarioprueba = usuarioRepository.findByNombre("Gabriela");      
        assertEquals("Gabriela", usuarioprueba.get(0).getNombre());
     }
      
     @Test
     @DisplayName("Prueba unitaria del metodo findSpecialist()")
     void testFindSpecialist() {
     List<Especialista> especialistaPrueba = usuarioRepository.findSpecialist();
      assertFalse(especialistaPrueba.isEmpty(),
                "La lista de especialista no debe estar vacía.");
        assertEquals(especialistaPrueba.get(0).getCorreo(), "gabriela@gmail.com");
      
     }
      
      @Test
      @DisplayName("Prueba unitaria del metodo findSpecialistByNombreYCedula()")
      void testFindSpecialistByNombreYCedula() {
       List<Especialista> especialistaPrueba2 = usuarioRepository.findSpecialistByNombreYCedula("Ga");
      assertFalse(especialistaPrueba2.isEmpty(),
                "El filtro no trae nada.");
        assertEquals(especialistaPrueba2.get(0).getNombre(), "Gabriela");
      }
}
