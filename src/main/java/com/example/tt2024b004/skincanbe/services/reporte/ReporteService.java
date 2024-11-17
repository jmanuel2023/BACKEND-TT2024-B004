/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodriguez Juarez     **
 ******************************** 
 * Descripción: Clase servicio para implementar toda la lógica de negocios de las tareas que realiza la tabla Reporte.
 */
package com.example.tt2024b004.skincanbe.services.reporte;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.Observacion.Observacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;
import com.example.tt2024b004.skincanbe.repository.observacion.ObservacionRepository;
import com.example.tt2024b004.skincanbe.repository.usuario.UsuarioRepository;
import com.example.tt2024b004.skincanbe.services.observacion.ObservacionService;

@Service
public class ReporteService {

    private String rutaReporte = "src/main/resources/static/reportes/";

    @Autowired
    private LesionRepository lesionRepository;
 
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObservacionRepository observacionRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private ObservacionService observacionService;
    
    @Autowired
    private GenerarPdfService generarPdfService;

    public void generarYEnviarReporte(Long lesionId) throws Exception {
        System.out.println("Inicio del metodo generarYEnviarReporte");
        
        Lesion lesion = lesionRepository.findById(lesionId)
                .orElseThrow(() -> new RuntimeException("Lesión no encontrada"));

        // Verificar si ya existe un reporte asociado
        Optional<Usuario> usuario = usuarioRepository.findById(lesion.getUsuario().getId());
        if (usuario.isPresent()) {
            System.out.println("Existe el usuario relacionado con la lesion");
            String descripcion= "Reporte de la lesion del Paciente " + usuario.get().getNombre() + " "
            + usuario.get().getApellidos() + " " + lesion.getId_lesion();
            System.out.println("Generando el PDF del reporte ...");
            String file = generarPDFReporte(descripcion, lesion);
            
            System.out.println("Enviando el correo con el reporte adjunto...");
            enviarCorreoReporte(lesion, file);
            System.out.println("Correo electronico enviado exitosamente.");
        }else{
            System.out.println("No existe usuario relacionado con la lesión...");
        }
    }

    public String generarPDFReporte(String descripcion, Lesion lesion) throws IOException {
        System.out.println("Inicio del metodo generarPDFReporte");

        if (lesion == null) {
            System.out.println("El reporte no tiene lesión asociada");
            throw new IllegalArgumentException("El reporte no tiene una lesion asociada");
        } else {
            List<Long> observacionesPorLesion= observacionRepository.findByIdPorUsuario(lesion.getId_lesion());
            System.out.println("ID de la lesion: " + lesion.getId_lesion());
            System.out.println("ID del usuario: " + lesion.getUsuario().getId());
            List<Observacion> listaObservaciones = observacionService.obtenerHistorialObservaciones(lesion.getId_lesion(), observacionesPorLesion);
            
            System.out.println("Generando el archivo PDF...");
            ByteArrayOutputStream baos = generarPdfService.generarPdfDeLesion(lesion, listaObservaciones);
            String filePath = rutaReporte + descripcion + ".pdf"; // Asumiendo que el reporte tiene un ID
            System.out.println("Ruta del archivo PDF: "+ filePath);
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                baos.writeTo(fos);
            }

            return filePath;
        }
    }

    public void enviarCorreoReporte(Lesion lesion, String archivo) throws Exception {
        // Configurar y enviar el correo electrónico con el PDF adjunto
        System.out.println("Inicio del metodo enviarCorreoReporte");
        String destinatario = lesion.getUsuario().getCorreo();
        System.out.println("Destinatario: "+destinatario);
        String asunto = "Reporte de Lesión: " + lesion.getNombre_lesion();
        System.out.println("Asunto: "+ asunto);
        String mensaje = "Hola, adjuntamos el reporte en formato pdf de la lesión solicitada."
                + "\nEquipo de Skincanbe.\nSaludos";
        System.out.println("Mensaje" + mensaje);

        emailService.sendEmailWithAttachment(destinatario, asunto, mensaje,archivo);
    }
}
