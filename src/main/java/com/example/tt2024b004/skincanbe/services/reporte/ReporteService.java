package com.example.tt2024b004.skincanbe.services.reporte;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tt2024b004.skincanbe.model.Usuario;
import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.Reporte.Reporte;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;
import com.example.tt2024b004.skincanbe.repository.reporte.ReporteRepository;
import com.example.tt2024b004.skincanbe.services.UsuarioService;

@Service
public class ReporteService {

    private String rutaReporte = "src/main/resources/static/reportes";

    @Autowired
    private LesionRepository lesionRepository;
    @Autowired
    private ReporteRepository reporteRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private GenerarPdfService generarPdfService;

    public Reporte generarYEnviarReporte(Long lesionId) throws Exception {
        system.out.println("Inicio del metodo generarYEnviarReporte");
        Lesion lesion = lesionRepository.findById(lesionId)
                .orElseThrow(() -> new RuntimeException("Lesión no encontrada"));

        // Verificar si ya existe un reporte asociado
        Optional<Reporte> existeReporte = reporteRepository.findByLesion(lesion);
        if (existeReporte.isPresent()) {
            // Si ya existe, retornar el reporte existente
            system.out.println("Ya existe el reporte");
            String rutacompleta=rutaReporte + existeReporte.get().getId_reporte()+".pdf";
            system.out.println(rutacompleta);
            enviarCorreoReporte(existeReporte.get(), rutacompleta);
            return existeReporte.get();
        } else {
            // Crear y guardar el nuevo reporte
            system
            Reporte nuevoReporte = new Reporte();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-mm-yyyy HH:mm:ss");
            nuevoReporte.setLesion(lesion);

            Optional<Usuario> usuario = usuarioService.findById(lesion.getUsuario().getId());
            if (usuario.isPresent()) {
                nuevoReporte.setFecha_generacion(LocalDateTime.now().format(formato));
                nuevoReporte.setDescripcion("Reporte de la lesion del Paciente" + usuario.get().getNombre() + " "
                        + usuario.get().getApellidos());
                reporteRepository.save(nuevoReporte);
                // Generar el PDF (función ficticia `generarPDFReporte`)
                String file = generarPDFReporte(nuevoReporte);
                // Enviar el PDF por correo
                enviarCorreoReporte(nuevoReporte, file);
                return nuevoReporte;
            } else {
                return null;
            }
        }
    }

    private String generarPDFReporte(Reporte reporte) throws IOException {
        Lesion lesion = reporte.getLesion();
        if (lesion == null) {
            throw new IllegalArgumentException("El reporte no tiene una lesion asociada");
        } else {
            ByteArrayOutputStream baos = generarPdfService.generarPdfDeLesion(lesion);
            String filePath = "src/main/resources/static/reportes/reporte_" + reporte.getId_reporte() + ".pdf"; // Asumiendo que el
                                                                                                                // reporte tiene un ID
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                baos.writeTo(fos);
            }

            return filePath;
        }
    }

    private void enviarCorreoReporte(Reporte reporte, String archivo) throws Exception {
        // Configurar y enviar el correo electrónico con el PDF adjunto
        String destinatario = reporte.getLesion().getUsuario().getCorreo();
        String asunto = "Reporte de Lesión: " + reporte.getLesion().getNombre_lesion();
        String mensaje = "Hola, adjuntamos el reporte en formato pdf de la lesión solicitada."
                + "Equipo de Skincanbe.Saludos";

        emailService.sendEmailWithAttachment(destinatario, asunto, mensaje,archivo);
    }
}
