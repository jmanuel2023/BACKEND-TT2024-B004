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

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println(vinculo.getId());
        System.out.println(vinculo.getEspecialista().getId());
        System.out.println(vinculo.getPaciente().getId());
        System.out.println(vinculo.getFechaVinculacion());

        Optional<Usuario> paciente = userService.findById(vinculo.getPaciente().getId());
        if (paciente.isPresent()) {
            System.out.println("Paciente si existe");
            Vinculacion nuevoVinculo = vinculacionService.crearVinculo(vinculo);
            System.out.println(nuevoVinculo.getId());
            System.out.println(nuevoVinculo.getPaciente());
            System.out.println(nuevoVinculo.getEspecialista());
            System.out.println(nuevoVinculo.getFechaVinculacion());
            return new ResponseEntity<>(nuevoVinculo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
