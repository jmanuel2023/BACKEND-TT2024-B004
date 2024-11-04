/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodriguez Juarez     **
 ******************************** 
 * Descripción: Clase servicio para implementar toda la lógica de negocios de las tareas que realiza la tabla TokenVerificación
 */
package com.example.tt2024b004.skincanbe.services.usuario;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.usuario.TokenVerificacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.usuario.TokenVerificacionRepository;

@Service
public class TokenVerificacionService {
    @Autowired
    private TokenVerificacionRepository tokenVerificacionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public String validarToken(String token) {
        Optional<TokenVerificacion> tokenV = tokenVerificacionRepository.findByToken(token);
        if (tokenV.isPresent()) {
            TokenVerificacion tokenValido = tokenV.get();
            Usuario usuario = tokenValido.getUsuario();

            if (tokenValido.getFechaExp().isBefore(LocalDateTime.now())) {
                return "Token expirado";
            }

            usuario.setStatus("Activo");
            usuarioService.actualizarUsuario(usuario);

            tokenVerificacionRepository.delete(tokenValido);
            return "Correo validado correctamente";
        } else {
            return "Token inválido";
        }
    }
}
