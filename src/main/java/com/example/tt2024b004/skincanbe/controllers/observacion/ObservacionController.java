package com.example.tt2024b004.skincanbe.controllers.observacion;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tt2024b004.skincanbe.DTO.ObservacionConUsuarioDTO;
import com.example.tt2024b004.skincanbe.model.Observacion.Observacion;
import com.example.tt2024b004.skincanbe.services.observacion.ObservacionService;

@RestController
@CrossOrigin("*")
public class ObservacionController {

    @Autowired
    private ObservacionService observacionService;

    @PostMapping("/agregar-observacion/{id_lesion}/{id_especialista}")
    public ResponseEntity<?> agregarObservacionControlador (@RequestBody Map<String,String> descripcion, @PathVariable Long id_lesion, @PathVariable Long id_especialista){
        Observacion observacionControlador = observacionService.agregarObservacion(descripcion, id_lesion,id_especialista);
        if (observacionControlador != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(observacionControlador);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La lesión con el id proporcionado no fue encontrada.");
        }
    }

    @GetMapping("/historial-observaciones/{id_lesion}/{id_especialista}")
    public ResponseEntity<List<Observacion>> obtenerHistorialObservaciones (@PathVariable Long id_lesion, @PathVariable Long id_especialista){
        System.out.println("Entre al historial");
        List<Observacion> listaObservaciones = observacionService.obtenerHistorialObservacionesParaPantalla(id_lesion,id_especialista);
        System.out.println(listaObservaciones);
        return ResponseEntity.status(HttpStatus.CREATED).body(listaObservaciones);
    }
    
    @GetMapping("/historial-observaciones/{id_lesion}")
public ResponseEntity<List<ObservacionConUsuarioDTO>> obtenerHistorialObservacionesConUsuario(@PathVariable Long id_lesion) {
    System.out.println("Entre al historial total con usuario");
    List<ObservacionConUsuarioDTO> listaObservaciones = observacionService.obtenerHistorialObservacionesConUsuario(id_lesion);
    return ResponseEntity.status(HttpStatus.OK).body(listaObservaciones);
}



}
