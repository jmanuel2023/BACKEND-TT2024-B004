package com.example.tt2024b004.skincanbe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tt2024b004.skincanbe.model.TokenVerificacion;

public interface TokenVerificacionRepository extends JpaRepository<TokenVerificacion, Long> {
    Optional<TokenVerificacion> findByToken(String token);

}
