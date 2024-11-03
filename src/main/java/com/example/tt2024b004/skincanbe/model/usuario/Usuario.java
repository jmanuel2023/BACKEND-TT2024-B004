/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase entidad para crear la tabla Usuario y 
 * declarar los atributos que tendra la tabla en la base de datos.
 */
package com.example.tt2024b004.skincanbe.model.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*Esta anotacion le indica a JPA que Usuario es una clase que se debe mapear a una tabla en la base de datos*/
@Entity 
/*Esta anotacion especifica que se utilizara la estrategia de tabla unica (SINGLE_TABLE) para herencia 
 * lo cual significa que todas las clases hijas (Paciente y Especialista) se guardaran en la misma tabla que Usuario.*/
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
/*Esta anotacion crea una columna adicional en la tabla llamada tipo_usuario que almacenara un valor que identifica 
si el registro es de tipo "Paciente* o "Especialista"*/
/*De igual manera, esta columna sera utilizada por el ORM para determinar que clase hija (Paciente o Especialista) 
corresponde a un registro especifico */
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
public class Usuario {
    @Id /*Clave primaria que se genera automaticamente por la base de datos*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*Atributos comunes para cualquier usuario ya sea Paciente o Especialista*/
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(nullable = false, length = 100)
    private String apellidos;
    @Column(nullable = false)
    private int edad;
    @Column(nullable = false, length = 100, unique = true)
    private String correo;
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String status;

    
    /*Constructores */
    public Usuario() {
        this.status= "Pendiente";
    }

    


    public Usuario(Long id, String nombre, String apellidos, int edad, String correo, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.correo = correo;
        this.password = password;
    }
    
}


