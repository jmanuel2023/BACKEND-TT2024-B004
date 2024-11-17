package com.example.tt2024b004.skincanbe.repository.observacion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tt2024b004.skincanbe.model.Observacion.Observacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

@Repository
public interface ObservacionRepository extends JpaRepository<Observacion, Long> {

    @Query(value = "SELECT * FROM observacion WHERE lesion_id=?1 AND usuario_id IN(?2)", nativeQuery = true)
    List<Observacion> findHistorialLesionesParaReportes(Long id_lesion, List<Long> usuariosIds);

    @Query(value = "SELECT DISTINCT usuario_id FROM observacion  WHERE lesion_id=?1", nativeQuery = true)
    List<Long> findByIdPorUsuario(Long id_lesion);

    @Query(value = "SELECT * FROM observacion WHERE lesion_id=?1 AND usuario_id=?2", nativeQuery = true)
    List<Observacion> findHistorialLesionesParaHistorial(Long id_lesion, Long especialista_id);

    @Query(value = "SELECT o.*, u.nombre, u.apellidos FROM observacion o JOIN usuario u ON o.usuario_id = u.id WHERE o.lesion_id = ?1", nativeQuery = true)
    List<Object[]> findHistorialLesionesTotal(Long lesion_id);

    void deleteAllByUsuario(Usuario usuario);



}
