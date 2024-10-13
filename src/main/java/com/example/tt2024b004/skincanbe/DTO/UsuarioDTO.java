package com.example.tt2024b004.skincanbe.DTO;

public class UsuarioDTO {

    private String nombre;
    private String apellidos;
    private int edad;
    private String correo;
    private String tipo_usuario;
    private String cedula;
    
    public UsuarioDTO() {
    }

    public UsuarioDTO(String nombre, String apellidos, int edad, String correo, String tipoUsuario, String cedula) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.correo = correo;
        this.tipo_usuario = tipoUsuario;
        this.cedula = cedula;
    }

    public UsuarioDTO(String nombre, String apellidos, int edad, String correo, String tipoUsuario) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.correo = correo;
        this.tipo_usuario = tipoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipoUsuario() {
        return tipo_usuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipo_usuario = tipoUsuario;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    
}
