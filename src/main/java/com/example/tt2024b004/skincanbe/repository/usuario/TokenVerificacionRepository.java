/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase repositorio para que se puedan hacer sentencias sql en la tabla TokenVerificación
 */
package com.example.tt2024b004.skincanbe.repository.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tt2024b004.skincanbe.model.usuario.TokenVerificacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

public interface TokenVerificacionRepository extends JpaRepository<TokenVerificacion, Long> {
    Optional<TokenVerificacion> findByToken(String token);

    void deleteByUsuario(Usuario usuario);

}
