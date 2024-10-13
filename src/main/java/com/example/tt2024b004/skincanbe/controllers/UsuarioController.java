package com.example.tt2024b004.skincanbe.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tt2024b004.skincanbe.model.Especialista;
import com.example.tt2024b004.skincanbe.model.Paciente;
import com.example.tt2024b004.skincanbe.model.Usuario;
import com.example.tt2024b004.skincanbe.services.TokenVerificacionService;
import com.example.tt2024b004.skincanbe.services.UsuarioService;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenVerificacionService tokenVerificacionService;

    @Transactional
    @GetMapping("/validar")
    ResponseEntity<?> validarCorreo(@RequestParam("token") String token){

        String resultado = tokenVerificacionService.validarToken(token);

        if ("Correo validado correctamente".equals(resultado)) {
            return ResponseEntity.ok(resultado);
        } else if ("Token expirado".equals(resultado)) {
            return ResponseEntity.badRequest().body(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
        
    }


    //Guardar un usuario
    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody Map<String, Object> payload) throws MessagingException {
        String tipoUsuario = (String) payload.get("tipo_usuario");
        Usuario usuario;

        // Según el tipo de usuario en el JSON, instanciar la clase correspondiente
        if ("Especialista".equals(tipoUsuario)) {
            Especialista especialista = new Especialista();
            especialista.setCedula((String) payload.get("cedula"));
            usuario = especialista;
        } else if ("Paciente".equals(tipoUsuario)) {
            usuario = new Paciente(); // Paciente no tiene atributos adicionales
        } else {
            usuario = new Usuario(); // En caso de que no se especifique el tipo o sea "Usuario"
        }

        //Verificamos si el correo ya existe, ya que debe de ser unico
        if(usuarioService.existsByCorreo((String) payload.get("correo"))){
            return ResponseEntity.badRequest().body("Este correo ya esta registrado!");
        }else {
            int edadN = (Integer) payload.get("edad");
            usuario.setNombre((String) payload.get("nombre"));
            usuario.setApellidos((String) payload.get("apellidos"));
            usuario.setEdad(edadN);
            usuario.setCorreo((String) payload.get("correo"));
            String passwordN = (String) payload.get("password");
            String passwordEncoded = passwordEncoder.encode(passwordN);
            usuario.setPassword(passwordEncoded);

            // Guardar el usuario en la base de datos
            usuarioService.guardarUsuario(usuario);

            return ResponseEntity.ok(usuario);
        }

        
    }



    /*
    //Obtener todos los pacientes
    @GetMapping("/pacientes")
    public List<Paciente> getTodosLosPacientes() {
        return usuarioService.obtenerTodosLosPacientes();
    }

    //Obtener todos los usuarios
    @GetMapping("/all")
    public List<Usuario> getTodosLosUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    //Obtener usuarios por nombre
    @GetMapping("/nombre/{nombre}")
    public List<Usuario> getUsuariosPorNombre (@PathVariable String nombre){
        return usuarioService.obtenerUsuariosPorNombre(nombre);
    }

    //Obtener especialista por cedula
    @GetMapping("/especialista/{cedula}")
    public Especialista getEspecialistaPorCedula(@PathVariable String cedula){
        return usuarioService.obtenerEspecialistaPorCedula(cedula);
    }*/
    
    
}
