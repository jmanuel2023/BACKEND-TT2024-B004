/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase entidad para crear la tabla Observación y 
 * declarar los atributos que tendra la tabla en la base de datos.
 */

package com.example.tt2024b004.skincanbe.model.Observacion;

import java.time.LocalDate;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Observacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_observacion;

    private String descripcion;

    private LocalDate fecha;
    
    @ManyToOne
    @JoinColumn(name= "lesion_id")
    private Lesion lesion;

    @ManyToOne
    @JoinColumn(name= "usuario_id")
    private Usuario usuario;
    
}
