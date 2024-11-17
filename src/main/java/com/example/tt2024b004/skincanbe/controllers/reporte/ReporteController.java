/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase controlador para todo lo relacionado con los reportes.
 */

package com.example.tt2024b004.skincanbe.controllers.reporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tt2024b004.skincanbe.services.reporte.ReporteService;

@RestController
@CrossOrigin("*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;


    @PostMapping("/generar-reporte/{lesionId}")
    public ResponseEntity<String> generaryenviarReporte(@PathVariable Long lesionId) {
        try {
            reporteService.generarYEnviarReporte(lesionId);
            return ResponseEntity.ok("Reporte enviado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al generar o enviar el reporte.");
        }
    }
}
