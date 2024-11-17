/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase controlador para todo lo relacionado con las lesiones
 */
package com.example.tt2024b004.skincanbe.controllers.lesion;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tt2024b004.skincanbe.DTO.PacienteLesionDTO;
import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.services.lesion.LesionService;

@RestController
@CrossOrigin("*")
public class LesionController {

    @Autowired
    private LesionService lesionService;

    @GetMapping("/pacientes-vinculados-aceptados/{especialistaId}")
    public List<PacienteLesionDTO> obtenerLesionesDePacientesConVinculacionAceptada(@PathVariable Long especialistaId) {
        System.out.println("ENTRE EN EL METODO PARA OBTENER LOS PACIENTES VINCULADOS ACEPTADOS");
        return lesionService.obtenerLesionesDePacientesConVinculacionAceptadas(especialistaId);
    }

    @PostMapping("/register-injury")
    public ResponseEntity<Lesion> registrarLesion(
            @RequestParam("id_usuario") Long usuarioId,
            @RequestParam("nombre_lesion") String nombreLesion,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("imagen") MultipartFile imagen) throws IOException {
        System.out.println("Ya estoy en el controlador de la lesion");
        try {
            Lesion lesion = lesionService.guardarLesion(usuarioId, nombreLesion, descripcion, imagen);
            System.out.println("despues de la llamada del servicio");
            return ResponseEntity.status(HttpStatus.CREATED).body(lesion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   @GetMapping("/{usuarioId}")
public ResponseEntity<List<Map<String, Object>>> obtenerLesionesPorUsuarioId(@PathVariable Long usuarioId) {
    List<Lesion> lesiones = lesionService.obtenerLesionesPorUsuarioId(usuarioId);

    List<Map<String, Object>> response = lesiones.stream().map(lesion -> {
        Map<String, Object> lesionData = new HashMap<>();
        lesionData.put("id_lesion", lesion.getId_lesion());
        lesionData.put("fecha", lesion.getFecha());
        lesionData.put("nombre_lesion", lesion.getNombre_lesion());
        lesionData.put("descripcion", lesion.getDescripcion());
        // Aquí generas la URL completa
        lesionData.put("imagen", "http://192.168.100.63:8080/images/" + lesion.getImagen());
        return lesionData;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(response);
}


}
