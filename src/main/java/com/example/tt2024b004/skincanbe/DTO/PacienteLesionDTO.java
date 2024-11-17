package com.example.tt2024b004.skincanbe.DTO;

import java.util.List;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PacienteLesionDTO {

    private Long id_paciente;
    private String nombre;
    private String apellidos;
    private String correo;
    private List<Lesion> lesiones;

    

}
