package com.example.tt2024b004.skincanbe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tt2024b004.skincanbe.model.Especialista;
import com.example.tt2024b004.skincanbe.model.Paciente;
import com.example.tt2024b004.skincanbe.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    
    //Consulta para buscar usuarios por nombre
    List<Usuario> findByNombre (String nombre);

    //Consulta para buscar un especialista por cedula
    List<Especialista> findByCedula(String cedula);

    Optional<Usuario> findByCorreo(String correo);

    //Consulta para buscar un usuario con un correo en especifico
    boolean existsByCorreo(String correo);

    //Consulta para buscar todos los pacientes
    @Query(value = "SELECT * FROM usuario WHERE tipo_usuario = 'Paciente'", nativeQuery = true)
    List<Paciente> findAllPacientes();

}
