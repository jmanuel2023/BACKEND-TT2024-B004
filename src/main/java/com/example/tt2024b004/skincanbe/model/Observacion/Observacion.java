package com.example.tt2024b004.skincanbe.model.Observacion;

import com.example.tt2024b004.skincanbe.model.Reporte.Reporte;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Observacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_observacion;

    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name= "reporte_id")
    private Reporte reporte;

    public Long getId_observacion() {
        return id_observacion;
    }

    public void setId_observacion(Long id_observacion) {
        this.id_observacion = id_observacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }
    
}
