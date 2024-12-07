/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase repositorio para que se puedan hacer sentencias sql en la tabla Vinculación
 */
package com.example.tt2024b004.skincanbe.repository.usuario;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tt2024b004.skincanbe.enums.EstadoVinculacion;
import com.example.tt2024b004.skincanbe.model.usuario.Especialista;
import com.example.tt2024b004.skincanbe.model.usuario.Paciente;
import com.example.tt2024b004.skincanbe.model.usuario.Vinculacion;

@Repository
public interface VinculacionRepository extends JpaRepository<Vinculacion, Long> {

    // @Query(value = "SELECT u.* FROM usuario u JOIN vinculacion v ON u.id = v.paciente_id WHERE v.especialista_id = ?1 AND v.status = ?2", nativeQuery = true)
    List<Vinculacion> findByEspecialistaIdAndStatus(Long especialistaId, EstadoVinculacion status);


    //Consulta para obtener la lista de vinculaciones
    @Query(value = "SELECT * FROM vinculacion WHERE especialista_id= ?2 AND paciente_id= ?1", nativeQuery = true)
    List<Vinculacion> findByIdentificadores(Long pacienteId, Long especialistaId);

    //Consulta para obtener una vinculacion en especifico
    @Query("SELECT v FROM Vinculacion v WHERE v.paciente.id = ?1 AND v.especialista.id = ?2")
    Vinculacion findByIds(Long pacienteId, Long especialistaId);

    void deleteAllByEspecialista (Especialista especialista);
    void deleteAllByPaciente (Paciente paciente);
}
