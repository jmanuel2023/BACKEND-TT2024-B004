package com.example.tt2024b004.skincanbe.services.reporte;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.Reporte.Reporte;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;
import com.example.tt2024b004.skincanbe.repository.reporte.ReporteRepository;
import com.example.tt2024b004.skincanbe.services.usuario.UsuarioService;

@Service
public class ReporteService {

    private String rutaReporte = "src/main/resources/static/reportes/";

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
        System.out.println("Inicio del metodo generarYEnviarReporte");
        Lesion lesion = lesionRepository.findById(lesionId)
                .orElseThrow(() -> new RuntimeException("Lesión no encontrada"));

        // Verificar si ya existe un reporte asociado
        Optional<Reporte> existeReporte = reporteRepository.findByLesion(lesion);
        if (existeReporte.isPresent()) {
            // Si ya existe, retornar el reporte existente
            System.out.println("Ya existe el reporte");
            String rutacompleta=rutaReporte + existeReporte.get().getDescripcion()+".pdf";
            System.out.println(rutacompleta);
            enviarCorreoReporte(existeReporte.get(), rutacompleta);
            return existeReporte.get();
        } else {
            // Crear y guardar el nuevo reporte
            System.out.println("No existe el reporte, se creará");
            Reporte nuevoReporte = new Reporte();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            nuevoReporte.setLesion(lesion);

            Optional<Usuario> usuario = usuarioService.findById(lesion.getUsuario().getId());
            if (usuario.isPresent()) {
                System.out.println("Existe el usuario relacionado con la lesion");
                nuevoReporte.setFecha_generacion(LocalDateTime.now().format(formato));
                System.out.println(LocalDateTime.now().format(formato));
                nuevoReporte.setDescripcion("Reporte de la lesion del Paciente " + usuario.get().getNombre() + " "
                        + usuario.get().getApellidos()+" "+lesion.getId_lesion());
                System.out.println(usuario.get().getNombre());
                System.out.println(usuario.get().getApellidos());
                String file = generarPDFReporte(nuevoReporte);
                System.out.println(file);
                reporteRepository.save(nuevoReporte);
                System.out.println("Se ha guardado el reporte");
                // Enviar el PDF por correo
                enviarCorreoReporte(nuevoReporte, file);
                System.out.println("Se ha mandado el correo electrónico");
                return nuevoReporte;
            } else {
                System.out.println("No existe usuario relacionado con la lesión");
                return null;
            }
        }
    }

    private String generarPDFReporte(Reporte reporte) throws IOException {
        System.out.println("Inicio del metodo generarPDFReporte");
        Lesion lesion = reporte.getLesion();
        if (lesion == null) {
            System.out.println("El reporte no tiene lesión asociada");
            throw new IllegalArgumentException("El reporte no tiene una lesion asociada");
        } else {
            System.out.println("Se empieza a generar el reporte");
            ByteArrayOutputStream baos = generarPdfService.generarPdfDeLesion(lesion);
            String filePath = rutaReporte + reporte.getDescripcion() + ".pdf"; // Asumiendo que el reporte tiene un ID
            System.out.println(filePath);
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                baos.writeTo(fos);
            }

            return filePath;
        }
    }

    private void enviarCorreoReporte(Reporte reporte, String archivo) throws Exception {
        // Configurar y enviar el correo electrónico con el PDF adjunto
        System.out.println("Inicio del metodo enviarCorreoReporte");
        String destinatario = reporte.getLesion().getUsuario().getCorreo();
        System.out.println(destinatario);
        String asunto = "Reporte de Lesión: " + reporte.getLesion().getNombre_lesion();
        System.out.println(asunto);
        String mensaje = "Hola, adjuntamos el reporte en formato pdf de la lesión solicitada."
                + "Equipo de Skincanbe.Saludos";
        System.out.println(mensaje);

        emailService.sendEmailWithAttachment(destinatario, asunto, mensaje,archivo);
    }
}
