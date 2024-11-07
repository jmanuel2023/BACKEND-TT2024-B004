/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase controlador para todo lo relacionado con los usuarios.
 */

package com.example.tt2024b004.skincanbe.controllers.usuario;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tt2024b004.skincanbe.model.usuario.Especialista;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.security.JwtUtil;
import com.example.tt2024b004.skincanbe.services.usuario.TokenVerificacionService;
import com.example.tt2024b004.skincanbe.services.usuario.UsuarioService;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenVerificacionService tokenVerificacionService;

    @GetMapping("/specialistFilter")
    public ResponseEntity<List<Especialista>> obtenerTodosLosEspecialistas() {
        List<Especialista> especialistas = usuarioService.obtenerTodosLosEspecialistas();
        return ResponseEntity.ok(especialistas);
    }
    
    @GetMapping("/specialistFilter/{filtro}")
    public ResponseEntity<List<Especialista>> obtenerEspecialistasPorNombreYCedula(@PathVariable String filtro) {
        List<Especialista> filtroEspecialista = null;
        System.out.println(filtro);
        if (filtro.equals("nada")) {
            // Lógica para obtener todos los especialistas si el filtro está vacío
            filtroEspecialista = usuarioService.obtenerTodosLosEspecialistas();   
        } else {
            filtroEspecialista = usuarioService.obtenerEspecialistasPorNomYCed(filtro);
        }
        return ResponseEntity.ok(filtroEspecialista);
    }

    @GetMapping("/validar")
    ResponseEntity<?> validarCorreo(@RequestParam("token") String token) {

        String resultado = tokenVerificacionService.validarToken(token);

        if ("Correo validado correctamente".equals(resultado)) {
            return ResponseEntity.ok(resultado);
        } else if ("Token expirado".equals(resultado)) {
            return ResponseEntity.badRequest().body(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }

    }

    // Guardar un usuario
    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody Map<String, Object> payload) throws MessagingException {
        try {
            Usuario usuario = usuarioService.crearUsuario(payload);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) throws MessagingException {
        System.out.println("Ya entre al controlador, endpoint forgotpassword");
        String email = request.get("email");
        Usuario usuario = usuarioService.existsByEmailUsuario(email);
        if (usuario == null) {
            System.out.println("Correo no existe en la base de datos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Correo no encontrado!");
        } else {
            String token = JwtUtil.generatePasswordResetToken(usuario);
            usuarioService.sendPasswordResetEmail(usuario, token);
            System.out.println("Correo si existe, correo enviado");
            return ResponseEntity.ok("Correo enviado para recuperacion de contraseña");
        }

    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        System.out.println("Estoy en la funcion resetPassword, del controlador de usuario");
        String token = request.get("token");
        System.out.println(token);
        String newPassword = request.get("password");
        System.out.println(newPassword);

        // Validar token
        if (!JwtUtil.validatePasswordResetToken(token)) {
            System.out.println(!JwtUtil.validatePasswordResetToken(token));
            System.out.println("Entre al if del validate");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
        } else {
            // Extraer el usuario del token y actualizar la contraseña
            System.out.println("Entre al else del validate");
            String email = JwtUtil.getEmailFromPasswordResetToken(token);
            System.out.println(email);
            Usuario usuario = usuarioService.resetNewPassword(email, newPassword);
            if (usuario == null) {
                System.out.println(usuario);
                System.out.println("Entre al if del usuario null");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            } else {
                System.out.println("Entre al else del usuario null");
                return ResponseEntity.ok("Contraseña actualizada correctamente");
            }
        }

    }

}
