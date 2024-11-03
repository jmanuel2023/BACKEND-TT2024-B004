/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase entidad para crear la especialización Especialista en la tabla Usuario y 
 * declarar los atributos que tendra la tabla en la base de datos.
 */
package com.example.tt2024b004.skincanbe.model.usuario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/*Esta anotacion marca a esta clase como una entidad que se mapeara a la misma tabla que Usuario*/
@Entity
/*Esta anotacion sirve para que el valor "Especialista" se guarde en la columna tipo_usuario cuando un registro corresponda a un Especialista*/
@DiscriminatorValue("Especialista")
public class Especialista extends Usuario{
    /*Atributo adicional que solo tienen los especialista (no los pacientes)*/
    /*Este atribut se mapeara como una columna adicional en la tabla compartida, y su valor sera NULL para los pacientes*/
    private String cedula;

    /*Metodos getters y setters*/
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
}

