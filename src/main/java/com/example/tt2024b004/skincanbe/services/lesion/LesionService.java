package com.example.tt2024b004.skincanbe.services.lesion;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.Usuario;
import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.repository.UsuarioRepository;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;

@Service
public class LesionService {
    
    @Autowired
    private LesionRepository lesionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Guardar lesion
    @Transactional
    public Lesion guardarLesion(Map<String, Object> payload){
        String nombre_lesion = (String) payload.get("nombre_lesion");
        String descripcion = (String) payload.get("descripcion");
        String imagen = (String) payload.get("imagen");
        Long usuarioId = (Long)(payload.get("id_usuario"));
        
        Usuario usuario = usuarioRepository.findById(usuarioId).
        orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));


        Lesion lesion= new Lesion();

        lesion.setNombre_lesion(nombre_lesion);
        lesion.setDescripcion(descripcion);
        lesion.setImagen(imagen);
        lesion.setUsuario(usuario);
        
        System.out.println("Lesion a guardar: " + lesion);
        return lesionRepository.save(lesion);
    }

}
