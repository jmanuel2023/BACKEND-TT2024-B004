package com.example.tt2024b004.skincanbe.services.lesion;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;
import com.example.tt2024b004.skincanbe.repository.usuario.UsuarioRepository;

public class LesionServiceTest {

    @Mock
    private LesionRepository lesionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private LesionService lesionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testGuardarLesion() throws IOException {
        Long idUsuario = 1L;
        String nombreLesion = "Lesion Test";
        String descripcion = "Descripcion Test";
        MockMultipartFile imagen = new MockMultipartFile("file", "test.jpg", "image/jpeg",
                "test image content".getBytes());

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Lesion lesion = new Lesion();
        lesion.setNombre_lesion(nombreLesion);
        lesion.setDescripcion(descripcion);
        lesion.setUsuario(usuario);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(lesionRepository.save(any(Lesion.class))).thenReturn(lesion);

        Lesion result = lesionService.guardarLesion(idUsuario, nombreLesion, descripcion, imagen);

        assertNotNull(result);
        assertEquals(nombreLesion, result.getNombre_lesion());
        assertEquals(descripcion, result.getDescripcion());
        assertEquals(usuario, result.getUsuario());
        verify(usuarioRepository).findById(idUsuario);
        verify(lesionRepository).save(any(Lesion.class));
    }

    @Test
    @Transactional(readOnly = true)
    public void testObtenerLesionesPorUsuarioId() {
        Long usuarioId = 1L;
        Lesion lesion1 = new Lesion();
        Lesion lesion2 = new Lesion();
        List<Lesion> lesiones = Arrays.asList(lesion1, lesion2);

        when(lesionRepository.findByUsuario_Id(usuarioId)).thenReturn(lesiones);

        List<Lesion> result = lesionService.obtenerLesionesPorUsuarioId(usuarioId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(lesion1));
        assertTrue(result.contains(lesion2));
        verify(lesionRepository).findByUsuario_Id(usuarioId);
    }
}
