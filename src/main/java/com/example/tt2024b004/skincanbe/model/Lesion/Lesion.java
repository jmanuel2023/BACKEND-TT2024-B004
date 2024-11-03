/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase entidad para crear la tabla Lesión y 
 * declarar los atributos que tendra la tabla en la base de datos.
 */
package com.example.tt2024b004.skincanbe.model.Lesion;

import com.example.tt2024b004.skincanbe.model.Reporte.Reporte;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    @JsonIgnore
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @OneToOne(mappedBy = "lesion", cascade = CascadeType.ALL)
    private Reporte reporte;
}
