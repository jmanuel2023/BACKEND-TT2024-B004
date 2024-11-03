/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase entidad para crear la especialización Paciente de la tabla Usuario y 
 * declarar los atributos que tendra la tabla en la base de datos.
 */
package com.example.tt2024b004.skincanbe.model.usuario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/*Esta anotacion marca a Paciente como una entidad, aunque no sea crea una tabla separada, sino que se usa la misma tabla que Usuario*/
@Entity
/*Esta anotacion establece que si el valor de la columna tipo_usuario es "Paciente*, el ORM asociara este registro con la clase Paciente*/
@DiscriminatorValue("Paciente")
public class Paciente extends Usuario{
/*Clase Hija de Usuario, que hereda todos los atributos de Usuario, pero no añade ningun atributo adicional*/
/*Esta clase se creo con la unica razon de distinguir entre un paciente o un especialista a nivel logivo*/
}
