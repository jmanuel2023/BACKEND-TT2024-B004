package com.example.tt2024b004.skincanbe.controllers.lesion;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.services.lesion.LesionService;

@RestController
@CrossOrigin("*")
public class LesionController {

    @Autowired
    private LesionService lesionService;

    @PostMapping("/register-injury")
    public ResponseEntity<?> registrarLesion(@RequestBody Map<String, Object> payload) {
        try {
            Lesion lesion = lesionService.guardarLesion(payload);
            return ResponseEntity.ok(lesion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
