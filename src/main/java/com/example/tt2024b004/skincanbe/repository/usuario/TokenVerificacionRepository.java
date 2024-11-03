package com.example.tt2024b004.skincanbe.repository.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tt2024b004.skincanbe.model.usuario.TokenVerificacion;

public interface TokenVerificacionRepository extends JpaRepository<TokenVerificacion, Long> {
    Optional<TokenVerificacion> findByToken(String token);

}
