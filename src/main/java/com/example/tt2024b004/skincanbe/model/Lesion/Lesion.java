package com.example.tt2024b004.skincanbe.model.Lesion;

import com.example.tt2024b004.skincanbe.model.Usuario;
import com.example.tt2024b004.skincanbe.model.Reporte.Reporte;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Lesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_lesion;

    private String tipo;
    private String nombre_lesion;
    private String descripcion;
    private String imagen;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @OneToOne(mappedBy = "lesion", cascade = CascadeType.ALL)
    private Reporte reporte;

    //Getters and Setters

    public Long getId_lesion() {
        return id_lesion;
    }

    public void setId_lesion(Long id_lesion) {
        this.id_lesion = id_lesion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre_lesion() {
        return nombre_lesion;
    }

    public void setNombre_lesion(String nombre_lesion) {
        this.nombre_lesion = nombre_lesion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    
    

}
