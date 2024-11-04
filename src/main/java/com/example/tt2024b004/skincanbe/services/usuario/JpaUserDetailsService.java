/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodriguez Juarez     **
 ******************************** 
 * Descripción: Clase de servicio para el control del endpoint /login. 
 */
package com.example.tt2024b004.skincanbe.services.usuario;


import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.usuario.Especialista;
import com.example.tt2024b004.skincanbe.model.usuario.Paciente;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.usuario.UsuarioRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Usuario> userOptional = repository.findByCorreo(email);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("El usuario asociado con el correo %s no existe en la base de datos de SkinCanBe!", email));
        }

        Usuario user = userOptional.orElseThrow();
        String tipoUsuario;
        if (user instanceof Paciente) {
            tipoUsuario = "Paciente";
        } else if (user instanceof Especialista) {
            tipoUsuario = "Especialista";
        } else {
            tipoUsuario = "Desconocido";  // Esto es solo por seguridad
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new CustomUserDetails(user, tipoUsuario, authorities);
    }
    
}
