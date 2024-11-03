/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase entidad para crear la tabla TokenVerificación y 
 * declarar los atributos que tendra la tabla en la base de datos.
 */
package com.example.tt2024b004.skincanbe.model.usuario;

import java.time.LocalDateTime;
import java.util.UUID;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    
}
