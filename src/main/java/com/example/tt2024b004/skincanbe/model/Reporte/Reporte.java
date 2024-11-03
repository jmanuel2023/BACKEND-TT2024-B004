/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase entidad para crear la tabla Reporte y 
 * declarar los atributos que tendra la tabla en la base de datos.
 */
package com.example.tt2024b004.skincanbe.model.Reporte;

// import java.util.List;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
// import com.example.tt2024b004.skincanbe.model.Observacion.Observacion;
import com.fasterxml.jackson.annotation.JsonIgnore;

// import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_reporte;

    private String fecha_generacion;
    private String descripcion;

    @OneToOne
    @JoinColumn(name= "lesion_id")
    @JsonIgnore
    private Lesion lesion;

    /*@OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL)
    private List<Observacion> observaciones;*/

    public Long getId_reporte() {
        return id_reporte;
    }

    public void setId_reporte(Long id_reporte) {
        this.id_reporte = id_reporte;
    }

    public String getFecha_generacion() {
        return fecha_generacion;
    }

    public void setFecha_generacion(String fecha_generacion) {
        this.fecha_generacion = fecha_generacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Lesion getLesion() {
        return lesion;
    }

    public void setLesion(Lesion lesion) {
        this.lesion = lesion;
    }

    /*public List<Observacion> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<Observacion> observaciones) {
        this.observaciones = observaciones;
    }*/

    
}
