package com.example.tt2024b004.skincanbe.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.TokenVerificacion;
import com.example.tt2024b004.skincanbe.model.Usuario;
import com.example.tt2024b004.skincanbe.repository.TokenVerificacionRepository;

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
