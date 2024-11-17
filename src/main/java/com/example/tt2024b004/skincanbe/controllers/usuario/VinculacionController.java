/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase controlador para manejar los endpoint de lo relacionado con la vinculación entre el paciente y especialista.
 */
package com.example.tt2024b004.skincanbe.controllers.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tt2024b004.skincanbe.enums.EstadoVinculacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.model.usuario.Vinculacion;
import com.example.tt2024b004.skincanbe.services.usuario.UsuarioService;
import com.example.tt2024b004.skincanbe.services.usuario.VinculacionService;

@RestController
@CrossOrigin(origins = "*")
public class VinculacionController {
    @Autowired
    private UsuarioService userService;

    @Autowired
    private VinculacionService vinculacionService;


    @PostMapping("/vinculos/crear")
    public ResponseEntity<Vinculacion> crearVinculo(@RequestBody Vinculacion vinculo) {
        System.out.println("Entre al metodo crearVinculo del controlador");
        System.out.println(vinculo.getPaciente().getId());
        Optional<Usuario> paciente = userService.findById(vinculo.getPaciente().getId());
        if (paciente.isPresent()) {
            System.out.println("Paciente si existe");
            Vinculacion nuevoVinculo = vinculacionService.crearVinculo(vinculo);
            return new ResponseEntity<>(nuevoVinculo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Vinculacion> actualizarEstadoVinculacion(
            @PathVariable Long id,
            @RequestParam EstadoVinculacion estado) {
        Vinculacion vinculoActualizado = vinculacionService.actualizarEstadoVinculacion(id, estado);
        return new ResponseEntity<>(vinculoActualizado, HttpStatus.OK);
    }

    @GetMapping("/solicitudes/{especialistaId}")
    public ResponseEntity<List<Usuario>> obtenerSolicitudesPendientes(@PathVariable Long especialistaId) {
        System.out.println(especialistaId);
        List<Usuario> solicitudesPendientes = vinculacionService.obtenerSolicitudesPendientes(especialistaId);
        System.out.println(solicitudesPendientes);
        return new ResponseEntity<>(solicitudesPendientes, HttpStatus.OK);
    }

    @GetMapping("/vinculos/estado/{pacienteId}/{especialistaId}")
    public ResponseEntity<List<Vinculacion>> obtenerEstadoVinculacion(@PathVariable Long pacienteId, @PathVariable Long especialistaId){
        System.out.println("Entre al metodo obtenerEstadoVinculacion, sirve el initState");
        System.out.println(pacienteId);
        System.out.println(especialistaId);
        List<Vinculacion> vinculacionEstado = vinculacionService.obtenerEstadoVinculacion(pacienteId, especialistaId);
        System.out.println(vinculacionEstado);
        return new ResponseEntity<>(vinculacionEstado, HttpStatus.OK);
    }

    @PutMapping("vinculacion/aceptar/{pacienteId}/{especialistaId}")
    public ResponseEntity<String> aceptarVinculacion(
            @PathVariable Long pacienteId, 
            @PathVariable Long especialistaId) {
        vinculacionService.actualizarEstadoVinculacion(pacienteId, especialistaId, EstadoVinculacion.ACEPTADO);
        return new ResponseEntity<>("Vinculación aceptada", HttpStatus.OK);
    }

    @PutMapping("vinculacion/rechazar/{pacienteId}/{especialistaId}")
    public ResponseEntity<String> rechazarVinculacion(
            @PathVariable Long pacienteId, 
            @PathVariable Long especialistaId) {
        vinculacionService.actualizarEstadoVinculacion(pacienteId, especialistaId, EstadoVinculacion.RECHAZADO);
        return new ResponseEntity<>("Vinculación rechazada", HttpStatus.OK);
    }
}
