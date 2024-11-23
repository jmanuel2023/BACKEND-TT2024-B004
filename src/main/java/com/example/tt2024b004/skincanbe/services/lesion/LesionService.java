/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodriguez Juarez     **
 ******************************** 
 * Descripción: Clase servicio para implementar toda la lógica de negocios de las tareas que realiza la tabla Lesión
 */
package com.example.tt2024b004.skincanbe.services.lesion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.tt2024b004.skincanbe.DTO.PacienteLesionDTO;
import com.example.tt2024b004.skincanbe.enums.EstadoVinculacion;
import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;
import com.example.tt2024b004.skincanbe.repository.usuario.UsuarioRepository;

@Service
public class LesionService {
    private final String uploadDir = "src/main/resources/static/images/"; 
    
    @Autowired
    private LesionRepository lesionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Guardar lesion
    @Transactional
    public Lesion guardarLesion(Long idUsuario, String nombreLesion, String descripcion, MultipartFile imagen, String porcentaje) throws IOException{
        
        Usuario usuario = usuarioRepository.findById(idUsuario).
        orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));


        Lesion lesion= new Lesion();

        lesion.setNombre_lesion(nombreLesion);
        lesion.setDescripcion(descripcion);
        lesion.setPorcentaje(porcentaje);

        if(!imagen.isEmpty()){
            String imageFileName = System.currentTimeMillis() + "_"+ imagen.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + imageFileName);
            System.out.println(uploadDir+imageFileName);
            Files.copy(imagen.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            lesion.setImagen(imageFileName); 

        }

        lesion.setUsuario(usuario);
        lesion.setFecha(LocalDate.now());

        System.out.println("Lesion a guardar: " + lesion);
        return lesionRepository.save(lesion);
    }

    @Transactional(readOnly = true)
    public List<Lesion> obtenerLesionesPorUsuarioId(Long usuarioId) {
        return lesionRepository.findByUsuario_Id(usuarioId);
    }

    public List<PacienteLesionDTO> obtenerLesionesDePacientesConVinculacionAceptadas(Long especialistaId) {
        String baseUrl = "http://192.168.100.63:8080/images"; // Cambia esto por tu URL base para las imágenes
    
        List<Object[]> resultados = usuarioRepository.findPacientesWithLesionesByVinculacionStatus(EstadoVinculacion.ACEPTADO, especialistaId);
        Map<Long, PacienteLesionDTO> pacientesMap = new HashMap<>();
    
        for (Object[] row : resultados) {
            Long pacienteId = (Long) row[0];
            String nombre = (String) row[1];
            String apellidos = (String) row[2];
            String correo = (String) row[3];
    
            // Se crea la lesión
            Lesion lesion = new Lesion();
            lesion.setId_lesion((Long) row[4]);
            lesion.setDescripcion((String) row[5]);
            lesion.setFecha(((java.sql.Date) row[6]).toLocalDate());
    
            // Construir URL completa de la imagen
            String imagenNombre = (String) row[7];
            lesion.setImagen(baseUrl + "/" + imagenNombre);
    
            lesion.setNombre_lesion((String) row[8]);
            lesion.setPorcentaje((String) row[10]);
    
            // Verifica si el paciente ya está en el mapa
            if (!pacientesMap.containsKey(pacienteId)) {
                PacienteLesionDTO pacienteDto = new PacienteLesionDTO(pacienteId, nombre, apellidos, correo, new ArrayList<>());
                pacienteDto.getLesiones().add(lesion);
                pacientesMap.put(pacienteId, pacienteDto);
            } else {
                // Si el paciente ya está en el mapa, simplemente añade la lesión
                PacienteLesionDTO pacienteDto = pacientesMap.get(pacienteId);
                pacienteDto.getLesiones().add(lesion);
            }
        }
    
        // Retorna la lista de PacientesLesionDTO
        return new ArrayList<>(pacientesMap.values());
    }
    
        
    }
