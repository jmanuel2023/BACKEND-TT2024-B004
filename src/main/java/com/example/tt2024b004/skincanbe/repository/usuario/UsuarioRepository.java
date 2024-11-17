/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase repositorio para que se puedan hacer sentencias sql en la tabla Usuario
 */
package com.example.tt2024b004.skincanbe.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.tt2024b004.skincanbe.enums.EstadoVinculacion;
import com.example.tt2024b004.skincanbe.model.usuario.Especialista;

import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Consulta para buscar usuarios por nombre
    List<Usuario> findByNombre(String nombre);

    // Consulta para buscar un especialista por cedula
    List<Especialista> findByCedula(String cedula);

    Optional<Usuario> findByCorreo(String correo);

    // Consulta para buscar un usuario con un correo en especifico
    boolean existsByCorreo(String correo);

    // Consulta para buscar todos los pacientes
    @Query(value = "SELECT * FROM usuario WHERE tipo_usuario = 'Paciente'", nativeQuery = true)
    List<Usuario> findAllPacientes();

    // Consulta para buscar el usuario con un correo en especifico
    @Query(value = "SELECT * FROM usuario WHERE correo = ?1", nativeQuery = true)
    Usuario encontrarCorreo(String correo);

    // Consulta para obtener todos los usuarios especialistas
    @Query(value = "SELECT * FROM usuario WHERE tipo_usuario='Especialista'", nativeQuery = true)
    List<Especialista> findSpecialist();

    // Filtro para buscar especilistas por nombre y cedula
    @Query(value = "SELECT * FROM usuario WHERE tipo_usuario='Especialista' AND (nombre LIKE CONCAT ('%',?1,'%') OR cedula LIKE CONCAT ('%',?1,'%'))", nativeQuery = true)
    List<Especialista> findSpecialistByNombreYCedula(String filtro);

    @Query(value="SELECT u.id, u.nombre, u.apellidos, u.correo, l.*, DATE(l.fecha) " +
               "FROM vinculacion v " +
               "JOIN usuario u ON v.paciente_id = u.id " +
               "JOIN lesion l ON l.usuario_id = u.id " +
               "WHERE v.status = :status "+"AND v.especialista_id = :especialistaId", nativeQuery= true)
    List<Object[]> findPacientesWithLesionesByVinculacionStatus(@Param("status") EstadoVinculacion status, @Param("especialistaId") Long especialistaId);
}
