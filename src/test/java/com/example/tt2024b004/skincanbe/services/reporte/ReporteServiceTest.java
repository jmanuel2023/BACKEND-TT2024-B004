package com.example.tt2024b004.skincanbe.services.reporte;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.Reporte.Reporte;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;
import com.example.tt2024b004.skincanbe.repository.reporte.ReporteRepository;
import com.example.tt2024b004.skincanbe.services.usuario.UsuarioService;

public class ReporteServiceTest {

    @Mock
    private LesionRepository lesionRepository;

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private GenerarPdfService generarPdfService;

    @InjectMocks
    private ReporteService reporteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testGenerarYEnviarReporte_ReporteExistente() throws Exception {
        
        
        Long lesionId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellidos("Perez");
        usuario.setCorreo("test@gmail.com");

        Lesion lesion = new Lesion();
        lesion.setId_lesion(lesionId);
        lesion.setUsuario(usuario);

        Reporte reporteExistente = new Reporte();
        reporteExistente.setDescripcion("Reporte existente");
        reporteExistente.setLesion(lesion);

        when(lesionRepository.findById(lesionId)).thenReturn(Optional.of(lesion));
        when(reporteRepository.findByLesion(lesion)).thenReturn(Optional.of(reporteExistente));

        Reporte result = reporteService.generarYEnviarReporte(lesionId);

        assertNotNull(result);
        assertEquals(reporteExistente, result);
    }

    @Test
    @Transactional
    public void testGenerarYEnviarReporte_ReporteNuevo() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellidos("Perez");
        
        Long lesionId = 1L;
        Lesion lesion = new Lesion();
        lesion.setId_lesion(lesionId);

        lesion.setUsuario(usuario);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        when(lesionRepository.findById(lesionId)).thenReturn(Optional.of(lesion));
        when(reporteRepository.findByLesion(lesion)).thenReturn(Optional.empty());
        when(usuarioService.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(generarPdfService.generarPdfDeLesion(any(Lesion.class))).thenReturn(baos);

        Reporte result = reporteService.generarYEnviarReporte(lesionId);

        assertNotNull(result);
        assertEquals("Reporte de la lesion del Paciente Juan Perez " + lesionId, result.getDescripcion());
    }

    @Test
    @Transactional
    public void testGenerarYEnviarReporte_UsuarioNoEncontrado() throws Exception {
        Long lesionId = 1L;
        Lesion lesion = new Lesion();
        lesion.setId_lesion(lesionId);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        lesion.setUsuario(usuario);

        when(lesionRepository.findById(lesionId)).thenReturn(Optional.of(lesion));
        when(reporteRepository.findByLesion(lesion)).thenReturn(Optional.empty());
        when(usuarioService.findById(usuario.getId())).thenReturn(Optional.empty());

        Reporte result = reporteService.generarYEnviarReporte(lesionId);

        assertNull(result);
        verify(reporteRepository, never()).save(any(Reporte.class));
    }

    @Test
    public void testGenerarPDFReporte() throws IOException {
        Reporte reporte = new Reporte();
        Lesion lesion = new Lesion();
        reporte.setLesion(lesion);
        reporte.setDescripcion("Reporte de prueba");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(generarPdfService.generarPdfDeLesion(lesion)).thenReturn(baos);

        String filePath = reporteService.generarPDFReporte(reporte);

        assertNotNull(filePath);
        assertTrue(filePath.contains("Reporte de prueba.pdf"));
        verify(generarPdfService).generarPdfDeLesion(lesion);
    }

    @Test
    public void testGenerarPDFReporte_SinLesion() {
        Reporte reporte = new Reporte();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reporteService.generarPDFReporte(reporte);
        });

        assertEquals("El reporte no tiene una lesion asociada", exception.getMessage());
    }

    @Test
    public void testEnviarCorreoReporte() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@example.com");

        Lesion lesion = new Lesion();
        lesion.setUsuario(usuario);
        lesion.setNombre_lesion("Lesion Test");

        Reporte reporte = new Reporte();
        reporte.setLesion(lesion);

        String archivo = "ruta/del/reporte.pdf";

        reporteService.enviarCorreoReporte(reporte, archivo);

        verify(emailService).sendEmailWithAttachment(
                eq("test@example.com"),
                eq("Reporte de Lesión: Lesion Test"),
                eq("Hola, adjuntamos el reporte en formato pdf de la lesión solicitada.Equipo de Skincanbe.Saludos"),
                eq(archivo));
    }
}
