package com.example.tt2024b004.skincanbe.controllers.lesion;


import java.io.IOException; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.services.lesion.LesionService;

@RestController
@CrossOrigin("*")
public class LesionController {

    @Autowired
    private LesionService lesionService;

    @PostMapping("/register-injury")
    public ResponseEntity<?> registrarLesion (
        @RequestParam("id_usuario") Long usuarioId,
        @RequestParam("nombre_lesion") String nombreLesion,
        @RequestParam("descripcion") String descripcion,
        @RequestParam("imagen") MultipartFile imagen) throws IOException {
            System.out.println("Ya estoy en el controlador de la lesion");
        try {
            Lesion lesion = lesionService.guardarLesion(usuarioId, nombreLesion, descripcion, imagen);
            return ResponseEntity.ok(lesion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Lesion>> obtenerLesionesPorUsuarioId(@PathVariable Long usuarioId) {
        List<Lesion> lesiones = lesionService.obtenerLesionesPorUsuarioId(usuarioId);
        return ResponseEntity.ok(lesiones);
    }

}
