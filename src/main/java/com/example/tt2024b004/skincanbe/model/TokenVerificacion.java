package com.example.tt2024b004.skincanbe.model;

import java.time.LocalDateTime;
import java.util.UUID;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class TokenVerificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime fechaExp;

    @OneToOne
    private Usuario usuario;

    public TokenVerificacion() {
    }

    public TokenVerificacion(Usuario usuario){
        this.usuario = usuario;
        this.token = UUID.randomUUID().toString();
        this.fechaExp = LocalDateTime.now().plusHours(24);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getFechaExp() {
        return fechaExp;
    }

    public void setFechaExp(LocalDateTime fechaExp) {
        this.fechaExp = fechaExp;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
}
