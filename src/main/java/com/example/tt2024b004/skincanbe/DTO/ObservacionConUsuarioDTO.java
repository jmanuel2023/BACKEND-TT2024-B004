package com.example.tt2024b004.skincanbe.DTO;

public class ObservacionConUsuarioDTO {
    private Long id;
    private String descripcion;
    private String fecha;
    private String nombreUsuario;
    private String apellidoUsuario;

    // Constructor
    public ObservacionConUsuarioDTO(Long id, String descripcion, String fecha, String nombreUsuario, String apellidoUsuario) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

}
