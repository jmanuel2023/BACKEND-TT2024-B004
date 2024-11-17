/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase repositorio para que se puedan hacer sentencias sql en la tabla Lesión
 */
package com.example.tt2024b004.skincanbe.repository.lesion;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

@Repository
public interface LesionRepository extends JpaRepository<Lesion,Long> {

    List<Lesion> findByUsuario_Id(Long usuarioId);
    void deleteAllByUsuario(Usuario usuario);
} 
