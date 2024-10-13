package com.example.tt2024b004.skincanbe.services;

import com.example.tt2024b004.skincanbe.model.Usuario;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private String tipoUsuario;
    private String nombre;
    private String apellidos;

    public CustomUserDetails(Usuario user, String tipoUsuario, List<GrantedAuthority> authorities) {
        super(user.getCorreo(), user.getPassword(), authorities);
        this.tipoUsuario = tipoUsuario;
        this.nombre = user.getNombre();
        this.apellidos = user.getApellidos();
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
}
