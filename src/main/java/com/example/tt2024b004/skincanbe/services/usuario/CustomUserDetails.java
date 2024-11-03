package com.example.tt2024b004.skincanbe.services.usuario;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private String tipoUsuario;
    private String nombre;
    private String apellidos;
    private String status;
    private String idUsuario;

    public CustomUserDetails(Usuario user, String tipoUsuario, List<GrantedAuthority> authorities) {
        super(user.getCorreo(), user.getPassword(), authorities);
        this.tipoUsuario = tipoUsuario;
        this.nombre = user.getNombre();
        this.apellidos = user.getApellidos();
        this.status = user.getStatus();
        this.idUsuario = String.valueOf(user.getId());
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }
    public String getNombre(){
        return nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public String getStatus() {
        return status;
    }
    public String getId(){
        return idUsuario;
    }
}
